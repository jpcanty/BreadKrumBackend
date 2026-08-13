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

//    this one is connected to line 31 in the SQLiteWatchlist Repository file
    @GetMapping("watchlist/enabled")
    public CompletionStage<List<WatchlistRecord>> getWatchlistSubset() {
        return watchlistService.getEnabledWatchlists();
    }

    // this is connected to line 18 in the SQLiteWatchlist Repository file
    @PostMapping("/watchlist")
    public CompletionStage<String> postWatchlist(@RequestBody String entity) {
        final WatchlistRecord watchlist = buildWatchlistRequestEntity(entity);
        return watchlistService.postWatchlist(watchlist);
    }
    //for the getAllWatchlists() on sqliteWatchlistRepository
    @GetMapping("/watchlist")
    public CompletionStage<List<WatchlistRecord>>getAllWatchlists(){
        return watchlistService.getAllWatchlists();
        //follow the paths from here till you get to sqlite use the post watchlist as an example. 
    }

    @PostMapping("/watchlist/all")
    public CompletionStage<Void> postWatchlists() {
        return watchlistService.postAllWatchlists();
    }

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

    @GetMapping("/watchlist/alpaca/search")
    public CompletionStage<List<AlpacaWatchlist>> get10Watchlists(@RequestParam(value = "params") List<String> params) {
        if (params.size() > 10) {
            throw new NullPointerException();
        }
        return watchlistService.getAlpacaWatchlistsByParam(params);
    }

    @GetMapping("/watchlist/back4app/name")
    public CompletionStage<Back4AppWatchlist> getWatchlistObjectId(@RequestParam(value = "watchlistName") String watchlistName) {
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

    @GetMapping("/stockpriceSymbol")
    public CompletionStage<StockPrice> getStockPriceHistoryBySymbol(
            @RequestParam(value = "symbol", defaultValue = "") String symbol,
            @RequestParam(value = "timeframe", defaultValue = "") String timeframe) {
        return null;
        // return watchlistService.getStockPriceHistoryBySymbol(symbol, timeframe);
    }

    //this will be connected to line 31 in sqlitewatchlistRepository.java file
    @GetMapping("/watchlist/getWatchlistByIdName")
    public String getWatchlistByIdName(@RequestParam(value = "name") String watchlistName) {
        return watchlistService.getWatchlistIdByName(watchlistName).toString();
    }
    //this is the one we worked on last meeting.
    @GetMapping("watchlists/accountId")
    public CompletionStage<List<WatchlistRecord>> getWatchlistsByAccountId( @RequestParam(value = "accountId") String accountId) {
        return watchlistService.getWatchlistByAccountId(accountId);
    }

    @PutMapping("/watchlist/enable")
    public CompletionStage<Void> enableWatchlist(@RequestParam(value = "watchlistName") String watchlistName) {
        return watchlistService.enableWatchlist(watchlistName);
    }

    @DeleteMapping("watchlist/disable")
    public CompletionStage<Void> disableWatchlist(@RequestParam(value = "watchlistName") String watchlistName) {
        return watchlistService.disableWatchlist(watchlistName);
    }
    

}