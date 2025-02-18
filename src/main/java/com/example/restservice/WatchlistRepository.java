package com.example.restservice;
import org.springframework.stereotype.Repository;

import org.springframework.web.client.RestClient;

@Repository
public class WatchlistRepository {
   private final RestClient restClient;
   private static final String BASE_URL = "https://api.alpaca.markets/v2/watchlists";
   private static final String APCA_API_KEY_ID = "AKWSFWM7A44H4OOBKXPE";
   private static final String APCA_API_SECRET_KEY = "K3pOFIa1upEHYxcpdI50CK86FQ6xKnLIC9UWvBqy";

   public WatchlistRepository () {
    this.restClient = RestClient.builder().build();
   }

   public String getAllWatchlists(){
    String response = restClient.get()
    .uri(BASE_URL)
    .header("APCA-API-KEY-ID", APCA_API_KEY_ID)
    .header("APCA-API-SECRET-KEY", APCA_API_SECRET_KEY)
    .retrieve()
    .body(String.class);

   //  System.out.println(response);
    return response;
   }
}
