package com.bkb.restservice.models;

import java.time.Instant;
import java.util.List;

public record AlpacaWatchlistWithAssets(String id, String name, String accountId, Instant createdAt, Instant updatedAt, List<Asset> assets) {}
