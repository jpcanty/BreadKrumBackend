package com.bkb.restservice.repositories;

import java.util.List;
import java.util.concurrent.CompletionStage;

import com.bkb.restservice.models.Watchlist;
import com.bkb.restservice.models.Watchlist.WatchlistRecord;
import org.springframework.data.repository.query.Param;

public interface WatchlistRepository {
    public void insertWatchlist(String id , String name,  String accountId, boolean enabled,  String createdAt, String updatedAt);
    
    public List<Watchlist> getAllWatchlists();

    public List<Watchlist> getWatchlistsByIdName(String name);

    public List<Watchlist> getWatchlistsByAccountId(String accountId);

    public List<Watchlist> getEnabledWatchlists();

    //one of the params has to be a watchlist (get string watchlist name
    public void enableWatchlist(String name);

    public void disableWatchlist(String name);
}
