package com.gntsoft.flagmon.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMCommonMapFragment;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.detail.DetailActivity;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FMMapParser;
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.utils.FMLocationFinder;
import com.gntsoft.flagmon.utils.FMLocationListener;
import com.gntsoft.flagmon.utils.FMPhotoResizer;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusLogger;
import com.pluslibrary.utils.PlusOnClickListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 2. 27.
 */
public class MapUserFragment extends FMCommonMapFragment implements
        PlusOnGetDataListener, FMLocationListener {
    private static final int GET_TOTAL_REPLY_PIN = 7;
    private static final int GET_USER_MAP_DATA = 0;
    String[] mapOptionDatas = {"인기순", "최근 등록순"};

    private Button mMyLocationButton;
    private boolean mIsMapDrawn;

    public MapUserFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addListenerToMap();
    }

    public void getDataFromServer(String sortType) {
        LatLngBounds bounds = mGoogleMap.getProjection().getVisibleRegion().latLngBounds;

        double left = bounds.southwest.longitude;
        double top = bounds.northeast.latitude;
        double right = bounds.northeast.longitude;
        double bottom = bounds.southwest.latitude;
        ((FMCommonActivity) mActivity).setLatUL(bounds.northeast.latitude);
        ((FMCommonActivity) mActivity).setLonUL(bounds.southwest.longitude);
        ((FMCommonActivity) mActivity).setLatLR(bounds.southwest.latitude);
        ((FMCommonActivity) mActivity).setLonLR(bounds.northeast.longitude);


        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("user_email", mActivity.getIntent().getStringExtra(FMConstants.KEY_USER_EMAIL)));
        postParams.add(new BasicNameValuePair("latUL", String.valueOf(bounds.northeast.latitude)));
        postParams.add(new BasicNameValuePair("lonUL", String.valueOf(bounds.southwest.longitude)));
        postParams.add(new BasicNameValuePair("latLR", String.valueOf(bounds.southwest.latitude)));
        postParams.add(new BasicNameValuePair("lonLR", String.valueOf(bounds.northeast.longitude)));
        postParams.add(new BasicNameValuePair("sort", sortType));
        if (LoginChecker.isLogIn(mActivity)) {
            postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        }


        new PlusHttpClient(mActivity, this, false).execute(GET_USER_MAP_DATA,
                FMApiConstants.GET_USER_MAP_DATA, new PlusInputStreamStringConverter(),
                postParams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_map,
                container, false);
        return rootView;
    }

    public void showSortPopup(View v) {
        PlusClickGuard.doIt(v);

        AlertDialog.Builder ab = new AlertDialog.Builder(mActivity, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("정렬방식을 선택해주세요.");
        ab.setItems(mapOptionDatas, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                doSort(whichButton);

            }
        }).setNegativeButton("닫기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        ab.show();
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case GET_USER_MAP_DATA:
                clearMap();
                handleMapData(new FMMapParser().doIt((String) datas));
                setIsMapDrawnTrue();
                getTotalUserPost();
                break;
            case GET_TOTAL_REPLY_PIN:
                //파서등 수정!!
                showTotalUserPost(new FMMapParser().doIt((String) datas));
                ((UserPageActivity) mActivity).setTotalUserPost(10);
                break;
        }

    }

    @Override
    public void onGPSCatched(Location location) {
        Button myLocationButton = (Button) mActivity.findViewById(R.id.my_location);
        myLocationButton.setSelected(true);

        MapView mapView = (MapView) mActivity.findViewById(R.id.mapview);
        GoogleMap googleMap = mapView.getMap();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));

    }

    @Override
    protected void addListenerToButton() {
        // TODO Auto-generated method stub
        mMyLocationButton = (Button) mActivity.findViewById(R.id.my_location);
        mMyLocationButton.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                if (!mMyLocationButton.isSelected()) {

                    showAlertDialog();

                }
            }
        });

        Button sort = (Button) mActivity.findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortPopup(v);
            }
        });
    }

    private boolean isGPSCatched() {
        FMLocationFinder locationFinder = FMLocationFinder.getInstance(mActivity, this);
        return locationFinder.isGpsCatched();
    }

    private void showAlertDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(mActivity, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("GPS 기능을 활성화 하시겠습니까?");
        ab.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                getCurrentLocation();
            }
        });
        ab.show();
    }

    private void clearMap() {
        mGoogleMap.clear();
    }

    private void setIsMapDrawnTrue() {
        mIsMapDrawn = true;
    }

    private void addListenerToMap() {
        mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                getDataFromServer(FMConstants.SORT_BY_POPULAR);
            }
        });


        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                LatLngBounds bounds = mGoogleMap.getProjection().getVisibleRegion().latLngBounds;


                if (mIsMapDrawn) {
                    mIsMapDrawn = false;
                    getNewDataFromServer(bounds);
                }
            }
        });
    }

    private void getNewDataFromServer(LatLngBounds bounds) {
        PlusLogger.doIt("bounds: " + bounds.northeast.latitude + " " + bounds.southwest.longitude + " " +
                bounds.southwest.latitude + " " + bounds.northeast.longitude);
        double left = bounds.southwest.longitude;
        double top = bounds.northeast.latitude;
        double right = bounds.northeast.longitude;
        double bottom = bounds.southwest.latitude;

        //동서남북이 헷갈림
        ((FMCommonActivity) mActivity).setLatUL(bounds.northeast.latitude);
        ((FMCommonActivity) mActivity).setLonUL(bounds.southwest.longitude);
        ((FMCommonActivity) mActivity).setLatLR(bounds.southwest.latitude);
        ((FMCommonActivity) mActivity).setLonLR(bounds.northeast.longitude);

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("user_email", mActivity.getIntent().getStringExtra(FMConstants.KEY_USER_EMAIL)));
        postParams.add(new BasicNameValuePair("latUL", String.valueOf(bounds.northeast.latitude)));
        postParams.add(new BasicNameValuePair("lonUL", String.valueOf(bounds.southwest.longitude)));
        postParams.add(new BasicNameValuePair("latLR", String.valueOf(bounds.southwest.latitude)));
        postParams.add(new BasicNameValuePair("lonLR", String.valueOf(bounds.northeast.longitude)));
        postParams.add(new BasicNameValuePair("sort", "0"));
        if (LoginChecker.isLogIn(mActivity)) {
            postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        }


        new PlusHttpClient(mActivity, this, false).execute(GET_USER_MAP_DATA,
                FMApiConstants.GET_USER_MAP_DATA, new PlusInputStreamStringConverter(),
                postParams);
    }


    private BitmapDescriptor getMarKerImg(Bitmap original, String postType) {

        //마스킹 이미지를 xxhdpi 폴더에 넣으면 마스킹이 안됨, xhdpi 폴더에 넣어야 함
        //마스킹
        Bitmap scaledOriginal = FMPhotoResizer.doIt(original);
        Bitmap frame = BitmapFactory.decodeResource(getResources(), postType.equals("0") ? R.drawable.thumbnail_1_0001 : R.drawable.marker_album_frame);//0: 포스팅, 1: 앨범
        Bitmap mask = BitmapFactory.decodeResource(getResources(), R.drawable.mask);
        Log.d("mask", "image witdh: " + mask.getWidth() + " height: " + mask.getHeight());
        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(scaledOriginal, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        mCanvas.drawBitmap(frame, 0, 0, null);
        paint.setXfermode(null);


        return BitmapDescriptorFactory.fromBitmap(result);


    }

    private void getCurrentLocation() {
        FMLocationFinder locationFinder = FMLocationFinder.getInstance(mActivity, this);
        locationFinder.doIt();
    }

    private void doSort(int whichButton) {


        switch (whichButton) {
            case 0:
                sortByPopular();
                break;

            case 1:
                sortByRecent();
                break;


        }
    }

    private void sortByRecent() {
        getDataFromServer(FMConstants.SORT_BY_RECENT);
    }

    private void sortByPopular() {
        getDataFromServer(FMConstants.SORT_BY_POPULAR);
    }

    private void showTotalUserPost(ArrayList<FMModel> fmModels) {
//수정!!
        TextView reply = (TextView) mActivity.findViewById(R.id.reply);
        TextView pin = (TextView) mActivity.findViewById(R.id.pin);
        reply.setText(fmModels.get(0).getScrapCount());
        pin.setText(fmModels.get(0).getScrapCount());


    }

    private void getTotalUserPost() {
        //특정 사용자 아이디등 처리!!
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_NEIGHBOR));
        if (LoginChecker.isLogIn(mActivity)) {
            postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        }


        new PlusHttpClient(mActivity, this, false).execute(GET_TOTAL_REPLY_PIN,
                FMApiConstants.GET_TOTAL_REPLY_PIN, new PlusInputStreamStringConverter(),
                postParams);


    }

    private void handleMapData(ArrayList<FMModel> datas) {

        mGoogleMap.clear();

        for (int i = 0; i < datas.size(); i++) {
            fetchImageFromServer(datas.get(i), i);


        }
    }

    private void fetchImageFromServer(final FMModel mapDataModel, final int position) {
        mImageLoader.loadImage(mapDataModel.getImgUrl(), mOption, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                showMarkers(bitmap, mapDataModel);


            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });


    }

    private void showMarkers(Bitmap bitmap, FMModel mapDataModel) {
        LatLng latLng = new LatLng(Double.parseDouble(mapDataModel.getLat()), Double.parseDouble(mapDataModel.getLon()));
        mGoogleMap.addMarker(new MarkerOptions().position(latLng).snippet(mapDataModel.getIdx())
                .icon(getMarKerImg(bitmap, mapDataModel.getPostType())).anchor(mapDataModel.getPostType().equals("0") ? 0f : 0.14f, mapDataModel.getPostType().equals("0") ? 1.0f : 0.92f));
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                goToDetail(marker.getSnippet());
                return false;
            }
        });
    }

    private void goToDetail(String idx) {
        Intent intent = new Intent(mActivity, DetailActivity.class);
        intent.putExtra(FMConstants.KEY_POST_IDX, idx);

        startActivity(intent);
    }


}
