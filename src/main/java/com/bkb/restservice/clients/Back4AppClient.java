package com.bkb.restservice.clients;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.bkb.restservice.models.WatchlistsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.bkb.restservice.models.Asset;
import com.bkb.restservice.models.Watchlist;

@Component
public class Back4AppClient {
    @Autowired
    private RestClient restClient;
    private static final String BACK_4_APP_APPLICATION_ID = "19FwHl4gMBw72qM8OHaGyCgm038AjHznnh4E5Wmz";
    private static final String BACK_4_APP_REST_API_KEY = "URGqznPEwDMGaPmkyCJMbx7SCtvCOfDWPlvp26gF";
    @Autowired
    private ObjectMapper objectMapper;

    public CompletionStage<ResponseEntity<Void>> postWatchlistFuture(Watchlist watchlist) {
        return CompletableFuture.supplyAsync(() -> {
            String tableName = "Watchlist3";
            return restClient.post()
                    .uri("https://parseapi.back4app.com/classes/" + tableName)
                    .header("X-Parse-Application-Id", BACK_4_APP_APPLICATION_ID)
                    .header("X-Parse-REST-API-Key", BACK_4_APP_REST_API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body( buildWatchlistBody(watchlist) )
                    .retrieve()
                    .toBodilessEntity();
        });
    }
// Add the enabled column to the body
        // Create a private method called buildWatchlistBody(watchlist)

        // include
            //         List<Asset> assets = watchlist.assets();
            // if (assets == null) {
            //     assets = List.of();
            // }

        // .body(buildWatchlistBody(watchlist))
        private Map<String, Object> buildWatchlistBody(Watchlist watchlist){

            List<Asset> assets = watchlist.assets();
            Map<String,Object> theBody = new HashMap<>();
            theBody.put("name", watchlist.name());
            theBody.put("enabled" , false);
            if (assets == null) {
                assets = List.of();
                theBody.put("assets", assets );
            }
            else{
                theBody.put("assets", watchlist.assets() );
            }
            return theBody;
            // if .assets() != null
            // {"name":"blah","assets":["UBER"], "enabled":true}

            // if .assets() == null
            // current: {}
            // desired: {"name":"blah","assets":[], "enabled":true}
            // breaks: {"name":"blah","assets":null, "enabled":true}
        }

        public CompletionStage<String> getEnabledWatchlistsFuture(){
            return CompletableFuture.supplyAsync( () -> {
                String enabledWatchlistsClass = "EnabledWatchlists";  // this should be the name of the Back4App class
                return restClient.get() // GET request
                        .uri("https://parseapi.back4app.com/classes/" + enabledWatchlistsClass)
                        .header( "X-Parse-Application-Id" , BACK_4_APP_APPLICATION_ID)
                        .header( "X-Parse-REST-API-Key", BACK_4_APP_REST_API_KEY)
                        .retrieve() //execute the request
                        .body(String.class); // the response is in a String
            });
        }

        public CompletionStage<String> getWatchlistObjectId(String watchlistName) {
            return CompletableFuture.supplyAsync(() -> {
                String watchList = "Watchlist3";
                String where = "{\"name\":\"" + watchlistName + "\"}";
                return restClient.get()
                        .uri("https.parseapi.back4app.com/classes/" + watchList + "?where=" + where)
                        .header("X-Parse-Application-Id", BACK_4_APP_APPLICATION_ID)
                        .header("X-Parse-REST-API-Key", BACK_4_APP_REST_API_KEY)
                        .retrieve()
                        .body(String.class);
            });
        }

        // We want to write a row to a table with a POST
        // Name of the table: EnabledWatchlists
        // {"name":"Shoes"}

        public CompletionStage<ResponseEntity<Void>> enableWatchlist (String watchlistName){

            return CompletableFuture.supplyAsync(() -> {
                String tableName = "EnabledWatchlists";
                return restClient.post()
                        .uri("https://parseapi.back4app.com/classes/" + tableName)
                        .header("X-Parse-Application-Id", BACK_4_APP_APPLICATION_ID)
                        .header("X-Parse-REST-API-Key", BACK_4_APP_REST_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Map.of("name", watchlistName))
                        .retrieve()
                        .toBodilessEntity();
            });
        }

        public CompletionStage<ResponseEntity<Void>> disableWatchlist(String watchlistName){
            //getWAtchlistByName <----
            // delete watchlist with object id
            return null;
        }
        public WatchlistsResponse getAllWatchlists() {
            try {


                // 3. Execute the GET request using RestClient
                WatchlistsResponse apiResponse = restClient.get()
                        .uri("https://parseapi.back4app.com/classes/" + "Watchlist3")
                        .header("X-Parse-Application-Id", BACK_4_APP_APPLICATION_ID)
                        .header("X-Parse-REST-API-Key", BACK_4_APP_REST_API_KEY)
                        .retrieve()
                        .body(WatchlistsResponse.class);

                // 4. Extract the objectId safely
//                if (apiResponse != null && apiResponse.results() != null && !apiResponse.results().isEmpty()) {
//                    String objectId = apiResponse.results().get(0).objectId();
//                    return new WatchlistIdResponse(objectId);
//                }

                return apiResponse;

            } catch (Exception e) {
                // In a production app, log this cleanly or throw a custom exception
                return new WatchlistsResponse(List.of());
            }
        }


}
