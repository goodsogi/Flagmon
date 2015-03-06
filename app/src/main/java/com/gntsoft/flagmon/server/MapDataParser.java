package com.gntsoft.flagmon.server;

import com.pluslibrary.server.PlusInputStreamStringConverter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 6.
 */
public class MapDataParser {

    public ArrayList<MapDataModel> doIt(InputStream in) {
        ArrayList<MapDataModel> model = new ArrayList<>();

        try {
            String rawData = (String) new PlusInputStreamStringConverter().doIt(in);

            JSONArray jsonArray = new JSONArray(rawData);
            for(int i=0; i<jsonArray.length();i++) {
                MapDataModel data = new MapDataModel();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONObject subJsonObject = (JSONObject) jsonObject.get("mapList");

                data.setIdx(subJsonObject.optString("idx"));
                data.setUserId(subJsonObject.optString("uid"));
                data.setUserName(subJsonObject.optString("unm"));
                data.setPostType(subJsonObject.optString("posttype"));
                data.setAlbumName(subJsonObject.optString("album"));
                data.setMemo(subJsonObject.optString("memo"));
                data.setImgUrl(subJsonObject.optString("imgURL"));
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
