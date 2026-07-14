package com.bkb.restservice.models;
import java.util.List;
public class Watchlist {
    public record WatchlistRecord(String id, String name, String account_id, List<Asset> assets, boolean enabled) {}
}
