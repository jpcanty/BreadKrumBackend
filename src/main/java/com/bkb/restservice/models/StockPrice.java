package com.bkb.restservice.models;
import java.util.Map;

public record StockPrice(Map<String, PriceHistory[]> bars, String nextPageToken){}
