package com.bkb.restservice.models;
import jakarta.persistence.*;

import java.util.List;
@Entity
@Table( name = "watchlist")
public class Watchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String name;

    private String account_id;

    private boolean enabled;

    public record WatchlistRecord(String id, String name, String account_id, List<Asset> assets, boolean enabled) {}

    public static WatchlistRecord toRecord( Watchlist watchlist){
        return new WatchlistRecord( watchlist.getId(), watchlist.getName(), watchlist.getAccountId(), List.of(), watchlist.getEnabled() );
     }
     String getId(){
        return id;
     }
     String getName(){
        return name;
     }
     String getAccountId(){
        return account_id;
     }
     boolean getEnabled(){
        return enabled;
     }
}
