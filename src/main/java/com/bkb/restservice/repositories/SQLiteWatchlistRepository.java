package com.bkb.restservice.repositories;

import com.bkb.restservice.models.StockPrice;
import com.bkb.restservice.models.Watchlist.WatchlistRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;

public interface SQLiteWatchlistRepository extends WatchlistRepository, JpaRepository<WatchlistRecord, String> {
    @Override
    public CompletionStage<String> postWatchlist(WatchlistRecord watchlist);

    @Query("SELECT b FROM Watchlist b")
    public CompletionStage<WatchlistRecord[]> getAllWatchlists()
        //in here we got to create the logic that outputs the watchlists in the database to our java{}
        ;

    @Override
    public CompletionStage<WatchlistRecord> getWatchlistById(String id);
    @Override
    public CompletionStage<StockPrice> getStockPriceHistoryBySymbol(String symbol, String timeframe) ;

    @Override
    public CompletionStage<StockPrice> getStockPriceHistoryBySymbols(Set<String> symbols, String timeframe);

    @Override
    public CompletionStage<List<String>> getEnabledWatchlists();
}
