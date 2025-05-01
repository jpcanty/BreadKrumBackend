package com.example.restservice.services;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.restservice.models.Watchlist;
import com.example.restservice.repositories.WatchlistRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository; 
    final ObjectMapper mapper = new ObjectMapper();

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

            Watchlist watchlist = mapper.readValue(entity, Watchlist.class);
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
                // System.out.println(watchlist.id());
                watchlistRepository.postWatchlistFuture(watchlist);
                // watchlistRepository.getWatchlistByIdFuture(watchlist.id());
            });
        }));
        
    }
}
