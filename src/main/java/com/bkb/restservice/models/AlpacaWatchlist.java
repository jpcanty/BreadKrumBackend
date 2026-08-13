package com.bkb.restservice.models;

import java.time.Instant;

public record AlpacaWatchlist(String id, String name, String accountId, Instant createdAt, Instant updatedAt) {}
