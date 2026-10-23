package com.bkb.restservice.controllers;

import java.util.List;
import java.util.concurrent.CompletionStage;

import com.bkb.restservice.models.Watchlist.WatchlistRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bkb.restservice.models.AlpacaWatchlist;
import com.bkb.restservice.models.AlpacaWatchlistWithAssets;
import com.bkb.restservice.models.Back4AppWatchlist;
import com.bkb.restservice.models.StockPrice;
import com.bkb.restservice.services.StockPriceService;
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
    private StockPriceService stockPriceService;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("watchlist")
    public CompletionStage<Void> postWatchlist(@RequestBody String entity) {
        final WatchlistRecord watchlist = buildWatchlistRequestEntity(entity);
        return watchlistService.postWatchlist(watchlist);
    }

    @GetMapping("watchlist/enabled")
    public CompletionStage<List<WatchlistRecord>> getWatchlistSubset() {
        return watchlistService.getEnabledWatchlists();
    }

    @GetMapping("watchlist/all")
    public CompletionStage<List<WatchlistRecord>> getAllWatchlists() {
        return watchlistService.getAllWatchlists();
    }
    
    @PostMapping("watchlist/all")
    public CompletionStage<Void> postWatchlists() {
        return watchlistService.postAllWatchlists();
    }

    @GetMapping("watchlist/getWatchlistIdByName")
    public CompletionStage<String> getWatchlistByName(@RequestParam(value = "name") String watchlistName) {
        return watchlistService.getWatchlistIdByName(watchlistName);
    }

    @GetMapping("watchlists/accountId")
    public CompletionStage<List<WatchlistRecord>> getWatchlistsByAccountId(
            @RequestParam(value = "accountId") String accountId) {
        return watchlistService.getWatchlistsByAccountId(accountId);
    }

    @PutMapping("watchlist/enable")
    public CompletionStage<Void> enableWatchlist(@RequestParam(value = "watchlistName") String watchlistName) {
        return watchlistService.enableWatchlist(watchlistName);
    }

    @DeleteMapping("watchlist/disable")
    public CompletionStage<Void> disableWatchlist(@RequestParam(value = "watchlistName") String watchlistName) {
        return watchlistService.disableWatchlist(watchlistName);
    }

    @GetMapping("watchlist/alpaca/id")
    public CompletionStage<AlpacaWatchlistWithAssets> getWatchlistById(
            @RequestParam(value = "id", defaultValue = "") String id) {
        if (id.isBlank()) {
            throw new NullPointerException();
        }
        return watchlistService.getAlpacaWatchlistById(id);
    }

    @GetMapping("watchlist/back4app/name")
    public CompletionStage<Back4AppWatchlist> getWatchlistObjectId(
            @RequestParam(value = "watchlistName") String watchlistName) {
        return watchlistService.getWatchlistObjectId(watchlistName);
    }

    @GetMapping("stockpriceSymbol")
    public CompletionStage<StockPrice> getStockPriceHistoryBySymbol(
            @RequestParam(value = "symbol", defaultValue = "SPOT") String symbol,
            @RequestParam(value = "timeframe", defaultValue = "1D") String timeframe) {
        return stockPriceService.getStockPriceHistoryBySymbol(symbol, timeframe);
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