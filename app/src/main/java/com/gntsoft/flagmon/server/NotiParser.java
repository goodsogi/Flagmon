package com.gntsoft.flagmon.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 4. 21.
 */
public class NotiParser {
    public ArrayList<NotiModel> doIt(String rawData) {
        ArrayList<NotiModel> models = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(rawData);
            JSONObject bulletinlist = jsonObject.optJSONObject("bulletinlist");
            String title = bulletinlist.optString("subject");
            String content = bulletinlist.optString("body");
            String wdate = bulletinlist.optString("wdate");

            NotiModel model = new NotiModel();
            model.setTitle(title);
            model.setContent(content);
            model.setWdate(wdate);

            models.add(model);

        } catch (Exception e) {
            return null;
        }

        return models;
    }
}