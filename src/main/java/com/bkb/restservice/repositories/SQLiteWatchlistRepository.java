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

    @Query ("SELECT b FROM Watchlist b WHERE b.accountId = :accountId")
    public List<Watchlist> getWatchlistsByAccountId(@Param("accountId") String accountId);

    @Query("SELECT b FROM Watchlist b WHERE b.name = :name")
    public CompletionStage<WatchlistRecord> getWatchlistByIdName(@Param( "name") String name);

    @Query("SELECT b FROM Watchlist b WHERE b.enabled =  1")
    public List<Watchlist> getEnabledWatchlists();
}
