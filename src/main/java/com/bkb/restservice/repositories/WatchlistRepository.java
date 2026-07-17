package com.bkb.restservice.repositories;

import java.util.List;
import java.util.concurrent.CompletionStage;

import com.bkb.restservice.models.Watchlist;
import com.bkb.restservice.models.Watchlist.WatchlistRecord;

public interface WatchlistRepository {
    public CompletionStage<String> postWatchlist(WatchlistRecord watchlist);
    
    public List<Watchlist> getAllWatchlists();

    public CompletionStage<WatchlistRecord> getWatchlistById(String id);

    public List<Watchlist> getWatchlistsByAccountId(String accountId);

    public CompletionStage<List<String>> getEnabledWatchlists();
}
