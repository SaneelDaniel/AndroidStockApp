package edu.sjsu.android.StockSearch;

import org.json.JSONObject;

public class StockInfo {

    String ticker;
    String name;
    String price;
    JSONObject jsonObject;
    boolean favorite;

    public StockInfo() {

    }

    public StockInfo(String ticker, String currentPrice) {
        this.ticker = ticker;
        this.name = price;
        favorite = false;
    }

    public StockInfo(JSONObject json_object) {
        jsonObject = json_object;
    }
}
