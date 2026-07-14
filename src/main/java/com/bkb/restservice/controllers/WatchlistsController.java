package com.bkb.restservice.controllers;

import java.util.List;
import java.util.concurrent.CompletionStage;

import com.bkb.restservice.models.Watchlist.WatchlistRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bkb.restservice.models.StockPrice;
import com.bkb.restservice.services.WatchlistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// Controller is the entry into the backend service 
@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class WatchlistsController {

    @Autowired
    private WatchlistService watchlistService;
    @Autowired
    private ObjectMapper objectMapper;

    @PutMapping("/watchlist/enable")
    public CompletionStage<Void> enableWatchlist(@RequestParam(value = "watchlistName") String watchlistName) {
        return watchlistService.enableWatchlist(watchlistName);
    }

    //DELETE endpoint for disable. Accepts watchlist name just like enable endpoint
    // First. Get the object id for watchlist name. Query EnabledWatchlists table with watchlist name
    // Second. Delete the row in EnabledWatchlists using the object id
    @DeleteMapping("watchlist/disable")
    public CompletionStage<Void> disableWatchlist(@RequestParam(value = "watchlistName") String watchlistName){
        return watchlistService.disableWatchlist(watchlistName);
    }

    @GetMapping("/watchlist/objectId")
    public CompletionStage<String> getWatchlistObjectId(@RequestParam(value = "watchlistName") String watchlistName) {
        return watchlistService.getWatchlistObjectId(watchlistName);
    }

    @GetMapping("/watchlists/all")
    public CompletionStage<List<WatchlistRecord>> getAllWatchlists() {
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
    public CompletionStage<List<WatchlistRecord>> get10Watchlists(@RequestParam(value = "params") List<String> params) {
        if (params.size() > 10) {
            // throw error
        }
        return watchlistService.getWatchlistsByName(params);
    }


    @GetMapping("watchlists/subset")
    public CompletionStage<List<WatchlistRecord>>  getWatchlistSubset(){

        return watchlistService.getWatchlistByEnabled();
    }


    @GetMapping("/watchlistId")
    public CompletionStage<WatchlistRecord> getWatchlistById(@RequestParam(value = "id", defaultValue = "") String id) {
        return watchlistService.getWatchlistById(id);
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
    public String getWatchlistByIdName(@RequestParam(value ="name") String watchlistName ){
        return watchlistService.getWatchlistIdByName(watchlistName);
    }

}

//"localhost:8080/watchlist/getWatchlistByIdName?name=Shoes"