package com.bkb.restservice.clients;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.bkb.restservice.models.Back4AppWatchlistsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.bkb.restservice.ApplicationConfiguration;
import com.bkb.restservice.models.Asset;
import com.bkb.restservice.models.Back4AppWatchlist;
import com.bkb.restservice.models.Watchlist.WatchlistRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Back4AppClient {
    @Autowired
    private RestClient restClient;
    @Autowired
    @Qualifier(ApplicationConfiguration.BACK4APP_OBJECT_MAPPER)
    private ObjectMapper objectMapper;
    private static final String BACK_4_APP_APPLICATION_ID = "19FwHl4gMBw72qM8OHaGyCgm038AjHznnh4E5Wmz";
    private static final String BACK_4_APP_REST_API_KEY = "URGqznPEwDMGaPmkyCJMbx7SCtvCOfDWPlvp26gF";
    private static final String ENABLEDWATCHLIST_TABLE = "EnabledWatchlists";

    public CompletionStage<String> getWatchlistObjectId(String watchlistName) {
        return CompletableFuture.supplyAsync(() -> {
            String where;
            try {
                where = objectMapper.writeValueAsString(
                        Map.of("name", watchlistName));
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new NullPointerException();
            }

            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host("parseapi.back4app.com")
                    .path("/classes/Watchlist3")
                    .queryParam("where", where)
                    .build()
                    .encode()
                    .toUri();

            return restClient.get()
                    .uri(uri)
                    .header("X-Parse-Application-Id", BACK_4_APP_APPLICATION_ID)
                    .header("X-Parse-REST-API-Key", BACK_4_APP_REST_API_KEY)
                    .retrieve()
                    .body(String.class);
        });
        // .thenApply(this::parseResponse).thenApply(Back4AppWatchlistsResponse::results).thenApply(List::stream)
        // .thenApply(stream -> stream.findAny().orElseThrow());
    }

    public CompletionStage<ResponseEntity<Void>> postWatchlistFuture(WatchlistRecord watchlist) {
        return CompletableFuture.supplyAsync(() -> {
            String tableName = "Watchlist3";
            return restClient.post()
                    .uri("https://parseapi.back4app.com/classes/" + tableName)
                    .header("X-Parse-Application-Id", BACK_4_APP_APPLICATION_ID)
                    .header("X-Parse-REST-API-Key", BACK_4_APP_REST_API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(buildWatchlistBody(watchlist))
                    .retrieve()
                    .toBodilessEntity();
        });
    }

    public CompletionStage<String> getEnabledWatchlistsFuture() {
        return CompletableFuture.supplyAsync(() -> {
            return restClient.get() // GET request
                    .uri("https://parseapi.back4app.com/classes/" + ENABLEDWATCHLIST_TABLE)
                    .header("X-Parse-Application-Id", BACK_4_APP_APPLICATION_ID)
                    .header("X-Parse-REST-API-Key", BACK_4_APP_REST_API_KEY)
                    .retrieve() // execute the request
                    .body(String.class); // the response is in a String
        });
    }

    // We want to write a row to a table with a POST
    // Name of the table: EnabledWatchlists
    // {"name":"Shoes"}

    public CompletionStage<ResponseEntity<Void>> enableWatchlist(String watchlistName) {

        return CompletableFuture.supplyAsync(() -> {
            return restClient.post()
                    .uri("https://parseapi.back4app.com/classes/" + ENABLEDWATCHLIST_TABLE)
                    .header("X-Parse-Application-Id", BACK_4_APP_APPLICATION_ID)
                    .header("X-Parse-REST-API-Key", BACK_4_APP_REST_API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("name", watchlistName))
                    .retrieve()
                    .toBodilessEntity();
        });
    }

    public CompletionStage<ResponseEntity<Void>> deleteWatchlist(String objectId) {
        // delete watchlist with object id
        return CompletableFuture.supplyAsync(() -> {
            return restClient.delete()
                    .uri("https://parseapi.back4app.com/classes/" + ENABLEDWATCHLIST_TABLE + "/" + objectId)
                    .header("X-Parse-Application-Id", BACK_4_APP_APPLICATION_ID)
                    .header("X-Parse-REST-API-Key", BACK_4_APP_REST_API_KEY)
                    .retrieve()
                    .toBodilessEntity();
        });
    }

    public Back4AppWatchlistsResponse getAllWatchlists() {
        try {

            // 3. Execute the GET request using RestClient
            Back4AppWatchlistsResponse apiResponse = restClient.get()
                    .uri("https://parseapi.back4app.com/classes/" + "Watchlist3")
                    .header("X-Parse-Application-Id", BACK_4_APP_APPLICATION_ID)
                    .header("X-Parse-REST-API-Key", BACK_4_APP_REST_API_KEY)
                    .retrieve()
                    .body(Back4AppWatchlistsResponse.class);

            // 4. Extract the objectId safely
            // if (apiResponse != null && apiResponse.results() != null &&
            // !apiResponse.results().isEmpty()) {
            // String objectId = apiResponse.results().get(0).objectId();
            // return new WatchlistIdResponse(objectId);
            // }

            return apiResponse;

        } catch (Exception e) {
            // In a production app, log this cleanly or throw a custom exception
            return new Back4AppWatchlistsResponse(List.of());
        }
    }

    public Back4AppWatchlistsResponse getAllEnabledWatchlists() {
        try {

            // 3. Execute the GET request using RestClient
            Back4AppWatchlistsResponse apiResponse = restClient.get()
                    .uri("https://parseapi.back4app.com/classes/" + ENABLEDWATCHLIST_TABLE)
                    .header("X-Parse-Application-Id", BACK_4_APP_APPLICATION_ID)
                    .header("X-Parse-REST-API-Key", BACK_4_APP_REST_API_KEY)
                    .retrieve()
                    .body(Back4AppWatchlistsResponse.class);

            // 4. Extract the objectId safely
            // if (apiResponse != null && apiResponse.results() != null &&
            // !apiResponse.results().isEmpty()) {
            // String objectId = apiResponse.results().get(0).objectId();
            // return new WatchlistIdResponse(objectId);
            // }

            return apiResponse;

        } catch (Exception e) {
            // In a production app, log this cleanly or throw a custom exception
            return new Back4AppWatchlistsResponse(List.of());
        }
    }

    private static Map<String, Object> buildWatchlistBody(WatchlistRecord watchlist) {
        List<Asset> assets = watchlist.assets();
        Map<String, Object> theBody = new HashMap<>();
        theBody.put("name", watchlist.name());
        theBody.put("enabled", false);
        if (assets == null) {
            assets = List.of();
            theBody.put("assets", assets);
        } else {
            theBody.put("assets", watchlist.assets());
        }
        return theBody;
    }

    List<Back4AppWatchlist> parseListResponse(String response) {
        try {
            return objectMapper.readValue(response, new TypeReference<List<Back4AppWatchlist>>() {
            });
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return List.of();
        }
    }

    Back4AppWatchlistsResponse parseResponse(String response) {
        try {
            return objectMapper.readValue(response, Back4AppWatchlistsResponse.class);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            System.out.println(response);
            e.printStackTrace();
            throw new NullPointerException();
        }
    }

}