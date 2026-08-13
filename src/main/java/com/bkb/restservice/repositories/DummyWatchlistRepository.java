package com.bkb.restservice.repositories;

import com.bkb.restservice.models.Watchlist;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class DummyWatchlistRepository implements WatchlistRepository{
    @Override
    public CompletionStage<String> postWatchlist(Watchlist.WatchlistRecord watchlist) {
        return null;
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
}
