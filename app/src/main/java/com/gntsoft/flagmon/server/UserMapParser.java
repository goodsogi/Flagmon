package com.gntsoft.flagmon.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 4. 22.
 */
public class UserMapParser {

    public ArrayList<FMModel> doIt(String rawData) {
        ArrayList<FMModel> model = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(rawData);
            JSONArray jsonArray = jsonObject.getJSONArray("maplist");
            for (int i = 0; i < jsonArray.length(); i++) {
                FMModel data = new FMModel();
                JSONObject subJsonObject = (JSONObject) jsonArray.get(i);
                //JSONObject subJsonObject = (JSONObject) jsonObject.opt("maplist");

                data.setIdx(subJsonObject.optString("idx"));
                data.setUserId(subJsonObject.optString("uid"));
                data.setUserName(subJsonObject.optString("unm"));
                data.setPostType(subJsonObject.optString("posttype"));
                data.setAlbumName(subJsonObject.optString("album"));
                data.setMemo(subJsonObject.optString("memo"));
                data.setImgUrl("http://" + subJsonObject.optString("imgURL"));
                data.setLat(subJsonObject.optString("lat"));
                data.setLon(subJsonObject.optString("lon"));
                data.setHitCount(subJsonObject.optString("hit"));
                data.setRegisterDate(subJsonObject.optString("wdate"));
                data.setReplyCount(subJsonObject.optString("replycnt"));
                data.setScrapCount(subJsonObject.optString("scrapcnt"));
                data.setPhotoCount(subJsonObject.optString("readcnt"));
                model.add(data);
            }

        } catch (Exception e) {
            return null;
        }

        return model;
    }
}
