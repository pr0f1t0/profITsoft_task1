package input;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ParseFromJson {

    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonFactory jsonFactory = mapper.getFactory();

    public Map<String, Long> getAttribute(String path, String attribute) {
        Path directory = Paths.get(path);
        ConcurrentMap<String, LongAdder> map = new ConcurrentHashMap<>();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
             Stream<Path> fileStream = Files.list(directory)){

            fileStream
                    .filter(Files::isRegularFile)
                    .forEach(file -> executor.submit(() -> processFile(file, attribute, map)));

        }
        catch (IOException e) {
            System.err.println("Error opening the directory: " + e.getMessage());
        }

        return map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().sum()));

    }

    private void processFile(Path file, String target, ConcurrentMap<String, LongAdder> map) {

        try(JsonParser parser = jsonFactory.createParser(file.toFile())){

            while (parser.nextToken() != null) {

                if (parser.getCurrentToken() == JsonToken.FIELD_NAME) {

                    if(target.equals(parser.currentName())) {

                        parser.nextToken();

                        String value = parser.getValueAsString();

                        addEntry(value, map);

                    }

                }

            }

        } catch (IOException e) {
            System.err.println("Error streaming file: " + e.getMessage());
        }
    }

    private void addEntry(String rawText, ConcurrentMap<String, LongAdder> map) {

        if (rawText == null || rawText.isBlank()) {
            map.computeIfAbsent("No Data", k -> new LongAdder()).increment();
        } else if (rawText.contains(",")) {

            for (String attribute : rawText.split(",")) {
                String cleaned = attribute.trim();

                if (!cleaned.isEmpty()) {
                    map.computeIfAbsent(cleaned, k -> new LongAdder()).increment();
                }

            }
        } else {
            map.computeIfAbsent(rawText.trim(), k -> new LongAdder()).increment();
        }
    }
}
