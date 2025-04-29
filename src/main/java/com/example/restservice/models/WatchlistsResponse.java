package com.example.restservice.models;

import java.util.List;

public class WatchlistsResponse {
    private List <Watchlist> watchlists;

    public List<Watchlist> getWatchlists() {
        return this.watchlists;
    }

    public void setWatchlists(List <Watchlist> watchlists) {
        this.watchlists = watchlists;
    }
}
