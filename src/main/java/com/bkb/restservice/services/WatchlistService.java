package com.bkb.restservice.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.bkb.restservice.models.Watchlist;
import com.bkb.restservice.models.Watchlist.WatchlistRecord;

import com.bkb.restservice.models.AlpacaWatchlist;
import com.bkb.restservice.models.AlpacaWatchlistWithAssets;
import com.bkb.restservice.models.Back4AppWatchlist;
import com.bkb.restservice.repositories.WatchlistRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.bkb.restservice.ApplicationConfiguration;
import com.bkb.restservice.clients.AlpacaClient;
import com.bkb.restservice.clients.Back4AppClient;

import java.util.stream.Collectors;

@Service
public class WatchlistService {
    @Autowired
    private AlpacaClient alpacaClient;
    @Autowired
    private Back4AppClient back4AppClient;
    @Autowired
    @Qualifier(ApplicationConfiguration.SQLITE_RESPOSITORY)
    private WatchlistRepository watchlistRepository;
    @Autowired
    private StockPriceService stockPriceService;

    public CompletionStage<List<AlpacaWatchlist>> getAllAlpacaWatchlists() {
        return alpacaClient.getAllWatchlistsFuture();
    }

    public CompletionStage<AlpacaWatchlistWithAssets> getAlpacaWatchlistById(String id) {
        return alpacaClient.getWatchlistByIdFuture(id);
    }

    public CompletionStage<Back4AppWatchlist> getWatchlistObjectId(String watchlistName) {
        return back4AppClient.getWatchlistObjectId(watchlistName).thenApply(response -> {
            return response.results().stream().findAny().orElseThrow();
        });
    }

    public CompletionStage<List<AlpacaWatchlist>> getAlpacaWatchlistsByParam(List<String> params) {
        return getAllAlpacaWatchlists().thenApply(watchlists -> {
            return watchlists.stream()
                    .filter(watchlist -> {
                        boolean hasName = params.contains(watchlist.name());
                        boolean hasID = params.contains(watchlist.id());
                        return hasName || hasID;
                    })
                    .toList();
        });
    }

    public CompletionStage<List<WatchlistRecord>> getEnabledWatchlists() {
        return CompletableFuture.supplyAsync(() -> watchlistRepository.getEnabledWatchlists())
                .thenApply(WatchlistService::toRecords);
    }

    public CompletionStage<List<WatchlistRecord>> getAllWatchlists() {
        return CompletableFuture.supplyAsync(() -> watchlistRepository.getAllWatchlists())
                .thenApply(WatchlistService::toRecords);

    }

    private static List<WatchlistRecord> toRecords(List<Watchlist> watchlists) {
        return watchlists.stream().map(Watchlist::toRecord).toList();
    }

    public CompletionStage<String> postWatchlist(WatchlistRecord watchlist) {
        watchlistRepository.insertWatchlist(watchlist.id(), watchlist.name(), watchlist.accountId(),
                watchlist.enabled(), watchlist.createdAt(), watchlist.updatedAt());
        return CompletableFuture.completedFuture("");

    }

    public CompletionStage<Void> postAllWatchlists() {
        return getAllAlpacaWatchlists().thenCompose(watchlists -> {
            List<CompletionStage<String>> futures = watchlists.stream()
                    .map(Watchlist::toRecord)
                    .map(this::postWatchlist).toList();

            return CompletableFuture
                    .allOf(futures.toArray(new CompletableFuture[0]));

        });

    }

    private CompletionStage<Void> postAllWatchlists(List<Watchlist> watchlists) {
        return CompletableFuture.completedFuture(null);
        // List<CompletionStage<String>> futures = Arrays.stream(watchlists)
        // // Get all watchlist assets
        // .map(watchlist -> watchlistRepository.getWatchlistById(watchlist.id())
        // .thenApply(stockPriceService::hydrateStockPrice)
        // .thenCompose(watchlistWithAssets ->
        // watchlistRepository.postWatchlist(watchlistWithAssets)))
        // .toList();
        //
        // // Combine list of futures into one future
        // return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    public CompletionStage<Void> enableWatchlist(String watchlistName) {
        watchlistRepository.enableWatchlist(watchlistName);
        return CompletableFuture.completedFuture(null).thenAccept(response -> {
        });
        // return back4AppClient.enableWatchlist(watchlistName).thenAccept(response -> {
        // });
    }

    public CompletionStage<Void> disableWatchlist(String watchlistName) {

        watchlistRepository.disableWatchlist(watchlistName);
        return CompletableFuture.completedFuture(null).thenAccept(response -> {
        });

    }

    public CompletionStage<String> getWatchlistIdByName(String watchlistName) {
        // watchlist response is coming from back4appclient
        // Optional<Back4AppWatchlist> watchlistObjectId =
        // back4AppClient.getAllEnabledWatchlists().results().stream()
        // .filter(watchlist -> {
        // String name = watchlist.name();
        // return name.equals(watchlistName);
        // }).findAny();
        // String objectId = watchlistObjectId.orElseThrow().objectId();
        //
        // return objectId;
        return CompletableFuture.supplyAsync(() -> watchlistRepository.getWatchlistsByIdName(watchlistName))
                .thenApply(watchlists -> watchlists.stream().findAny().map(Watchlist::toRecord).map(WatchlistRecord::id)
                        .orElse("NOT_FOUND"));
    }

    public CompletionStage<List<WatchlistRecord>> getWatchlistByAccountId(String accountId) {
        return CompletableFuture.completedFuture(watchlistRepository.getWatchlistsByAccountId(accountId))
                .thenApply(watchlist -> {
                    return watchlist.stream().map(Watchlist::toRecord).toList();
                });
    }
}
