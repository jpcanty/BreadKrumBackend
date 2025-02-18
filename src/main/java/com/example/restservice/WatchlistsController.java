package com.example.restservice;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
}
