package com.bkb.restservice.services;

import java.util.concurrent.CompletionStage;

import org.springframework.stereotype.Service;

import com.bkb.restservice.models.Asset;

@Service 
public class AssetService {
    public CompletionStage<Asset> getAsset(String symbol) {
        return null;
    }
}
