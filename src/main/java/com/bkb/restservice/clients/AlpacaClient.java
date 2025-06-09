package com.bkb.restservice.clients;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AlpacaClient {
    @Autowired
    private RestClient restClient;
    private static final String BASE_URL = "https://api.alpaca.markets/v2/watchlists";
    private static final String APCA_API_KEY_ID = "AKWSFWM7A44H4OOBKXPE";
    private static final String APCA_API_SECRET_KEY = "K3pOFIa1upEHYxcpdI50CK86FQ6xKnLIC9UWvBqy";

    private static final String DATA_BASE_URL = "https://data.alpaca.markets/v2/stocks";


    public CompletionStage<String> getAllWatchlistsFuture() {
        return CompletableFuture.supplyAsync(() -> {
            return restClient.get()
                        .uri(BASE_URL)
                        .header("APCA-API-KEY-ID", APCA_API_KEY_ID)
                        .header("APCA-API-SECRET-KEY", APCA_API_SECRET_KEY)
                        .retrieve()
                        .body(String.class);
        });
    }

    public CompletionStage<String> getWatchlistByIdFuture(String id) {
        return CompletableFuture.supplyAsync(() -> {
            return restClient.get()
                        .uri(BASE_URL + "/" + id)
                        .header("APCA-API-KEY-ID", APCA_API_KEY_ID)
                        .header("APCA-API-SECRET-KEY", APCA_API_SECRET_KEY)
                        .retrieve()
                        .body(String.class);

        });
    }

    // "https://data.alpaca.markets/v2/stocks/:symbol/bars?timeframe=1D"

    public CompletionStage<String> getStockPriceHistoryBySymbolFuture(String symbol, String timeframe) {
        return CompletableFuture.supplyAsync(() -> {
            return restClient.get().uri(DATA_BASE_URL + "/" + symbol + "/bars?timeframe=" + timeframe)
            .header("APCA-API-KEY-ID", APCA_API_KEY_ID)
            .header("APCA-API-SECRET-KEY", APCA_API_SECRET_KEY)
            .retrieve()
            .body(String.class);
        });
    }
    
}
