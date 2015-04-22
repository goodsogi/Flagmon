package com.gntsoft.flagmon.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 4. 22.
 */
public class UserParser {

    public UserModel doIt(String rawData) {
        UserModel model = new UserModel();

        try {

            JSONObject jsonObject = new JSONObject(rawData);

            model.setStatusMsg(jsonObject.optString("status_msg"));
            model.setBirthday(jsonObject.optString("birthday"));
            model.setUserTotalPost(jsonObject.optString("post_cnt"));
            model.setUserTotalScrap(jsonObject.optString("scrap_cnt"));


        } catch (Exception e) {
            return null;
        }

        return model;
    }
}