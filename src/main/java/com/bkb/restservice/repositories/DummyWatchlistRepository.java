package com.bkb.restservice.repositories;

import com.bkb.restservice.models.Watchlist;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class DummyWatchlistRepository implements WatchlistRepository{
    @Override
    public void insertWatchlist(String id ,String name,  String accountId, boolean enabled, String createdAt, String updatedAt) {
    }

    @Override
    public List<Watchlist> getAllWatchlists() {
        return List.of();
    }

    @Override
    public CompletionStage<Watchlist.WatchlistRecord> getWatchlistByIdName(String name) {
        return null;
    }

    @Override
    public List<Watchlist> getWatchlistsByAccountId(String accountId) {
        return List.of();
    }

    @Override
    public List<Watchlist> getEnabledWatchlists() {
        return List.of();
    }

    @Override
    public void enableWatchlist(String name) {
    }
    public void disableWatchlist(String name){};
}
