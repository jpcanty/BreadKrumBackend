package com.bkb.restservice.repositories;

import com.bkb.restservice.models.Watchlist;
import com.bkb.restservice.models.Watchlist.WatchlistRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DummyWatchlistRepository implements WatchlistRepository {

    List<Watchlist> watchlists = new ArrayList<>();

    @Override
    public void insertWatchlist(String id, String name, String accountId, boolean enabled, String createdAt,
            String updatedAt) {
        watchlists.add(new Watchlist());
    }

    @Override
    public List<Watchlist> getAllWatchlists() {
        return watchlists;
    }

    @Override
    public List<Watchlist> getWatchlistsByIdName(String name) {
        return List.of();
    }

    @Override
    public List<Watchlist> getWatchlistsByAccountId(String accountId) {
        return watchlists;
    }

    @Override
    public List<Watchlist> getEnabledWatchlists() {
        return watchlists;
    }

    @Override
    public void enableWatchlist(String name) {
    }

    @Override 
    public void disableWatchlist(String name) {
    };
}
