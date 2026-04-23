package com.bkb.restservice.controllers;

import java.util.List;
import java.util.concurrent.CompletionStage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bkb.restservice.models.StockPrice;
import com.bkb.restservice.models.Watchlist;
import com.bkb.restservice.services.WatchlistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Controller is the entry into the backend service
@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class WatchlistsController {

    @Autowired
    private WatchlistService watchlistService;
    @Autowired
    private ObjectMapper objectMapper;

    @PutMapping("/watchlist/enable")
    public void enableWatchlist(@RequestParam(value = "watchlistName") String watchlistName) {
        watchlistService.enableWatchlist(watchlistName);
    }

    @GetMapping("/watchlists/all")
    public CompletionStage<List<Watchlist>> getAllWatchlists() {
        return watchlistService.getAllWatchlists();
    }

    /*
     * Create a new method at /watchlists/ that accepts a list of
     * strings as a param and limits the numbers of params to 10
     * 
     * Input: ["Semiconductor ETF (Pseudo)", "08cecd42-aafb-42ce-b8d2-ab77a539388b"]
     * 
     */
    @GetMapping("/watchlists")
    public CompletionStage<List<Watchlist>> get10Watchlists(@RequestParam(value = "params") List<String> params) {
        if (params.size() > 10) {
            // throw error
        }
        return watchlistService.getWatchlistsByName(params);
    }


    @GetMapping("watchlists/subset")
    public CompletionStage<List<Watchlist>>  getWatchlistSubset(){

        return watchlistService.getWatchlistByEnabled();
    }


    @GetMapping("/watchlistId")
    public CompletionStage<Watchlist> getWatchlistById(@RequestParam(value = "id", defaultValue = "") String id) {
        return watchlistService.getWatchlistById(id);
    }

    // posting watchlist to the frontend use ObjectMapper objectMapper turns string
    // to object
    @PostMapping("/watchlist")
    public CompletionStage<String> postWatchlist(@RequestBody String entity) {
        final Watchlist watchlist = buildWatchlistRequestEntity(entity);
        return watchlistService.postWatchlist(watchlist);
    }

    @PostMapping("/watchlists")
    public CompletionStage<Void> postWatchlists() {
        return watchlistService.postAllWatchlists();
    }

    private Watchlist buildWatchlistRequestEntity(String entity) {
        try {
            return objectMapper.readValue(entity, Watchlist.class);
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

}

