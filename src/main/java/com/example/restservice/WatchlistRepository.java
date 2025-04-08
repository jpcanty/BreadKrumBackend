package com.example.restservice;
import java.util.List;

import org.springframework.stereotype.Repository;

import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class WatchlistRepository {
   private final RestClient restClient;
   private final ObjectMapper objectMapper;
   private static final String BASE_URL = "https://api.alpaca.markets/v2/watchlists";
   private static final String APCA_API_KEY_ID = "AKWSFWM7A44H4OOBKXPE";
   private static final String APCA_API_SECRET_KEY = "K3pOFIa1upEHYxcpdI50CK86FQ6xKnLIC9UWvBqy";

   public WatchlistRepository () {
    this.restClient = RestClient.builder().build();
    final ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    this.objectMapper = mapper;
   }

   public Watchlist getWatchlistById() throws JsonMappingException, JsonProcessingException {
      String response = restClient.get()
      .uri(BASE_URL)
    .header("APCA-API-KEY-ID", APCA_API_KEY_ID)
    .header("APCA-API-SECRET-KEY", APCA_API_SECRET_KEY)
    .retrieve()
    .body(String.class);

    return objectMapper.readValue(response, Watchlist.class);
   }

   public Watchlist[] getAllWatchlists() throws JsonMappingException, JsonProcessingException {
    String response = restClient.get()
    .uri(BASE_URL)
    .header("APCA-API-KEY-ID", APCA_API_KEY_ID)
    .header("APCA-API-SECRET-KEY", APCA_API_SECRET_KEY)
    .retrieve()
    .body(String.class);

   //  System.out.println(response);
   // [{watchlist1, watchlist2}] <- correct
   // {watchlists: [{watchlist1, watchlist2}]} <- incorrect
   // Watchlist[] myObjects = mapper.readValue(json, Watchlist[].class);
   return objectMapper.readValue(response, Watchlist[].class);
   }
}
