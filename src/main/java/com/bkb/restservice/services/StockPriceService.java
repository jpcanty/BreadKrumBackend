package com.bkb.restservice.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.bkb.restservice.models.Asset;
import com.bkb.restservice.models.PriceHistory;
import com.bkb.restservice.models.StockPrice;
import com.bkb.restservice.models.Watchlist;
import com.bkb.restservice.repositories.StockPriceRepository;

public class StockPriceService {
    @Autowired
    private StockPriceRepository stockPriceRepository;

    public static Asset stockPriceAsset(Asset asset, StockPrice stockPrice) {
        PriceHistory[] priceHistory = stockPrice.bars().get(asset.symbol());
        double price = getPrice(stockPrice.bars(), asset.symbol());
        double pct = 0.0;
        if (priceHistory != null && priceHistory.length > 0) {
            double close = Double.parseDouble(priceHistory[0].c());
            double open = Double.parseDouble(priceHistory[0].o());
            pct = calculatePct(close, open);
        }
        return new Asset(asset.id(), asset.symbol(), asset.name(), price, pct);
    }

    public Watchlist hydrateStockPrice(Watchlist watchlistWithAssets) {
        Set<String> assetSymbols = watchlistWithAssets.assets().stream().map(Asset::symbol)
                .collect(Collectors.toSet());
        StockPrice stockPrice = stockPriceRepository.getStockPriceHistoryBySymbols(assetSymbols, "1D")
                .toCompletableFuture()
                .join();
        List<Asset> newAssets = watchlistWithAssets.assets().stream()
                .map(asset -> {
                    return StockPriceService.stockPriceAsset(asset, stockPrice);
                })
                .toList();

        Watchlist newWatchlist = new Watchlist(
                watchlistWithAssets.id(),
                watchlistWithAssets.name(),
                watchlistWithAssets.account_id(),
                newAssets);
        return newWatchlist;
    }

    private static double getPrice(Map<String, PriceHistory[]> bars, String symbol) {
        PriceHistory[] priceHistory = bars.get(symbol);

        if (priceHistory == null) {
            System.out.println(bars);
            System.out.println(symbol);
            return 0;
        }

        return Double.valueOf(priceHistory[0].c());
    }

    private static double calculatePct(double close, double open) {
        if (open == 0)
            return 0.0;
        double pct = (close - open) / open;
        System.out.println(pct);
        return Math.round(pct * 10000.0) / 10000.0;
    }

    public CompletionStage<StockPrice> getStockPriceHistoryBySymbol(String symbol, String timeframe) {
        return stockPriceRepository.getStockPriceHistoryBySymbol(symbol, timeframe);
    }
}
