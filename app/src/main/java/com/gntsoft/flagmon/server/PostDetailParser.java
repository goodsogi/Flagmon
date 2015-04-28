package com.gntsoft.flagmon.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 10.
 */
public class PostDetailParser {
    public ArrayList<FMModel> doIt(String rawData) {
        ArrayList<FMModel> model = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(rawData);
            for (int i = 0; i < jsonArray.length(); i++) {
                FMModel data = new FMModel();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONObject subJsonObject = (JSONObject) jsonObject.opt("postview");

                data.setIdx(subJsonObject.optString("idx"));
                data.setUserId(subJsonObject.optString("uid"));
                data.setUserName(subJsonObject.optString("unm"));
                data.setPostType(subJsonObject.optString("posttype"));
                data.setAlbumName(subJsonObject.optString("album"));
                data.setAlbumTotalPost(subJsonObject.optString("album_cnt"));
                data.setAlbumCurrentPosition(subJsonObject.optString("album_seq"));
                data.setAlbumPreviousPostId(subJsonObject.optString("album_prev"));
                data.setAlbumNextPostId(subJsonObject.optString("album_next"));
                data.setMemo(subJsonObject.optString("memo"));
                data.setImgUrl("http://" + subJsonObject.optString("imgURL"));
                data.setLat(subJsonObject.optString("lat"));
                data.setLon(subJsonObject.optString("lon"));
                data.setHitCount(subJsonObject.optString("hit"));
                data.setRegisterDate(subJsonObject.optString("wdate"));
                data.setReplyCount(subJsonObject.optString("replycnt"));
                data.setScrapCount(subJsonObject.optString("scrapcnt"));
                data.setPhotoCount(subJsonObject.optString("readcnt"));
                data.setUserEmail(subJsonObject.optString("userEmail"));
                model.add(data);
            }

        } catch (Exception e) {
            return null;
        }

        return model;
    }
}
