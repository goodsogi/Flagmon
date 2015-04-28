package com.gntsoft.flagmon.server;

/**
 * 목적지 데이터 모델
 * 
 * @author jeff
 * 
 */
public class PlaceModel {
	private String name; //장소명
    private String address; //주소
    private String lat; //위도
    private String lng; //경도

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
