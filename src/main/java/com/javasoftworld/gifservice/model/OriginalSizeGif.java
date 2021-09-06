package com.javasoftworld.gifservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OriginalSizeGif {

    @JsonProperty("original")
    public GifLink gif;
}
