import input.ParseFromJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
public class ParseFromJsonTest {

    @TempDir
    Path tempDir;

    private final ParseFromJson parser = new ParseFromJson();

    @Test
    void shouldAggregateCommaSeparatedValues() throws IOException {
        createJsonFile("first.json", """
            {
              "manufacturer": "Apple",
              "model": "MacBook Air M2",
              "price": 1199,
              "ram": 8,
              "ports": "Thunderbolt, MagSafe, Audio-Jack"
            }
            """);

        createJsonFile("second.json", """
            {
              "manufacturer": "Dell",
              "model": "XPS 13 Plus",
              "price": 1450,
              "ram": 16,
              "ports": "Thunderbolt, USB-C"
            }
            """);

        createJsonFile("third.json", """
            {
              "manufacturer": "Lenovo",
              "model": "ThinkPad X1 Carbon",
              "price": 1800,
              "ram": 16,
              "ports": "HDMI, Thunderbolt, USB-A, Audio-Jack"
            }
            """);

        Map<String, Long> result = parser.getAttribute(tempDir.toString(), "ports");

        assertThat(result)
                .containsEntry("Thunderbolt", 3L)
                .containsEntry("Audio-Jack", 2L)
                .containsEntry("MagSafe", 1L)
                .containsEntry("USB-C", 1L)
                .containsEntry("HDMI", 1L)
                .containsEntry("USB-A", 1L);

    }

    @Test
    void shouldHandleEmptyValues() throws IOException {
        createJsonFile("null_val.json", """
        {
          "manufacturer": "Dell",
          "ports": null
        }
        """);

        createJsonFile("empty_val.json", """
        {
          "manufacturer": "Apple",
          "ports": ""
        }
        """);

        createJsonFile("blank_val.json", """
        {
          "manufacturer": "Lenovo",
          "ports": "   "
        }
        """);

        Map<String, Long> result = parser.getAttribute(tempDir.toString(), "ports");

        assertThat(result)
                .containsEntry("No Data", 3L);
    }

    @Test
    void shouldSurviveMalformedJsonFiles() throws IOException {

        createJsonFile("right.json", "{ \"manufacturer\": \"Apple\" }");

        createJsonFile("wrong.json", "{ \"manufacturer\": \"Dell\"");

        createJsonFile("garbage.json", "This is just random text.");

        Map<String, Long> result = parser.getAttribute(tempDir.toString(), "manufacturer");

        assertThat(result)
                .containsEntry("Apple", 1L)
                .containsEntry("Dell", 1L)
                .hasSize(2);
    }

    @Test
    void shouldReturnEmptyMapForNonExistentDirectory() {

        Map<String, Long> result = parser.getAttribute("wrong/path", "attr");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyMapIfPathIsAFileNotDirectory() throws IOException {
        Path singleFile = tempDir.resolve("file.txt");
        Files.createFile(singleFile);

        Map<String, Long> result = parser.getAttribute(singleFile.toString(), "attr");

        assertThat(result).isEmpty();
    }


    private void createJsonFile(String filename, String content) throws IOException {
        Path file = tempDir.resolve(filename);
        Files.writeString(file, content);
    }
}

