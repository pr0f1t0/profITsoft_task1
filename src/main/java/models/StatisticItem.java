package models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record StatisticItem(
        @JacksonXmlProperty(localName = "value")
        String value,
        @JacksonXmlProperty(localName = "count")
        long count
) {}
