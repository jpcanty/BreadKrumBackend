package com.bkb.restservice.repositories;

import java.util.Set;
import java.util.concurrent.CompletionStage;

import org.springframework.beans.factory.annotation.Autowired;

import com.bkb.restservice.clients.AlpacaClient;
import com.bkb.restservice.models.StockPrice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StockPriceRepository {
    @Autowired
    private AlpacaClient alpacaClient;
    @Autowired
    private ObjectMapper objectMapper;

    public CompletionStage<StockPrice> getStockPriceHistoryBySymbol(String symbol, String timeframe) {
        return alpacaClient.getStockPriceHistoryBySymbolsFuture(Set.of(symbol), timeframe).thenApply(response -> {
            try {
                System.out.println(response);
                // Deserialize the response into Watchlist
                return objectMapper.readValue(response, StockPrice.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to process JSON response", e);
            }
        });
    }

    public CompletionStage<StockPrice> getStockPriceHistoryBySymbols(Set<String> symbols, String timeframe) {
        return alpacaClient.getStockPriceHistoryBySymbolsFuture(symbols, timeframe).thenApply(response -> {
            try {
                System.out.println(response);
                // Deserialize the response into Watchlist
                return objectMapper.readValue(response, StockPrice.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to process JSON response", e);
            }
        });
    }

}
