package com.example.restservice.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.models.Watchlist;
import com.example.restservice.services.WatchlistService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


// Controller is the entry into the backend service 
@RestController
public class WatchlistsController {

    @Autowired
    private final WatchlistService watchlistService;

    public WatchlistsController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping("/watchlists")
    public List<Watchlist> getAllWatchlists() {
        return watchlistService.getAllWatchlists();
    }

    @GetMapping("/watchlistId")
    public Watchlist getWatchlistById(@RequestParam(value = "id", defaultValue = "") String id) {
        return watchlistService.getWatchlistById(id);
    }
    
    //posting watchlist to the frontend use  ObjectMapper objectMapper turns string to object
    @PostMapping("/watchlist")
    public String postWatchlist(@RequestBody String entity) {
        return watchlistService.postWatchlist(entity);
    }
    
    @PostMapping("/watchlists")
    public void postWatchlists() {
        watchlistService.postAllWatchlists();
    }
}
