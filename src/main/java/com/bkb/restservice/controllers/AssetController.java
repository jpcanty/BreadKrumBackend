package com.bkb.restservice.controllers;

import java.util.concurrent.CompletionStage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bkb.restservice.models.StockPrice;
import com.bkb.restservice.services.StockPriceService;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class AssetController {
    @Autowired 
    StockPriceService stockPriceService;
    
    @GetMapping("asset/price")
    public CompletionStage<StockPrice> getStockPriceHistoryBySymbol(
            @RequestParam(value = "symbol", defaultValue = "GOOG") String symbol,
            @RequestParam(value = "timeframe", defaultValue = "1D") String timeframe) {
        return stockPriceService.getStockPriceHistoryBySymbol(symbol, timeframe);
    }
}
