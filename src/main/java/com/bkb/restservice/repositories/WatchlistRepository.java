package com.bkb.restservice.repositories;

import java.util.List;
import java.util.concurrent.CompletionStage;

import com.bkb.restservice.models.Watchlist;
import com.bkb.restservice.models.Watchlist.WatchlistRecord;

public interface WatchlistRepository {
    public void insertWatchlist(String id, String name, String accountId, boolean enabled, String createdAt,
            String updatedAt);

    public List<Watchlist> getAllWatchlists();

    public CompletionStage<WatchlistRecord> getWatchlistByIdName(String name);

    public List<Watchlist> getWatchlistsByAccountId(String accountId);

    public List<Watchlist> getEnabledWatchlists();

    public void enableWatchlist(String name);

    public void disableWatchlist(String name);
}
