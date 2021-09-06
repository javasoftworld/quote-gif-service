package com.javasoftworld.gifservice.service;

import com.javasoftworld.gifservice.exception.BadCurrencyCodeException;
import com.javasoftworld.gifservice.model.QuoteSeriesResponse;
import com.javasoftworld.gifservice.util.QuoteCompareResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class QuoteCompareServiceImpl implements QuoteCompareService {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private final QuoteServiceClient quoteServiceClient;
    @Value("${api.open-exchange-rates.api-id}")
    private String apiId;
    @Value("${api.open-exchange-rates.base:RUB}")
    private String baseCurrency;

    public QuoteCompareServiceImpl(QuoteServiceClient quoteServiceClient) {
        this.quoteServiceClient = quoteServiceClient;
    }

    @Override
    public QuoteCompareResult doCompare(String currencyToCompare) throws BadCurrencyCodeException {
        log.info("==> comparing cross rate currency: BASE {} CURRENCY_TO_LOOK {}", baseCurrency, currencyToCompare);

        final QuoteSeriesResponse todayQuotes = quoteServiceClient.getLatestQuotes(apiId);

        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        final String dayBeforeDate = LocalDate.now().minusDays(1L).format(dateFormatter);
        final QuoteSeriesResponse dayBeforeQuotes = quoteServiceClient.getQuotesForSpecificDate(dayBeforeDate, apiId);

        // https://svikk.biz/3-osnovnyx-sposoba-pravilno-raschitat-cross-kurs/
        try {
            double currentUsdToBaseCurrencyQuote = todayQuotes.getQuotes().get(baseCurrency);
            double yesterdayUsdToBaseCurrencyQuote = dayBeforeQuotes.getQuotes().get(baseCurrency);
            double currentUsdToDerivedCurrencyQuote = todayQuotes.getQuotes().get(currencyToCompare);
            double yesterdayUsdToDerivedCurrencyQuote = dayBeforeQuotes.getQuotes().get(currencyToCompare);

            double currentCrossRate =
                    calculateCurrencyCrossRate(currentUsdToDerivedCurrencyQuote, currentUsdToBaseCurrencyQuote);

            double yesterdayCrossRate =
                    calculateCurrencyCrossRate(yesterdayUsdToDerivedCurrencyQuote, yesterdayUsdToBaseCurrencyQuote);

            return currentCrossRate > yesterdayCrossRate ? QuoteCompareResult.INCREASE :
                    currentCrossRate < yesterdayCrossRate ? QuoteCompareResult.DECREASE : QuoteCompareResult.EQUAL;

        } catch (NullPointerException ex) {
            throw new BadCurrencyCodeException("Unexpected currency code encountered!");
        }
    }

    private double calculateCurrencyCrossRate(double currency, double base) {
        return currency / base;
    }
}
