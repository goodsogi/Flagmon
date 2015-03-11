package com.gntsoft.flagmon.myalbum;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.login.LoginFragment;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusImageByteConverter;
import com.pluslibrary.utils.PlusPhoneNumberFinder;
import com.pluslibrary.utils.PlusStringEmailChecker;
import com.pluslibrary.utils.PlusTimeFormatter;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 3. 4.
 */
public class PostSetLocationActivity extends FMCommonActivity implements
        PlusOnGetDataListener {
    private static final int SEND_POST = 0;
    private Double mPhotoLat;
    private Double mPhotoLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_set_location);
        getPhotoLatLon();
        showMap();
    }


    private void showMap() {



        Bundle bundle = new Bundle();
        bundle.putString(FMConstants.KEY_IMAGE_PATH, getIntent().getStringExtra(FMConstants.KEY_IMAGE_PATH));
        bundle.putDouble(FMConstants.KEY_PHOTO_LAT, mPhotoLat);
        bundle.putDouble(FMConstants.KEY_PHOTO_LON, mPhotoLon);
        MapPostLocationFragment fragment = new MapPostLocationFragment();
        fragment.setArguments(bundle);


        getFragmentManager().beginTransaction()
                .replace(R.id.container_post_location, fragment)
                .commit();

    }


    public void showChooseShareTypeBar(View v) {
        PlusClickGuard.doIt(v);

        LinearLayout barChooseShareType = (LinearLayout) findViewById(R.id.barChooseShareType);
        barChooseShareType.setVisibility(View.VISIBLE);
    }



    public void onBackPressed() {
        LinearLayout barChooseShareType = (LinearLayout) findViewById(R.id.barChooseShareType);
        if (barChooseShareType.isShown()) barChooseShareType.setVisibility(View.GONE);
        else super.onBackPressed();
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case SEND_POST:
                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(this,model.getResult().equals("success")?"포스팅되었습니다":"포스팅되지 못했습니다");
                if(model.getResult().equals("success")) {
                    //추가 처리??
                }
                break;
        }

    }

    public void completePost(View v) {
        PlusClickGuard.doIt(v);

        EditText photoDescriptionView = (EditText) findViewById(R.id.photoDescription);
        String photoDescription = photoDescriptionView.getText().toString();

        if (photoDescription.equals("")) {
            PlusToaster.doIt(this, "사진 설명을 입력해주세요.");
            return;
        }

        String imgUrl = getIntent().getStringExtra(FMConstants.KEY_IMAGE_PATH);


        MultipartEntity entity = new MultipartEntity();
        try {
            entity.addPart("key", new StringBody(
                    getUserAuthKey()));

            if (imgUrl != null && !imgUrl.equals("")) {

                entity.addPart("photo", PlusImageByteConverter.doIt(imgUrl));
            }
            entity.addPart("memo", new StringBody(
                    photoDescription));
            entity.addPart("lat", new StringBody(String.valueOf(
                    mPhotoLat)));
            entity.addPart("lon", new StringBody(String.valueOf(
                    mPhotoLon)));
            entity.addPart("update", new StringBody(
                    getPhotoTakenTime(imgUrl)));

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        new PlusHttpClient(this, this, true).execute(SEND_POST,
                FMApiConstants.SEND_POST, new PlusInputStreamStringConverter(),
                entity);

    }

    private String getPhotoTakenTime(String imgUrl) {

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imgUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String date = exif.getAttribute(ExifInterface.TAG_DATETIME);
        //date가 null인 경우 처리!!
        return date != null? date: "2014-03-09";
    }


    public String getUserAuthKey() {
        SharedPreferences sharedPreference = getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getString(FMConstants.KEY_USER_AUTH_KEY,"");
    }

    public PhotoLocation getPhotoLocation(String filepath) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String LATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String LATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
        String LONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        String LONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

        // your Final lat Long Values
        Double lat, lon;
        PhotoLocation photoLocation = new PhotoLocation();
        if ((LATITUDE != null)
                && (LATITUDE_REF != null)
                && (LONGITUDE != null)
                && (LONGITUDE_REF != null)) {

            if (LATITUDE_REF.equals("N")) {
                lat = convertToDegree(LATITUDE);
            } else {
                lat = 0 - convertToDegree(LATITUDE);
            }

            if (LONGITUDE_REF.equals("E")) {
                lon = convertToDegree(LONGITUDE);
            } else {
                lon = 0 - convertToDegree(LONGITUDE);
            }


            photoLocation.setLat(lat);
            photoLocation.setLon(lon);


        } else {
            //사진에 gps 정보가 없는 경우 임시로 서울을 지정!!
            photoLocation.setLat(37.561562);
            photoLocation.setLon(127.010149);
        }

        return photoLocation;
    }


    private Double convertToDegree(String stringDMS){
        Double result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0/S1;

        result = new Double(FloatD + (FloatM/60) + (FloatS/3600));

        return result;


    };

    public void getPhotoLatLon() {
        String imgPath = getIntent().getStringExtra(FMConstants.KEY_IMAGE_PATH);
        PhotoLocation photoLocation =
                getPhotoLocation(imgPath);

        mPhotoLat = photoLocation.getLat();
        mPhotoLon = photoLocation.getLon();

    }

    public void setPhotoLat(double latitude) {
        mPhotoLat = latitude;
    }

    public void setPhotoLon(double longitude) {
        mPhotoLon = longitude;
    }


//
//    @Override
//    public String toString() {
//        // TODO Auto-generated method stub
//        return (String.valueOf(Latitude)
//                + ", "
//                + String.valueOf(Longitude));
//    }
//
//    public int getLatitudeE6(){
//        return (int)(Latitude*1000000);
//    }
//
//    public int getLongitudeE6(){
//        return (int)(Longitude*1000000);
//    }
}





