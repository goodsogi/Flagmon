package com.gntsoft.flagmon.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 13.
 */
public class SearchFriendByNameListParser
{
    public ArrayList<FriendModel> doIt(String rawData) {
        ArrayList<FriendModel> model = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(rawData);
            for (int i = 0; i < jsonArray.length(); i++) {
                FriendModel data = new FriendModel();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONObject subJsonObject = (JSONObject) jsonObject.opt("friendlist");

                data.setIdx(subJsonObject.optString("idx"));
                data.setProfileImageUrl(subJsonObject.optString("profile_url"));
                data.setName(subJsonObject.optString("user_name"));
                data.setIsFavorite(subJsonObject.optString("is_favorite"));
                data.setUserEmail(subJsonObject.optString("user_email"));

                model.add(data);
            }

        } catch (Exception e) {
            return null;
        }

        return model;
    }
}