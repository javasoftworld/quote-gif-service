package com.javasoftworld.gifservice.service;

import com.javasoftworld.gifservice.model.QuoteSeriesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "quote-fetch-service", url = "${api.open-exchange-rates.url}")
public interface QuoteServiceClient {

    // Get the latest exchange rates available from the Open Exchange Rates API.
    @GetMapping("${api.open-exchange-rates.path-latest}")
    QuoteSeriesResponse getLatestQuotes(@RequestParam("app_id") String apiId);

    // Get historical exchange rates for any date available from the Open Exchange Rates API,
    // currently going back to 1st January 1999.
    @GetMapping("/api/historical/{date}.json")
    QuoteSeriesResponse getQuotesForSpecificDate(@PathVariable("date") String date,
                                                 @RequestParam("app_id") String apiId);
}
