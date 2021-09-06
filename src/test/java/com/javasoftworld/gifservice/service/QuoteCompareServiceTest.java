package com.javasoftworld.gifservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javasoftworld.gifservice.exception.BadCurrencyCodeException;
import com.javasoftworld.gifservice.model.QuoteSeriesResponse;
import com.javasoftworld.gifservice.util.QuoteCompareResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class QuoteCompareServiceTest {

    private static QuoteSeriesResponse currentQuotes;
    private static QuoteSeriesResponse yesterdayQuotes;
    @Autowired
    private QuoteCompareService quoteCompareService;
    @MockBean
    private QuoteServiceClient quoteServiceClient;

    @BeforeAll
    static void beforeAll() {

        ObjectMapper mapper = new ObjectMapper();
        try {
            currentQuotes =
                    mapper.readValue(new File("src/test/resources/current-quote-series-response.json"),
                            QuoteSeriesResponse.class);
            yesterdayQuotes =
                    mapper.readValue(new File("src/test/resources/yesterday-quote-series-response.json"),
                            QuoteSeriesResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldBeReturnedIncreaseCompareStatus_WhenCompareRUBUSD() {
        // Given
        QuoteSeriesResponse mockCurrent = currentQuotes;
        QuoteSeriesResponse mockYesterday = yesterdayQuotes;

        String currency = "USD";

        doReturn(mockCurrent).when(quoteServiceClient).getLatestQuotes(anyString());
        doReturn(mockYesterday).when(quoteServiceClient).getQuotesForSpecificDate(anyString(), anyString());

        // When
        final QuoteCompareResult result = quoteCompareService.doCompare(currency);

        // Then
        Assertions.assertEquals(QuoteCompareResult.INCREASE, result);
    }

    @Test
    void shouldBeReturnedDecreaseCompareStatus_WhenCompareRUBUSD() {
        // Given
        QuoteSeriesResponse mockCurrent = yesterdayQuotes;
        QuoteSeriesResponse mockYesterday = currentQuotes;
        String currency = "USD";

        doReturn(mockCurrent).when(quoteServiceClient).getLatestQuotes(anyString());
        doReturn(mockYesterday).when(quoteServiceClient).getQuotesForSpecificDate(anyString(), anyString());
        // When
        final QuoteCompareResult result = quoteCompareService.doCompare(currency);

        // Then
        Assertions.assertEquals(QuoteCompareResult.DECREASE, result);
    }

    @Test
    void shouldBeReturnedEqualCompareStatus_WhenCompareRUBUSD() {
        // Given
        QuoteSeriesResponse mockCurrent = yesterdayQuotes;
        QuoteSeriesResponse mockYesterday = yesterdayQuotes;
        String currency = "USD";

        doReturn(mockCurrent).when(quoteServiceClient).getLatestQuotes(anyString());
        doReturn(mockYesterday).when(quoteServiceClient).getQuotesForSpecificDate(anyString(), anyString());

        // When
        final QuoteCompareResult result = quoteCompareService.doCompare(currency);

        // Then
        Assertions.assertEquals(QuoteCompareResult.EQUAL, result);
    }

    @Test
    void shouldBeThrownBadCurrencyCodeException_WhenUnexpectedCurrencyCodeUsed() {
        // Given
        QuoteSeriesResponse mockCurrent = yesterdayQuotes;
        QuoteSeriesResponse mockYesterday = yesterdayQuotes;
        String currency = "UUU";

        doReturn(mockCurrent).when(quoteServiceClient).getLatestQuotes(anyString());
        doReturn(mockYesterday).when(quoteServiceClient).getQuotesForSpecificDate(anyString(), anyString());

        // When
        // Then
        Assertions.assertThrows(BadCurrencyCodeException.class, () -> quoteCompareService.doCompare(currency));
    }

}