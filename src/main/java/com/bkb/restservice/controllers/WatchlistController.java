package com.bkb.restservice.controllers;

import java.util.List;
import java.util.concurrent.CompletionStage;

import com.bkb.restservice.models.Watchlist.WatchlistRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bkb.restservice.models.Back4AppWatchlist;
import com.bkb.restservice.services.WatchlistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("watchlists/enabled")
    public CompletionStage<List<WatchlistRecord>> getWatchlistSubset() {
        return watchlistService.getEnabledWatchlists();
    }

    @GetMapping("watchlists/all")
    public CompletionStage<List<WatchlistRecord>> getAllWatchlists() {
        return watchlistService.getAllWatchlists();
    }
    
    @PostMapping("watchlists/all")
    public CompletionStage<Void> postWatchlists() {
        return watchlistService.postAllWatchlists();
    }

    @GetMapping("watchlists/accountId")
    public CompletionStage<List<WatchlistRecord>> getWatchlistsByAccountId(
            @RequestParam(value = "accountId") String accountId) {
        return watchlistService.getWatchlistsByAccountId(accountId);
    }

    @PostMapping("watchlist")
    public CompletionStage<Void> postWatchlist(@RequestBody String entity) {
        final WatchlistRecord watchlist = buildWatchlistRequestEntity(entity);
        return watchlistService.postWatchlist(watchlist);
    }

    @GetMapping("watchlist/getWatchlistIdByName")
    public CompletionStage<String> getWatchlistByName(@RequestParam(value = "name") String watchlistName) {
        return watchlistService.getWatchlistIdByName(watchlistName);
    }

    @PutMapping("watchlist/enable")
    public CompletionStage<Void> enableWatchlist(@RequestParam(value = "watchlistName") String watchlistName) {
        return watchlistService.enableWatchlist(watchlistName);
    }

    @DeleteMapping("watchlist/disable")
    public CompletionStage<Void> disableWatchlist(@RequestParam(value = "watchlistName") String watchlistName) {
        return watchlistService.disableWatchlist(watchlistName);
    }

    @GetMapping("watchlist/back4app/name")
    public CompletionStage<Back4AppWatchlist> getWatchlistObjectId(
            @RequestParam(value = "watchlistName") String watchlistName) {
        return watchlistService.getWatchlistObjectId(watchlistName);
    }

    private WatchlistRecord buildWatchlistRequestEntity(String entity) {
        try {
            return objectMapper.readValue(entity, WatchlistRecord.class);
        } catch (JsonProcessingException e) {
            System.out.println("Unable to parse watchlist " + entity);
            System.out.println(e);

            throw new RuntimeException("Unable to parse watchlist");
        }
    }
}