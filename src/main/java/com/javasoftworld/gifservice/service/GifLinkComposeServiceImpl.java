package com.javasoftworld.gifservice.service;

import com.javasoftworld.gifservice.model.GifFetchResponse;
import com.javasoftworld.gifservice.util.QuoteCompareResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GifLinkComposeServiceImpl implements GifLinkComposeService {

    private static final String RATING = "g";
    private static final int GIF_TO_FETCH_COUNT = 1;
    private final GiphyServiceClient giphyServiceClient;
    @Value("${api.giphy.api-key}")
    private String apiKey;
    @Value("${api.giphy.in-money}")
    private String levelUp;
    @Value("${api.giphy.out-money}")
    private String levelDown;
    @Value("${api.giphy.same-money}")
    private String sameLevel;

    public GifLinkComposeServiceImpl(GiphyServiceClient giphyServiceClient) {
        this.giphyServiceClient = giphyServiceClient;
    }

    @Override
    public String getGifLink(QuoteCompareResult result) {

        String gifSearchCriteria = "";
        switch (result) {
            case INCREASE:
                gifSearchCriteria = levelUp;
                break;
            case DECREASE:
                gifSearchCriteria = levelDown;
                break;
            default:
                gifSearchCriteria = sameLevel;
        }

        GifFetchResponse gifFetchResponse =
                giphyServiceClient.getRandomGifLinkByCriteria(apiKey, gifSearchCriteria,
                        GIF_TO_FETCH_COUNT, getRandomIndex(), RATING);

        String gifUrl = gifFetchResponse.getDataEntry().get(0).getOriginalSizeGifEntry().getGif().getLink();

        // truncate request parameters within gif file url
        gifUrl = gifUrl.split(".?cid=")[0];

        return gifUrl;
    }

    private int getRandomIndex() {
        Random random = new Random();
        return random.nextInt(100);
    }
}
