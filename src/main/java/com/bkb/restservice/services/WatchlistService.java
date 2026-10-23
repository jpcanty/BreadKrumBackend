package com.bkb.restservice.services;

import java.util.List;
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
import org.springframework.stereotype.Service;

import com.bkb.restservice.ApplicationConfiguration;
import com.bkb.restservice.clients.AlpacaClient;
import com.bkb.restservice.clients.Back4AppClient;

@Service
public class WatchlistService {
    @Autowired
    private AlpacaClient alpacaClient;
    @Autowired
    private Back4AppClient back4AppClient;
    @Autowired
    @Qualifier(ApplicationConfiguration.SQLITE_RESPOSITORY)
    private WatchlistRepository watchlistRepository;

    public CompletionStage<List<WatchlistRecord>> getEnabledWatchlists() {
        return CompletableFuture.supplyAsync(() -> watchlistRepository.getEnabledWatchlists())
                .thenApply(WatchlistService::toRecords);
    }

    public CompletionStage<List<WatchlistRecord>> getAllWatchlists() {
        return CompletableFuture.supplyAsync(() -> watchlistRepository.getAllWatchlists())
                .thenApply(WatchlistService::toRecords);

    }

    public CompletionStage<Void> postWatchlist(WatchlistRecord watchlist) {
        watchlistRepository.insertWatchlist(watchlist.id(), watchlist.name(), watchlist.accountId(),
                watchlist.enabled(), Watchlist.cleanTime(watchlist.createdAt()),
                Watchlist.cleanTime(watchlist.updatedAt()));
        return CompletableFuture.completedFuture(null);
    }

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

    public CompletionStage<Void> postAllWatchlists() {
        return getAllAlpacaWatchlists().thenCompose(watchlists -> {
            List<CompletionStage<Void>> futures = watchlists.stream()
                    .map(Watchlist::toRecord)
                    .map(this::postWatchlist).toList();

            return CompletableFuture
                    .allOf(futures.toArray(new CompletableFuture[0]));
        });
    }

    public CompletionStage<Void> enableWatchlist(String watchlistName) {
        watchlistRepository.enableWatchlist(watchlistName);
        return CompletableFuture.completedFuture(null).thenAccept(response -> {
        });
    }

    public CompletionStage<Void> disableWatchlist(String watchlistName) {
        watchlistRepository.disableWatchlist(watchlistName);
        return CompletableFuture.completedFuture(null).thenAccept(response -> {
        });
    }

    public CompletionStage<String> getWatchlistIdByName(String watchlistName) {
        return CompletableFuture.supplyAsync(() -> watchlistRepository.getWatchlistsByIdName(watchlistName))
                .thenApply(watchlists -> watchlists.stream().findAny().map(Watchlist::toRecord).map(WatchlistRecord::id)
                        .orElse("NOT_FOUND"));
    }

    public CompletionStage<List<WatchlistRecord>> getWatchlistsByAccountId(String accountId) {
        return CompletableFuture.completedFuture(watchlistRepository.getWatchlistsByAccountId(accountId))
                .thenApply(WatchlistService::toRecords);
    }

    private static List<WatchlistRecord> toRecords(List<Watchlist> watchlists) {
        return watchlists.stream().map(Watchlist::toRecord).toList();
    }
}
