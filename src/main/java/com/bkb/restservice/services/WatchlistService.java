package com.bkb.restservice.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bkb.restservice.models.Watchlist;
import com.bkb.restservice.repositories.WatchlistRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository; 
    @Autowired
    private ObjectMapper objectMapper;

    public List<Watchlist> getAllWatchlists() {
        try {
            return Arrays.asList(watchlistRepository.getAllWatchlists());
        }

        catch(JsonProcessingException e) {
            System.out.println("Unable to parse watchlists");
            System.out.println(e);
        }
        return List.of();
    }

    public Watchlist getWatchlistById(String id) {
        try {
            return watchlistRepository.getWatchlistById(id);
        }

        catch(JsonProcessingException e) {
            System.out.println("Unable to get watchlist with that id");
            System.out.println(e);
        }
        
        return new Watchlist(null, null, null, null);
    }

    public String postWatchlist(String entity) {
        try {

            Watchlist watchlist = objectMapper.readValue(entity, Watchlist.class);
            ResponseEntity<Void> response = watchlistRepository.postWatchlist(watchlist);
            // System.out.println(response);
            Boolean isOk = response.getStatusCode() == HttpStatusCode.valueOf(201);

            if(isOk) {
                return watchlist.id();
            }
            return response.getStatusCode().toString();
        }

        catch(JsonProcessingException e){
            System.out.println("Unable to parse watchlist " + entity);
            System.out.println(e);

            return "failed";
        }
    }

    // chain on getWatchlistById, inside the forEach 
    public void postAllWatchlists() {
        watchlistRepository.getAllWatchlistsFuture().thenAccept((watchlists -> {
            Arrays.stream(watchlists).forEach(watchlist -> {
                // System.out.println("Watchlist" + watchlist);
                
                watchlistRepository.getWatchlistByIdFuture(watchlist.id()).thenAccept(watchlistWithAssets -> {
                    System.out.println("Watchlist with assets" + watchlistWithAssets);
            
                    watchlistRepository.postWatchlistFuture(watchlistWithAssets).thenAccept(response -> {
                         System.out.println("Successfully posted watchlist: " + watchlistWithAssets.id());
                    });
                });
            });
        }));        
    }
}
