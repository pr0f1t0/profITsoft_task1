package models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record StatisticsItem(
        @JacksonXmlProperty(localName = "value")
        String value,
        @JacksonXmlProperty(localName = "count")
        long count
) {}
