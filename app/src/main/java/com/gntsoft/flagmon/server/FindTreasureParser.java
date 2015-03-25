package com.gntsoft.flagmon.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 23.
 */
public class FindTreasureParser {
    public ArrayList<TreasureModel> doIt(String rawData) {
        ArrayList<TreasureModel> model = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(rawData);
            for (int i = 0; i < jsonArray.length(); i++) {
                TreasureModel data = new TreasureModel();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONObject subJsonObject = (JSONObject) jsonObject.opt("treasurelist");

                data.setLat(subJsonObject.optString("lat"));
                data.setLon(subJsonObject.optString("lon"));
                model.add(data);
            }

        } catch (Exception e) {
            return null;
        }

        return model;
    }
}