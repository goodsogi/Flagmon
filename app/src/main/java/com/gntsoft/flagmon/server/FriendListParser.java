package com.gntsoft.flagmon.server;

import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.setting.FriendModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 13.
 */
public class FriendListParser {
    public ArrayList<FriendModel> doIt(String rawData) {
        ArrayList<FriendModel> model = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(rawData);
            for(int i=0; i<jsonArray.length();i++) {
                FriendModel data = new FriendModel();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONObject subJsonObject = (JSONObject) jsonObject.opt("commentlist");

                data.setId(subJsonObject.optString("uid"));
                data.setProfileImageUrl(subJsonObject.optString("imgURL"));
                data.setName(subJsonObject.optString("unm"));

                model.add(data);
            }

        } catch (Exception e) {
            return null;
        }

        return model;
    }
}