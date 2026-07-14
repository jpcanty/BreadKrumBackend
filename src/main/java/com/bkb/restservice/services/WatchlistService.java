package com.bkb.restservice.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.bkb.restservice.models.Watchlist.WatchlistRecord;

import com.bkb.restservice.models.WatchlistObjectId;
import com.bkb.restservice.repositories.SQLiteWatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bkb.restservice.clients.Back4AppClient;

import java.util.stream.Collectors;

@Service
public class WatchlistService {
    @Autowired
    private Back4AppClient back4AppClient;
    @Autowired
    private SQLiteWatchlistRepository watchlistRepository;
    @Autowired
    private StockPriceService stockPriceService;

    public CompletionStage<String> getWatchlistObjectId(String watchlistName) {
        return back4AppClient.getWatchlistObjectId(watchlistName);
    }

    public CompletionStage<List<WatchlistRecord>> getAllWatchlists() {
//        return watchlistRepository.getAllWatchlists().thenCompose(this::hydrate);
        CompletionStage<WatchlistRecord[]> allWatchlistsFuture = watchlistRepository.getAllWatchlists();
        CompletionStage<List<String>> enabledWatchlistNamesFuture = watchlistRepository.getEnabledWatchlists();

        //TODO see how this would look with a thenCompose
        return allWatchlistsFuture.thenCombine(enabledWatchlistNamesFuture, (allWatchlists, enabledNames) ->{
            List<WatchlistRecord> updateWatchlists = Arrays.stream(allWatchlists)
                    .map(watchlist -> {
                        boolean isEnabled = enabledNames.contains(watchlist.name());
                        return new WatchlistRecord(
                                watchlist.id(),
                                watchlist.name(),
                                watchlist.account_id(),
                                watchlist.assets(),
                                isEnabled
                        );
                    })
                    .collect(Collectors.toList());
            return updateWatchlists.toArray(new WatchlistRecord[0]);
        } ). thenCompose( this::hydrate);
    }

    public CompletionStage<List<WatchlistRecord>> getWatchlistsByName(List<String> params) {

        return getAllWatchlists().thenApply(watchlists -> {
            return watchlists.stream()
                    .filter(watchlist -> { 
                       boolean hasName =  params.contains(watchlist.name());  
                       boolean hasID = params.contains(watchlist.id());  
                       boolean hasAssetSymbolOrName = watchlist.assets().stream().filter(asset -> params.contains(asset.symbol() ) || params.contains( asset.name())).findAny().isPresent();
                       System.out.println(params); 
                       return hasName || hasID || hasAssetSymbolOrName; 
                    //    add logic for the asset name -- as along as the substring exists in the name.
                    }) 
                    .toList();
        });
    }

    public CompletionStage<List<WatchlistRecord>> getWatchlistByEnabled(){

        CompletionStage<WatchlistRecord[]> allWatchlistsFuture = watchlistRepository.getAllWatchlists();
        CompletionStage<List<String>> enabledWatchlistNamesFuture = watchlistRepository.getEnabledWatchlists();

        //TODO see how this would look with a thenCompose
        return allWatchlistsFuture.thenCombine(enabledWatchlistNamesFuture, (allWatchlists, enabledNames) ->{
            List<WatchlistRecord> updateWatchlists = Arrays.stream(allWatchlists)
                    .map(watchlist -> {
                        boolean isEnabled = enabledNames.contains(watchlist.name());
                        return new WatchlistRecord(
                                watchlist.id(),
                                watchlist.name(),
                                watchlist.account_id(),
                                watchlist.assets(),
                                isEnabled
                        );
                    })
                    .filter(watchlist -> {
                        boolean isEnabled =  watchlist.enabled();
                        return isEnabled;
                    })
                    .collect(Collectors.toList());
            return updateWatchlists.toArray(new WatchlistRecord[0]);
        } ). thenCompose( this::hydrate);
    }

    private CompletionStage<List<WatchlistRecord>> hydrate(WatchlistRecord[] watchlists) {
        List<CompletionStage<WatchlistRecord>> futures = Arrays.stream(watchlists)
                .map(watchlist -> watchlistRepository.getWatchlistById(watchlist.id())
                        .thenApply(watchlistWithAssets -> stockPriceService.hydrateStockPrice(watchlistWithAssets)))
                .toList();

        // Combine all futures into one CompletionStage<List<Watchlist>>
        return CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletionStage::toCompletableFuture)
                        .map(CompletableFuture::join)
                        .toList());
    }

    public CompletionStage<WatchlistRecord> getWatchlistById(String id) {
        return watchlistRepository.getWatchlistById(id);
    }

    public CompletionStage<String> postWatchlist(WatchlistRecord watchlist) {
        return watchlistRepository.postWatchlist(watchlist);

    }

//    goes in here and then moves to 128
    public CompletionStage<Void> postAllWatchlists() {
        //the .getAllWatchlists() is where our logic that connects to the rremote db lives
        return watchlistRepository.getAllWatchlists().thenCompose(this::postAllWatchlists);
    }

//    this is a method that will show us our db (July 14,2026)
    private CompletionStage<Void> postAllWatchlists(WatchlistRecord[] watchlists) {
        return CompletableFuture.completedFuture(null);
//        List<CompletionStage<String>> futures = Arrays.stream(watchlists)
//                // Get all watchlist assets
//                .map(watchlist -> watchlistRepository.getWatchlistById(watchlist.id())
//                        .thenApply(stockPriceService::hydrateStockPrice)
//                        .thenCompose(watchlistWithAssets -> watchlistRepository.postWatchlist(watchlistWithAssets)))
//                .toList();
//
//        // Combine list of futures into one future
//        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    public CompletionStage<Void> enableWatchlist(String watchlistName) {
        // We call the back4app client to add watchlistName to EnabledWatchlists table
        return back4AppClient.enableWatchlist(watchlistName).thenAccept(response -> {});
    }
    //
    public CompletionStage<Void> disableWatchlist(String watchlistName) {
        //String objectId = back4AppClient.getObjectId(watchlistName) <- EnabledWatchlists table
        String objectID = getWatchlistIdByName(watchlistName);
        // back4appClient.disableWatchlist(objectId)
       return back4AppClient.deleteWatchlist(objectID).thenAccept(response ->{});

    }
    public String getWatchlistIdByName(String watchlistName){
        //watchlist response is coming from back4appclient
       Optional<WatchlistObjectId> watchlistObjectId = back4AppClient.getAllEnabledWatchlists().results().stream().filter(watchlist->{
            String name = watchlist.name();
            return name.equals(watchlistName);
        }).findAny();
        String objectId = watchlistObjectId.orElseThrow().objectId();
       // 1. stream results filter on watch list name

        // 2. findAny -> Optional<WatchlistObjectId>
        // 3. Get objectId
        // change signature to return String
        return objectId;
    }
}
