package com.bkb.restservice.models;

import java.util.List;

record WatchlistObjectId(String objectId, String name) {}

public record WatchlistsResponse(List<WatchlistObjectId> results) {}