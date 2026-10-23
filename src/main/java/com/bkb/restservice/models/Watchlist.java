package com.bkb.restservice.models;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "watchlists")
public class Watchlist {
   @Id
   private String id;

   private String name;

   private String accountId;

   private boolean enabled;

   private Instant createdAt;

   private Instant updatedAt;

   public record WatchlistRecord(String id, String name, String accountId, List<Asset> assets, boolean enabled,
         String createdAt, String updatedAt) {
   }

   public static WatchlistRecord toRecord(Watchlist watchlist) {
      return new WatchlistRecord(watchlist.getId(), watchlist.getName(), watchlist.getAccountId(), List.of(),
            watchlist.getEnabled(), cleanTime(watchlist.getCreatedAt().toString()),
            cleanTime(watchlist.getUpdatedAt().toString()));
   }

   public static WatchlistRecord toRecord(AlpacaWatchlist alpacaWatchlist) {
      return new WatchlistRecord(alpacaWatchlist.id(), alpacaWatchlist.name(), alpacaWatchlist.accountId(), List.of(),
            true, cleanTime(alpacaWatchlist.createdAt().toString()),
            cleanTime(alpacaWatchlist.updatedAt().toString()));
   }

   public static String cleanTime(String time) {
      if (!time.contains(".")) {
         return removeTandZ(time.replace("Z", ".0Z"));
      }
      return removeTandZ(time);
   }

   private static String removeTandZ(String time) {
      return time.replace("T", " ").replace("Z", " ");
   }

   String getId() {
      return id;
   }

   String getName() {
      return name;
   }

   String getAccountId() {
      return accountId;
   }

   boolean getEnabled() {
      return enabled;
   }

   Instant getCreatedAt() {
      return createdAt;
   }

   Instant getUpdatedAt() {
      return updatedAt;
   }
}
