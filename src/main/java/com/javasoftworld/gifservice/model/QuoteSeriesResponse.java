package com.javasoftworld.gifservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteSeriesResponse {

    @JsonProperty("rates")
    private Map<String, Double> quotes;
}
