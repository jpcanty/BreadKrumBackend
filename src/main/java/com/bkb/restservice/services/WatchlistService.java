package com.bkb.restservice.services;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bkb.restservice.models.Asset;
import com.bkb.restservice.models.PriceHistory;
import com.bkb.restservice.models.StockPrice;
import com.bkb.restservice.models.Watchlist;
import com.bkb.restservice.repositories.WatchlistRepository;

@Service
public class WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;
    // @Autowired
    // private StockPriceRepository repo;

    public CompletionStage<Watchlist[]> getAllWatchlists() {
        return watchlistRepository.getAllWatchlists();
    }

    public CompletionStage<Watchlist> getWatchlistById(String id) {
        return watchlistRepository.getWatchlistById(id);
    }

    public CompletionStage<String> postWatchlist(Watchlist watchlist) {
        return watchlistRepository.postWatchlist(watchlist);

    }

    public CompletionStage<Void> postAllWatchlists() {
        return watchlistRepository.getAllWatchlists().thenCompose(this::postAllWatchlists);
    }

    private CompletionStage<Void> postAllWatchlists(Watchlist[] watchlists) {
        List<CompletionStage<String>> futures = Arrays.stream(watchlists)
                // Get all watchlist assets
                .map(watchlist -> watchlistRepository.getWatchlistById(watchlist.id())
                        .thenApply(watchlistWithAssets -> {
                            // 1
                            Set<String> assetSymbols = watchlistWithAssets.assets().stream().map(Asset::symbol)
                                    .collect(Collectors.toSet());
                            // 2
                            StockPrice stockPrice= watchlistRepository.getStockPriceHistoryBySymbols(assetSymbols, "1D").toCompletableFuture()
                                    .join();

                            // 3 Incomplete (Use result from step 1 and 2, to append stock price and pct
                            // change)
                            List<Asset> newAssets = watchlistWithAssets.assets().stream()
                    .map(asset -> {
                        PriceHistory[] priceHistory = stockPrice.bars().get(asset.symbol());
                        double price = getPrice(stockPrice.bars(), asset.symbol());
                        double pct = 0.0;
                        if (priceHistory != null && priceHistory.length > 0) {
                            double close = Double.parseDouble(priceHistory[0].c());
                            double open = Double.parseDouble(priceHistory[0].o());
                            pct = calculatePct(close, open);
                        }
                        return new Asset(asset.id(), asset.symbol(), asset.name(), price, pct);
                    })
                    .toList();

                Watchlist newWatchlist = new Watchlist(
                    watchlistWithAssets.id(),
                    watchlistWithAssets.name(),
                    watchlistWithAssets.account_id(),
                    newAssets
                );
                return newWatchlist;
            })
                        // TODO
                        // 1. Get symbols set from watchlists
                        // 2. Get stock symbols for watchlist
                        // 3. Aggregate stock price into Watchlist via Assets

                        // Write watchlist to db
                        .thenCompose(watchlistWithAssets -> watchlistRepository.postWatchlist(watchlistWithAssets)))
                .toList();

        // Combine list of futures into one future
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private double getPrice(Map<String,PriceHistory[]> bars, String symbol) {
        PriceHistory[] priceHistory = bars.get(symbol);

        if (priceHistory == null) {
            System.out.println(bars);
            System.out.println(symbol);
            return 0;
        }

        return Double.valueOf(priceHistory[0].c());
    }

    private double calculatePct(double close, double open) {
        if (open == 0) return 0.0;
        double pct = (close - open) / open;
        System.out.println(pct);
        return Math.round(pct * 10000.0) / 10000.0;
    }

    public CompletionStage<StockPrice> getStockPriceHistoryBySymbol(String symbol, String timeframe) {
        return watchlistRepository.getStockPriceHistoryBySymbol(symbol, timeframe);
    }
}
