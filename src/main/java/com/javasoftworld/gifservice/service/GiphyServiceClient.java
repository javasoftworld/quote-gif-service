package com.javasoftworld.gifservice.service;

import com.javasoftworld.gifservice.model.GifFetchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "giphy-fetch-service", url = "${api.giphy.url}")
public interface GiphyServiceClient {

    @GetMapping("${api.giphy.path}")
    GifFetchResponse getRandomGifLinkByCriteria(@RequestParam("api_key") String apiKey,
                                                @RequestParam("q") String searchKeyword,
                                                @RequestParam("limit") int limit,
                                                @RequestParam("offset") int offset,
                                                @RequestParam("rating") String rating);
}
