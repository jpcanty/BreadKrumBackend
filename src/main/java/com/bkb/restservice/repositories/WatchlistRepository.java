package com.bkb.restservice.repositories;

import java.util.List;

import com.bkb.restservice.models.Watchlist;

public interface WatchlistRepository {
    public void insertWatchlist(String id, String name, String accountId, boolean enabled, String createdAt,
            String updatedAt);

    public List<Watchlist> getAllWatchlists();

    public List<Watchlist> getWatchlistsByIdName(String name);

    public List<Watchlist> getWatchlistsByAccountId(String accountId);

    public List<Watchlist> getEnabledWatchlists();

    public void enableWatchlist(String name);

    public void disableWatchlist(String name);
}
