package com.bkb.restservice.models;

import java.time.Instant;

public record Back4AppWatchlist(String id, String name, String accountId, Instant createdAt, Instant updatedAt, String objectId) {}
