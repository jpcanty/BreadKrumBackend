package com.bkb.restservice.repositories;

import com.bkb.restservice.ApplicationConfiguration;
import com.bkb.restservice.models.Watchlist;
import com.bkb.restservice.models.Watchlist.WatchlistRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletionStage;


@Component(ApplicationConfiguration.SQLITE_RESPOSITORY)
public interface SQLiteWatchlistRepository extends WatchlistRepository, JpaRepository<Watchlist, String> {
    @Override
    @Modifying
    @Transactional
    @Query("INSERT INTO Watchlist (id, name, accountId , enabled, createdAt, updatedAt ) VALUES(:id, :name , :accountId , :enabled, :createdAt,:updatedAt)")
    public void insertWatchlist(@Param("id")String id ,@Param("name") String name, @Param("accountId") String accountId,@Param("enabled") boolean enabled, @Param("createdAt") String createdAt, @Param("updatedAt")String updatedAt);

    @Override
    @Modifying
    @Transactional
    @Query("UPDATE Watchlist b SET b.enabled =  true  WHERE b.name = :name")
    public void enableWatchlist(@Param("name") String name);

    @Override
    @Modifying
    @Transactional
    @Query("UPDATE Watchlist b SET b.enabled =  false  WHERE b.name = :name")
    public void disableWatchlist(@Param("name") String name);

    @Override
    @Query("SELECT b FROM Watchlist b")
    public List<Watchlist> getAllWatchlists();


    @Override
    @Query ("SELECT b FROM Watchlist b WHERE b.accountId = :accountId")
    public List<Watchlist> getWatchlistsByAccountId(@Param("accountId") String accountId);

    @Override
    @Query("SELECT b FROM Watchlist b WHERE b.name = :name")
    public List<Watchlist> getWatchlistsByIdName(@Param( "name") String name);

    @Override
    @Query("SELECT b FROM Watchlist b WHERE b.enabled =  true")
    public List<Watchlist> getEnabledWatchlists();

}
