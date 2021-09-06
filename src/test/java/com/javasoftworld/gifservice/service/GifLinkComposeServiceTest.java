package com.javasoftworld.gifservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javasoftworld.gifservice.model.GifFetchResponse;
import com.javasoftworld.gifservice.util.QuoteCompareResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class GifLinkComposeServiceTest {

    private static GifFetchResponse gifFetchResponse;
    @Autowired
    private GifLinkComposeService gifLinkComposeService;
    @MockBean
    private GiphyServiceClient giphyServiceClient;

    @BeforeAll
    static void beforeAll() {

        ObjectMapper mapper = new ObjectMapper();
        try {
            gifFetchResponse =
                    mapper.readValue(new File("src/test/resources/giphy-stub-response.json"),
                            GifFetchResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldBeReturnedGifLink_ThenGifLinkFetched() {
        // Given
        QuoteCompareResult mockQuoteCompareResult = QuoteCompareResult.INCREASE;
        String mockGifLink = "https://media3.giphy.com/media/1n4bA8eF1Qymgj1W3M/giphy.gif";
        doReturn(gifFetchResponse)
                .when(giphyServiceClient).getRandomGifLinkByCriteria(
                        anyString(), anyString(), anyInt(), anyInt(), anyString());

        // When
        String derivedGifLink = gifLinkComposeService.getGifLink(mockQuoteCompareResult);
        // Then
        assertEquals(mockGifLink, derivedGifLink);
    }

}