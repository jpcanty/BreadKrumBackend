package com.example.restservice;

import java.util.List;

public record Watchlist(String id, String name, List<String> assets) {}
