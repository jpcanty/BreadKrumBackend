package com.bkb.restservice.repositories;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import com.bkb.restservice.models.StockPrice;
import com.bkb.restservice.models.Watchlist.WatchlistRecord;

public interface WatchlistRepository {
    public CompletionStage<String> postWatchlist(WatchlistRecord watchlist);
    public CompletionStage<WatchlistRecord[]> getAllWatchlists();
    public CompletionStage<WatchlistRecord> getWatchlistById(String id);

     public CompletionStage<StockPrice> getStockPriceHistoryBySymbol(String symbol, String timeframe);
     public CompletionStage<StockPrice> getStockPriceHistoryBySymbols(Set<String> symbols, String timeframe);
      public CompletionStage<List<String>> getEnabledWatchlists();
}
