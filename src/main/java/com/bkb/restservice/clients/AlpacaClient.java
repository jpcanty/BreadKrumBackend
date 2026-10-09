package com.bkb.restservice.clients;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import java.time.DayOfWeek;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.bkb.restservice.ApplicationConfiguration;
import com.bkb.restservice.models.AlpacaWatchlist;
import com.bkb.restservice.models.AlpacaWatchlistWithAssets;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AlpacaClient {
    @Autowired
    private RestClient restClient;

    @Autowired
    @Qualifier(ApplicationConfiguration.ALPACA_OBJECT_MAPPER)
    private ObjectMapper objectMapper;
    private static final String BASE_URL = "https://api.alpaca.markets/v2/watchlists";
    private static final String APCA_API_KEY_ID = "AKWSFWM7A44H4OOBKXPE";
    private static final String APCA_API_SECRET_KEY = "K3pOFIa1upEHYxcpdI50CK86FQ6xKnLIC9UWvBqy";

    private static final String DATA_BASE_URL = "https://data.alpaca.markets/v2/stocks";


    public CompletionStage<List<AlpacaWatchlist>> getAllWatchlistsFuture() {
        return CompletableFuture.supplyAsync(() -> {
            return restClient.get()
                        .uri(BASE_URL)
                        .header("APCA-API-KEY-ID", APCA_API_KEY_ID)
                        .header("APCA-API-SECRET-KEY", APCA_API_SECRET_KEY)
                        .retrieve()
                        .body(String.class);
        }).thenApply(this::parseListResponse);
    }

    public CompletionStage<AlpacaWatchlistWithAssets> getWatchlistByIdFuture(String id) {
        return CompletableFuture.supplyAsync(() -> {
            return restClient.get()
                        .uri(BASE_URL + "/" + id)
                        .header("APCA-API-KEY-ID", APCA_API_KEY_ID)
                        .header("APCA-API-SECRET-KEY", APCA_API_SECRET_KEY)
                        .retrieve()
                        .body(String.class);

        }).thenApply(this::parseResponseWithAssets);
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

public CompletionStage<String> getStockPriceHistoryBySymbolsFuture(Set<String> symbols, String timeframe) {
    // Determine last weekday
    ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
    DayOfWeek day = now.getDayOfWeek();
    if (day == DayOfWeek.SATURDAY) {
        now = now.minusDays(1); // Friday
    } else if (day == DayOfWeek.SUNDAY) {
        now = now.minusDays(2); // Friday
    }

    // Format as ISO 8601
    String lastWeekday = now.toLocalDate().atStartOfDay(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_INSTANT);

    return CompletableFuture.supplyAsync(() -> {
        return restClient.get()
            .uri(DATA_BASE_URL + "/bars?symbols=" + String.join(",", symbols)
                 + "&timeframe=" + timeframe
                 + "&start=" + lastWeekday)
            .header("APCA-API-KEY-ID", APCA_API_KEY_ID)
            .header("APCA-API-SECRET-KEY", APCA_API_SECRET_KEY)
            .retrieve()
            .body(String.class);
        });
    }

    List<AlpacaWatchlist> parseListResponse(String response) {
        try {
            return objectMapper.readValue(response, new TypeReference<List<AlpacaWatchlist>>(){});
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return List.of();
        }
    }

    AlpacaWatchlist parseResponse(String response) {
        try {
            return objectMapper.readValue(response, AlpacaWatchlist.class);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new NullPointerException();
        }
    }

    AlpacaWatchlistWithAssets parseResponseWithAssets(String response) {
        try {
            return objectMapper.readValue(response, AlpacaWatchlistWithAssets.class);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new NullPointerException();
        }
    }
}
