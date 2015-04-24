package com.gntsoft.flagmon.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 4. 23.
 */
public class UserMonParser {

     public String doIt(String rawData) {

        try {

            JSONObject jsonObject = new JSONObject(rawData);
            return jsonObject.optString("point");


        } catch (Exception e) {
            return null;
        }

    }
}
