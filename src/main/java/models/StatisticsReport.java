package models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public record StatisticsReport(
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "item")
        List<StatisticsItem> items
) {}
