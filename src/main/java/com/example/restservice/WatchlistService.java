package com.example.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;

    public String getAllWatchlists() {
        return watchlistRepository.getAllWatchlists();
    }
}
