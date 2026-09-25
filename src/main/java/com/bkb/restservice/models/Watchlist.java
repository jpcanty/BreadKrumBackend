package com.bkb.restservice.models;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;
@Entity
@Table( name = "watchlists")
public class Watchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String name;

    private String accountId;

    private boolean enabled;

    private Instant createdAt;

    private Instant updatedAt;

    public record WatchlistRecord(String id, String name, String accountId, List<Asset> assets, boolean enabled, String createdAt, String updatedAt) {}

    public static WatchlistRecord toRecord( Watchlist watchlist){
        return new WatchlistRecord( watchlist.getId(), watchlist.getName(), watchlist.getAccountId(), List.of(), watchlist.getEnabled(), watchlist.getCreatedAt().toString(), watchlist.getUpdatedAt().toString());
     }

     public static WatchlistRecord toRecord( AlpacaWatchlist alpacaWatchlist){
        return new WatchlistRecord( alpacaWatchlist.id() , alpacaWatchlist.name(), alpacaWatchlist.accountId(), List.of(), true, alpacaWatchlist.createdAt().toString().replace("T" ," ").replace("Z" , " "), alpacaWatchlist.updatedAt().toString().replace("T" ," ").replace("Z" , " "));
     }
     String getId(){
        return id;
     }
     String getName(){
        return name;
     }
     String getAccountId(){
        return accountId;
     }
     boolean getEnabled(){
        return enabled;
     }
     Instant getCreatedAt(){
      return createdAt;
     }
     Instant getUpdatedAt() {
      return updatedAt;
     }
}
