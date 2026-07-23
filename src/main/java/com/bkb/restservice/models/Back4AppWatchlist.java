package com.bkb.restservice.models;

import java.time.Instant;
import java.util.List;

public record Back4AppWatchlist(String name, String accountId, Instant createdAt, Instant updatedAt, String objectId,
        List<Back4AppAsset> assets, Boolean enabled) {
}
