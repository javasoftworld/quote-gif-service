package com.javasoftworld.gifservice.controller;

import com.javasoftworld.gifservice.exception.BadCurrencyCodeException;
import com.javasoftworld.gifservice.service.GifLinkComposeService;
import com.javasoftworld.gifservice.service.QuoteCompareService;
import com.javasoftworld.gifservice.util.QuoteCompareResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class AppController {

    private final static String ISO_4217_CODE_PATTERN = "^[A-Z]{3}$";

    private final QuoteCompareService quoteCompareService;
    private final GifLinkComposeService gifLinkComposeService;

    public AppController(QuoteCompareService quoteCompareService, GifLinkComposeService gifLinkComposeService) {
        this.quoteCompareService = quoteCompareService;
        this.gifLinkComposeService = gifLinkComposeService;
    }

    @GetMapping("/compare/{currencyCode}")
    public ResponseEntity<?> doProcess(@PathVariable @Valid @Pattern(regexp = ISO_4217_CODE_PATTERN,
            message = "Should be a valid ISO-4217 alphabetic currency code!") String currencyCode) {

        QuoteCompareResult result = quoteCompareService.doCompare(currencyCode);

        String redirectUrlLinkToGifFile = gifLinkComposeService.getGifLink(result);
        log.info("==> Result of comparison is {}. Composed link is {}", result.toString(), redirectUrlLinkToGifFile);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(redirectUrlLinkToGifFile))
                .build();
    }

    @ExceptionHandler(BadCurrencyCodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleBadCurrencyCodeException(BadCurrencyCodeException e) {
        return new ResponseEntity<>("Validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("Validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
