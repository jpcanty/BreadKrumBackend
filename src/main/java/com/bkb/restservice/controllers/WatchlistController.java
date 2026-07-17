package com.bkb.restservice.controllers;

import java.util.List;
import java.util.concurrent.CompletionStage;

import com.bkb.restservice.models.Watchlist.WatchlistRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bkb.restservice.models.AlpacaWatchlist;
import com.bkb.restservice.models.Back4AppWatchlist;
import com.bkb.restservice.models.StockPrice;
import com.bkb.restservice.services.WatchlistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// Controller is the entry into the backend service 
@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/watchlist/alpaca/all")
    public CompletionStage<List<AlpacaWatchlist>> getAllAlpacaWatchlists() {
        return watchlistService.getAllAlpacaWatchlists();
    }

    @GetMapping("/watchlist/alpaca/id")
    public CompletionStage<AlpacaWatchlist> getWatchlistById(@RequestParam(value = "id", defaultValue = "") String id) {
        if (id.isBlank()) {
            throw new NullPointerException();
        }
        return watchlistService.getAlpacaWatchlistById(id);
    }

    @GetMapping("/watchlist/back4app/name")
    public CompletionStage<String> getWatchlistObjectId(@RequestParam(value = "watchlistName") String watchlistName) {
        return watchlistService.getWatchlistObjectId(watchlistName);
    }

    @PutMapping("/watchlist/enable")
    public CompletionStage<Void> enableWatchlist(@RequestParam(value = "watchlistName") String watchlistName) {
        return watchlistService.enableWatchlist(watchlistName);
    }

    @DeleteMapping("watchlist/disable")
    public CompletionStage<Void> disableWatchlist(@RequestParam(value = "watchlistName") String watchlistName) {
        return watchlistService.disableWatchlist(watchlistName);
    }
    

    @GetMapping("/watchlists")
    public CompletionStage<List<WatchlistRecord>> get10Watchlists(@RequestParam(value = "params") List<String> params) {
        if (params.size() > 10) {
            // throw error
        }
        return watchlistService.getWatchlistsByName(params);
    }

    @GetMapping("watchlists/subset")
    public CompletionStage<List<WatchlistRecord>> getWatchlistSubset() {
        return watchlistService.getWatchlistByEnabled();
    }

    // posting watchlist to the frontend use ObjectMapper objectMapper turns string
    // to object
    @PostMapping("/watchlist")
    public CompletionStage<String> postWatchlist(@RequestBody String entity) {
        final WatchlistRecord watchlist = buildWatchlistRequestEntity(entity);
        return watchlistService.postWatchlist(watchlist);
    }

    // Use this to test SQL lite db change this is the curl (JUly 14, 2026
    @PostMapping("/watchlists")
    public CompletionStage<Void> postWatchlists() {
        return watchlistService.postAllWatchlists();
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

    @GetMapping("/stockpriceSymbol")
    public CompletionStage<StockPrice> getStockPriceHistoryBySymbol(
            @RequestParam(value = "symbol", defaultValue = "") String symbol,
            @RequestParam(value = "timeframe", defaultValue = "") String timeframe) {
        return null;
        // return watchlistService.getStockPriceHistoryBySymbol(symbol, timeframe);
    }

    @GetMapping("/watchlist/getWatchlistByIdName")
    public String getWatchlistByIdName(@RequestParam(value = "name") String watchlistName) {
        return watchlistService.getWatchlistIdByName(watchlistName);
    }

    @GetMapping("watchlists/accountId")
    public CompletionStage<List<WatchlistRecord>> getWatchlistsByAccountId(
            @RequestParam(value = "accountId") String accountId) {
        return watchlistService.getWatchlistByAccountId(accountId);
    }

}