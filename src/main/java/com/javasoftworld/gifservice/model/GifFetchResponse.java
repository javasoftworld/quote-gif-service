package com.javasoftworld.gifservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GifFetchResponse {

    @JsonProperty("data")
    public List<GifImageList> dataEntry;

}
