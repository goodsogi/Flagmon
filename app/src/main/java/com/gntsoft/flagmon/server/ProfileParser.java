package com.gntsoft.flagmon.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 5. 1.
 */
public class ProfileParser {
    public ProfileModel doIt(String rawData) {

        try {
          ProfileModel model = new ProfileModel();
            JSONObject jsonObject = new JSONObject(rawData);

            model.setEmail(jsonObject.optString("user_email"));
            model.setName(jsonObject.optString("user_name"));
            model.setGender(jsonObject.optString("user_gender"));
            model.setBirth(jsonObject.optString("user_birth"));
            model.setPhone(jsonObject.optString("user_phone"));
            model.setPhoto(jsonObject.optString("photo"));
            model.setStatus(jsonObject.optString("status_msg"));

               return model;


        } catch (Exception e) {
            return null;
        }

    }
}
