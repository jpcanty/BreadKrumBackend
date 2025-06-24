package com.bkb.restservice.repositories;

import java.util.Set;
import java.util.concurrent.CompletionStage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;

import com.bkb.restservice.clients.AlpacaClient;
import com.bkb.restservice.clients.Back4AppClient;
import com.bkb.restservice.models.StockPrice;
import com.bkb.restservice.models.Watchlist;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class WatchlistRepository {
    @Autowired
    private Back4AppClient back4AppClient;
    @Autowired
    private AlpacaClient alpacaClient;
    @Autowired
    private ObjectMapper objectMapper;

    public CompletionStage<String> postWatchlist(Watchlist watchlist) {
        return back4AppClient.postWatchlistFuture(watchlist).thenApply(response -> {
            Boolean isOk = response.getStatusCode() == HttpStatusCode.valueOf(201);

            if (isOk) {
                return watchlist.id();
            }
            throw new RuntimeException("Not ok response: " + response.getStatusCode().toString());
        });
    }

    public CompletionStage<Watchlist[]> getAllWatchlists() {
        return alpacaClient.getAllWatchlistsFuture().thenApply(response -> {
            try {
                // Deserialize the response into Watchlist[]
                return objectMapper.readValue(response, Watchlist[].class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to process JSON response", e);
            }
        });
    }

    public CompletionStage<Watchlist> getWatchlistById(String id) {
        return alpacaClient.getWatchlistByIdFuture(id).thenApply(response -> {
            try {
                // Deserialize the response into Watchlist
                return objectMapper.readValue(response, Watchlist.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to process JSON response", e);
            }
        });
    }

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
