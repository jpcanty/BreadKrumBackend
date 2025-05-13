package com.bkb.restservice.models;
import java.util.List;

public record Watchlist(String id, String name, String account_id, List<Asset> assets) {}