package com.bkb.restservice.clients;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

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


    public CompletionStage<ResponseEntity<Void>> postWatchlistFuture(Watchlist watchlist) {
        return CompletableFuture.supplyAsync(() -> {
            List<Asset> assets = watchlist.assets();
            if (assets == null) {
                assets = List.of();
            }
            String watchList = "Watchlist3";
            return restClient.post()                  
                    .uri("https://parseapi.back4app.com/classes/" + watchList)
                    .header("X-Parse-Application-Id", BACK_4_APP_APPLICATION_ID)
                    .header("X-Parse-REST-API-Key", BACK_4_APP_REST_API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("name", watchlist.name(), "assets", assets))
                    .retrieve()
                    .toBodilessEntity();
        });
    }
    
}
