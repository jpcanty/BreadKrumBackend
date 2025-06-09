package com.bkb.restservice.controllers;
import java.util.concurrent.CompletionStage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bkb.restservice.models.Watchlist;
import com.bkb.restservice.services.WatchlistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


// Controller is the entry into the backend service 
@RestController
public class WatchlistsController {

    @Autowired
    private WatchlistService watchlistService;
    @Autowired
    private ObjectMapper objectMapper;


    @GetMapping("/watchlists")
    public CompletionStage<Watchlist[]> getAllWatchlists() {
        return watchlistService.getAllWatchlists();
    }

    @GetMapping("/watchlistId")
    public CompletionStage<Watchlist> getWatchlistById(@RequestParam(value = "id", defaultValue = "") String id) {
        return watchlistService.getWatchlistById(id);
    }
    
    //posting watchlist to the frontend use  ObjectMapper objectMapper turns string to object
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
        }
        catch(JsonProcessingException e){
            System.out.println("Unable to parse watchlist " + entity);
            System.out.println(e);

            throw new RuntimeException("Unable to parse watchlist");
        }
    }

    @GetMapping("/stockpriceSymbol")
    public CompletionStage<Watchlist> getStockPriceHistoryBySymbol(
            @RequestParam(value = "symbol", defaultValue = "") String symbol,
            @RequestParam(value = "timeframe", defaultValue = "") String timeframe) {
        return watchlistService.getStockPriceHistoryBySymbol(symbol, timeframe);
    }
}
