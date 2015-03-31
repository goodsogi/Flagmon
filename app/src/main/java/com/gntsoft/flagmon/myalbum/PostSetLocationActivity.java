package com.gntsoft.flagmon.myalbum;

import android.media.ExifInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FMListParser;
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.server.PhotoLocationModel;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusImageByteConverter;
import com.pluslibrary.utils.PlusLogger;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 3. 4.
 */
public class PostSetLocationActivity extends FMCommonActivity implements
        PlusOnGetDataListener {
    private static final int SEND_POST = 0;
    private static final int SEARCH_LOCATION = 1;
    private Double mPhotoLat;
    private Double mPhotoLon;
    private String shareType;

    public void performLocationSearch(View v) {
        PlusClickGuard.doIt(v);
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_NEIGHBOR));
        if (LoginChecker.isLogIn(this)) {
            postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        }


        new PlusHttpClient(this, this, false).execute(SEARCH_LOCATION,
                FMApiConstants.SEARCH_LOCATION, new PlusInputStreamStringConverter(),
                postParams);


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
                Log.d("flagmon", model.getMsg());
                PlusLogger.doIt(model.getMsg());
                PlusToaster.doIt(this, model.getResult().equals("success") ? "포스팅되었습니다" : "포스팅되지 못했습니다");
                if (model.getResult().equals("success")) {
                    //추가 처리??
                }
                break;

            case SEARCH_LOCATION:
                makeList(new FMListParser().doIt((String) datas));
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
        String encodedPhotoDescription = null;
        try {
            encodedPhotoDescription = URLEncoder.encode(photoDescription, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        MultipartEntity entity = new MultipartEntity();
        try {
            entity.addPart("key", new StringBody(
                    getUserAuthKey()));

            entity.addPart("list_menu", new StringBody(
                    getShareType()));

            if (imgUrl != null && !imgUrl.equals("")) {

                entity.addPart("photo", PlusImageByteConverter.doIt(imgUrl));
            }
            entity.addPart("memo", new StringBody(
                    encodedPhotoDescription));
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

    public PhotoLocationModel getPhotoLocation(String filepath) {
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
        PhotoLocationModel photoLocation = new PhotoLocationModel();
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

    public void setPhotoLat(double latitude) {
        mPhotoLat = latitude;
    }

    public void setPhotoLon(double longitude) {
        mPhotoLon = longitude;
    }

    public String getShareType() {
        CheckBox checkboxShareAll = (CheckBox) findViewById(R.id.checkboxShareAll);
        CheckBox checkboxShareFriend = (CheckBox) findViewById(R.id.checkboxShareFriend);
        CheckBox checkboxPrivate = (CheckBox) findViewById(R.id.checkboxPrivate);
        if (checkboxShareAll.isChecked()) return FMConstants.DATA_TAB_NEIGHBOR;
        if (checkboxShareFriend.isChecked()) return FMConstants.DATA_TAB_FRIEND;
        if (checkboxPrivate.isChecked()) return FMConstants.DATA_TAB_MYALBUM;
        return FMConstants.DATA_TAB_NEIGHBOR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_set_location);
        addListenerToView();
        getPhotoLatLon();
        showMap();
    }

    private void addListenerToView() {
        EditText locationSearchInput = (EditText) findViewById(R.id.locationSearchInput);
        final Button locationSearchButton = (Button) findViewById(R.id.locationSearchButton);

        final Button completePost = (Button) findViewById(R.id.completePost);

        final CheckBox checkboxShareAll = (CheckBox) findViewById(R.id.checkboxShareAll);
        final CheckBox checkboxShareFriend = (CheckBox) findViewById(R.id.checkboxShareFriend);
        final CheckBox checkboxPrivate = (CheckBox) findViewById(R.id.checkboxPrivate);


        final LinearLayout barCheckboxShareAll = (LinearLayout) findViewById(R.id.barCheckboxShareAll);
        final LinearLayout barCheckboxShareFriend = (LinearLayout) findViewById(R.id.barCheckboxShareFriend);
        final LinearLayout barCheckboxPrivate = (LinearLayout) findViewById(R.id.barCheckboxPrivate);

        barCheckboxShareAll.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                checkboxShareAll.setChecked(true);
            }
        });

        barCheckboxShareFriend.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                checkboxShareFriend.setChecked(true);
            }
        });


        barCheckboxPrivate.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                checkboxPrivate.setChecked(true);
            }
        });


        locationSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edit) {
                String s = edit.toString();
                if (s.length() > 0 && !locationSearchButton.isEnabled())
                    locationSearchButton.setEnabled(true);
            }
        });

        EditText photoDescription = (EditText) findViewById(R.id.photoDescription);
        photoDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edit) {
                String s = edit.toString();
                if (s.length() > 0 && !completePost.isEnabled() && (checkboxShareAll.isChecked() || checkboxShareFriend.isChecked() || checkboxPrivate.isChecked()))
                    completePost.setEnabled(true);
            }
        });


        checkboxShareAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (checkboxShareFriend.isChecked()) checkboxShareFriend.setChecked(false);
                    if (checkboxPrivate.isChecked()) checkboxPrivate.setChecked(false);
                    if (!completePost.isEnabled()) completePost.setEnabled(true);

                }
            }
        });

        checkboxShareFriend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (checkboxShareAll.isChecked()) checkboxShareAll.setChecked(false);
                    if (checkboxPrivate.isChecked()) checkboxPrivate.setChecked(false);
                    if (!completePost.isEnabled()) completePost.setEnabled(true);
                }
            }
        });

        checkboxPrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (checkboxShareAll.isChecked()) checkboxShareAll.setChecked(false);
                    if (checkboxShareFriend.isChecked()) checkboxShareFriend.setChecked(false);
                    if (!completePost.isEnabled()) completePost.setEnabled(true);
                }
            }
        });


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

    private void makeList(final ArrayList<FMModel> datas) {

        ListView list = (ListView) findViewById(R.id.listSearchLocation);

        if (list == null || datas == null) return;
        list.setAdapter(new SearchLocationListAdapter(this,
                datas));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moveMarkerToPostion();
            }
        });

        //리스트 크기 제한(50dp??)
    }

    private void moveMarkerToPostion() {
        //구현!!
    }

    ;

    private String getPhotoTakenTime(String imgUrl) {

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imgUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String date = exif.getAttribute(ExifInterface.TAG_DATETIME);
        //date가 null인 경우 처리!!
        return date != null ? date : "2014-03-09";
    }

    private Double convertToDegree(String stringDMS) {
        Double result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0 / D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0 / M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0 / S1;

        result = new Double(FloatD + (FloatM / 60) + (FloatS / 3600));

        return result;


    }

    private void getPhotoLatLon() {
        String imgPath = getIntent().getStringExtra(FMConstants.KEY_IMAGE_PATH);
        PhotoLocationModel photoLocation =
                getPhotoLocation(imgPath);

        mPhotoLat = photoLocation.getLat();
        mPhotoLon = photoLocation.getLon();

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





