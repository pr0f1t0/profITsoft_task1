import abstractions.OutputStats;
import input.ParseFromJson;
import output.OutputToXml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


public class Main {
    public static void main(String[] args) {

        ParseFromJson parser = new ParseFromJson();
        OutputStats output = new OutputToXml();


        ConsoleMenu consoleMenu = new ConsoleMenu();
        consoleMenu.showMainMenu();

        String path = consoleMenu.getPathToFiles();
        String attribute = consoleMenu.getTargetAttribute();
        Path outputPath = createOutputPath();

        Map<String, Long> map = parser.getAttribute(path, attribute);

        output.outputToFile(map, attribute, outputPath);

    }

    private static Path createOutputPath() {
        try{
            Path outputDirectory = Paths.get("xml_reports");

            return Files.createDirectories(outputDirectory);

        } catch (IOException e){
            System.err.println("Error creating a file: " + e.getMessage());

            throw new RuntimeException("Failed to initialize output directory", e);
        }
    }
}
