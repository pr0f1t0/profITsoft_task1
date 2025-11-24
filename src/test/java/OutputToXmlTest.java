import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import output.OutputToXml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class OutputToXmlTest {
    @TempDir
    public Path tempDir;

    @Test
    void outputToFileShouldWriteToProvidedPath() throws IOException {
        OutputToXml outputToXml = new OutputToXml();
        String attribute = "ports";

        Map<String, Long> stats = Map.of(
                "MicroSD", 168L,
                "Audio-Jack", 301L,
                "Nano-SIM", 7L,
                "Expansion-Card", 7L,
                "SD-Card", 161L,
                "USB-A", 518L,
                "USB-C", 322L,
                "SmartCard", 14L,
                "SD-Express", 14L,
                "Mini-DisplayPort", 42L
        );

        outputToXml.outputToFile(stats, attribute, tempDir);

        Path expectedFile = tempDir.resolve("statistics_by_ports.xml");
        assertTrue(Files.exists(expectedFile));

        String content = Files.readString(expectedFile);

        stats.forEach((key, value) -> {
            assertTrue(content.contains("<value>" + key + "</value>"), "Missing key: " + key);
            assertTrue(content.contains("<count>" + value + "</count>"), "Missing count for: " + key);
        });
    }

}

