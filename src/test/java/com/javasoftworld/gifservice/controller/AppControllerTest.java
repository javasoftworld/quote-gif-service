package com.javasoftworld.gifservice.controller;

import com.javasoftworld.gifservice.exception.BadCurrencyCodeException;
import com.javasoftworld.gifservice.service.GifLinkComposeService;
import com.javasoftworld.gifservice.service.QuoteCompareService;
import com.javasoftworld.gifservice.util.QuoteCompareResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AppControllerTest {

    @MockBean
    private GifLinkComposeService gifLinkComposeService;

    @MockBean
    private QuoteCompareService quoteCompareService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void shouldBeMovedToGiphy_WhenProperCurrencyCodeEntered() throws Exception {
        // Given
        String properCurrencyCode = "USD";
        String mockGifLink = "/somewhere/to/Giphy/file/link";
        var mockCompareResult = QuoteCompareResult.EQUAL;

        doReturn(mockGifLink).when(gifLinkComposeService).getGifLink(mockCompareResult);
        doReturn(mockCompareResult).when(quoteCompareService).doCompare(properCurrencyCode);
        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/compare/{currencyCode}", properCurrencyCode))
                // Then
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, mockGifLink));
    }

    @Test
    void shouldBeReturn400ErrorCode_WhenInvalidCurrencyCodeEntered() throws Exception {
        // Given
        String invalidCode = "UUU";
        doThrow(BadCurrencyCodeException.class).when(quoteCompareService).doCompare(anyString());
        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/compare/{currencyCode}", invalidCode))
                // Then
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadCurrencyCodeException))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldBeReturn400ErrorCode_WhenNonISO4217CurrencyCodeEntered() throws Exception {
        // Given
        String nonIso4217CurrencyCode = "UUUU";
        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/compare/{currencyCode}",
                        nonIso4217CurrencyCode))
                // Then
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(status().isBadRequest());
    }

}