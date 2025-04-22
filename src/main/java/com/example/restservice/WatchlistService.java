package com.example.restservice;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
            System.out.println(response);
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
}
