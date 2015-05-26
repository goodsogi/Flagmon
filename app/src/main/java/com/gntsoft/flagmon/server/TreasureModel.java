package com.gntsoft.flagmon.server;

/**
 * Created by johnny on 15. 3. 23.
 */
public class TreasureModel {
    private String lat;
    private String lon;
    private String idx;
    private String opencheck;
    private String photoIdx;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getOpencheck() {
        return opencheck;
    }

    public void setOpencheck(String opencheck) {
        this.opencheck = opencheck;
    }

    public String getPhotoIdx() {
        return photoIdx;
    }

    public void setPhotoIdx(String photoIdx) {
        this.photoIdx = photoIdx;
    }
}
