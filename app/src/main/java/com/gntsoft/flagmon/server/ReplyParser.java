package com.gntsoft.flagmon.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 10.
 */
public class ReplyParser {
    public ArrayList<FMModel> doIt(String rawData) {
        ArrayList<FMModel> model = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(rawData);
            for(int i=0; i<jsonArray.length();i++) {
                FMModel data = new FMModel();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONObject subJsonObject = (JSONObject) jsonObject.opt("commentlist");

                data.setIdx(subJsonObject.optString("idx"));
                data.setPhotoIdx(subJsonObject.optString("photo_idx"));
                data.setUserId(subJsonObject.optString("uid"));
                data.setUserName(subJsonObject.optString("unm"));
                data.setMemo(subJsonObject.optString("memo"));
                data.setRegisterDate(subJsonObject.optString("wdate"));
                model.add(data);
            }

        } catch (Exception e) {
            return null;
        }

        return model;
    }
}
