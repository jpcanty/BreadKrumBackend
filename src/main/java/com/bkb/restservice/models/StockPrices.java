package com.bkb.restservice.models;
import java.util.Map;

public record StockPrices(Map<String, PriceHistory[]> bars, String nextPageToken){}
