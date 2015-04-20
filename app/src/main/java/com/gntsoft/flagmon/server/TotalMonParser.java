package com.gntsoft.flagmon.server;

import org.json.JSONObject;

/**
 * Created by johnny on 15. 4. 20.
 */
public class TotalMonParser {
    public String doIt(String rawData) {


        try {

            JSONObject jsonObject = new JSONObject(rawData);
            return jsonObject.optString("point");


        } catch (Exception e) {
            return null;
        }

    }
}
