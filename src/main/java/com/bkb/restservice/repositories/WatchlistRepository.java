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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;

//Represents a single item in the "results" array from back4app
//@JsonProperty is used to map the JSON key "watchlist_name" to the Java record component "name"
record EnabledWatchlist(@JsonProperty("name") String name) {}

//Represents the overall JSON object returned by the Back4app query
record EnabledWatchlistsResponse(List<EnabledWatchlist> results){}

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
                // Deserialize the response into Watchlist
                return objectMapper.readValue(response, StockPrice.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to process JSON response", e);
            }
        });
    }

    public CompletionStage<List <String>> getEnabledWatchlists(){
        return back4AppClient.getEnabledWatchlistsFuture().thenApply(response ->{
            try{
                EnabledWatchlistsResponse enabledWatchlistsResponse = objectMapper.readValue(response, EnabledWatchlistsResponse.class);

                return enabledWatchlistsResponse.results().stream()
                        .map(EnabledWatchlist::name)
                        .collect(Collectors.toList());
            }catch(JsonProcessingException e){
                throw new RuntimeException("Failed to process JSON response for enabled watchlists" , e);
            }
        });
    }
}
