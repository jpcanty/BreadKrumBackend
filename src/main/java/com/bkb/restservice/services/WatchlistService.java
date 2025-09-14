package com.bkb.restservice.services;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bkb.restservice.models.Watchlist;
import com.bkb.restservice.repositories.WatchlistRepository;

@Service
public class WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;
    @Autowired
    private StockPriceService stockPriceService;

    public CompletionStage<List<Watchlist>> getAllWatchlists() {
        return watchlistRepository.getAllWatchlists().thenCompose(this::hydrate);
    }

    private CompletionStage<List<Watchlist>> hydrate(Watchlist[] watchlists) {
    List<CompletionStage<Watchlist>> futures = Arrays.stream(watchlists)
        .map(watchlist -> watchlistRepository.getWatchlistById(watchlist.id())
                .thenApply(watchlistWithAssets -> stockPriceService.hydrateStockPrice(watchlistWithAssets))
        )
        .toList();

    // Combine all futures into one CompletionStage<List<Watchlist>>
    return CompletableFuture
            .allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                    .map(CompletionStage::toCompletableFuture)
                    .map(CompletableFuture::join)
                    .toList()
            );
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
                            return stockPriceService.hydrateStockPrice(watchlistWithAssets);
                        }).thenCompose(watchlistWithAssets -> watchlistRepository.postWatchlist(watchlistWithAssets)))
                .toList();

        // Combine list of futures into one future
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }
}
