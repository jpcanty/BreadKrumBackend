package com.bkb.restservice.repositories;

import java.util.List;
import java.util.concurrent.CompletionStage;

import com.bkb.restservice.models.Watchlist;
import com.bkb.restservice.models.Watchlist.WatchlistRecord;

public interface WatchlistRepository {
    public CompletionStage<String> postWatchlist(WatchlistRecord watchlist);
    
    public List<Watchlist> getAllWatchlists();

    public CompletionStage<WatchlistRecord> getWatchlistByIdName(String name);

    public List<Watchlist> getWatchlistsByAccountId(String accountId);

    public List<Watchlist> getEnabledWatchlists();
}
