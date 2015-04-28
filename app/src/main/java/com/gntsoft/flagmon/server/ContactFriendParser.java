package com.gntsoft.flagmon.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 4. 28.
 */
public class ContactFriendParser {
    public ArrayList<FriendModel> doIt(String rawData) {
        ArrayList<FriendModel> model = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(rawData);
            for (int i = 0; i < jsonArray.length(); i++) {
                FriendModel data = new FriendModel();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONObject subJsonObject = (JSONObject) jsonObject.opt("friendlist");

                data.setProfileImageUrl(subJsonObject.optString("profile_url"));
                data.setName(subJsonObject.optString("user_name"));
                data.setUserEmail(subJsonObject.optString(" user_email"));

                model.add(data);
            }

        } catch (Exception e) {
            return null;
        }

        return model;
    }
}
