package output;

import abstractions.OutputStats;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import models.StatisticsItem;
import models.StatisticsReport;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;


public class OutputToXml implements OutputStats {

    private final XmlMapper xmlMapper;

    public OutputToXml() {
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void outputToFile(Map<String, Long> stats, String attribute, Path output) {
        List<StatisticsItem> items = stats.entrySet().stream()
                .map(e -> new StatisticsItem(e.getKey(), e.getValue()))
                .toList();

        StatisticsReport report = new StatisticsReport(items);

        try{
            Path filePath = output.resolve("statistics_by_" + attribute + ".xml");
            xmlMapper.writeValue(filePath.toFile(), report);
        } catch (IOException e){
            System.err.println("Error creating a file: " + e.getMessage());
        }

    }

}
