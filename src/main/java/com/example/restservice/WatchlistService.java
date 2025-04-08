package com.example.restservice;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;

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
}
