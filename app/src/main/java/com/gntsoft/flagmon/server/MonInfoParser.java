package com.gntsoft.flagmon.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 4. 20.
 */
public class MonInfoParser {

    public ArrayList<MonInfoModel> doIt(String rawData) {
        ArrayList<MonInfoModel> model = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(rawData);
            JSONArray jsonArray = jsonObject.getJSONArray("history");
            for (int i = 0; i < jsonArray.length(); i++) {
                MonInfoModel data = new MonInfoModel();
                JSONObject subJsonObject = (JSONObject) jsonArray.get(i);
                data.setSeq(subJsonObject.optString("seq"));
                data.setValueDate(subJsonObject.optString("value_date"));
                data.setReason(subJsonObject.optString("reason"));
                data.setPoint(subJsonObject.optString("point"));
                data.setExpireDate(subJsonObject.optString("expire_date"));

                model.add(data);
            }

        } catch (Exception e) {
            return null;
        }

        return model;
    }
}
