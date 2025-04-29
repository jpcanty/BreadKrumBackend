package com.example.restservice.models;
import java.util.List;
// add assets in record, it's type is array that returns list of objects 
// nested types 

 /* Approach 1
  Create a nested record for the structure each assets: List<Asset> assets and public record Asset() passing fields into params
 */
public record Watchlist(String id, String name, String account_id, List<Asset> assets) {
    public record Asset( String id,
        String cusip,
        String assetClass, // Renamed from "class" to avoid reserved keyword conflict
        String exchange,
        String symbol,
        String name,
        String status,
        boolean tradable,
        boolean marginable,
        int maintenance_margin_requirement,
        String margin_requirement_long,
        String margin_requirement_short,
        boolean shortable,
        boolean easy_to_borrow,
        boolean fractionable,
        Object attributes // Use Object for attributes since it's null
        ) {
    
    }
}

/* 
Approach 2 
Use a generic type like List<Map<String, Object>> assets 
*/

// public record Watchlist(String id, String name, String account_id, List<Map<String, Object>> assets) {
// }
