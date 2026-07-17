package com.bkb.restservice.repositories;

import com.bkb.restservice.ApplicationConfiguration;
import com.bkb.restservice.models.Watchlist;
import com.bkb.restservice.models.Watchlist.WatchlistRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletionStage;


@Component(ApplicationConfiguration.SQLITE_RESPOSITORY)
public interface SQLiteWatchlistRepository extends WatchlistRepository, JpaRepository<Watchlist, String> {
    @Query("SELECT b FROM Watchlist b")
    public CompletionStage<String> postWatchlist(WatchlistRecord watchlist);

    @Query("SELECT b FROM Watchlist b")
    public List<Watchlist> getAllWatchlists();
        //in here we got to create the logic that outputs the watchlists in the database to our java{}

    @Query ("SELECT b FROM Watchlist b WHERE b.accountId = :accountId")
    public List<Watchlist> getWatchlistsByAccountId(@Param("accountId") String accountId);

    @Query("SELECT b FROM Watchlist b")
    public CompletionStage<WatchlistRecord> getWatchlistById(String id);

    @Query("SELECT b FROM Watchlist b")
    public CompletionStage<List<String>> getEnabledWatchlists();
}
