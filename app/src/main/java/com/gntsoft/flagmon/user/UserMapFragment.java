package com.gntsoft.flagmon.user;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.detail.DetailActivity;
import com.gntsoft.flagmon.main.MainActivity;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FMMapParser;
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusViewHolder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by johnny on 15. 2. 27.
 */
public class UserMapFragment extends FMCommonFragment implements
        PlusOnGetDataListener, LocationListener {
    private static final long DELAY_TIME = 1000 * 10;
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    private boolean mIsGpsCatched;
    private static final int GET_TOTAL_USER_POST = 7;
    private static final int GET_USER_MAP_DATA = 0;
    private MapView mMapView;
    private Button mMyLocationButton;

    String [] mapOptionDatas = {"인기순","최근 등록순"};

    public UserMapFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDataFromServer(FMConstants.SORT_BY_POPULAR);
    }

    public void getDataFromServer(String sortType) {

//특정 사용자 이메일등 처리!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("user_email", "user@email.com"));
        postParams.add(new BasicNameValuePair("sort", sortType));
        if(LoginChecker.isLogIn(mActivity)) { postParams.add(new BasicNameValuePair("key", getUserAuthKey()));}


        new PlusHttpClient(mActivity, this, false).execute(GET_USER_MAP_DATA,
                FMApiConstants.GET_USER_MAP_DATA, new PlusInputStreamStringConverter(),
                postParams);
    }




    private BitmapDescriptor getMarKerImg(Bitmap original) {

        //마스킹 이미지를 xxhdpi 폴더에 넣으면 마스킹이 안됨, xhdpi 폴더에 넣어야 함
        //마스킹
        Bitmap frame = BitmapFactory.decodeResource(getResources(), R.drawable.thumbnail_1_0001);
        Bitmap mask = BitmapFactory.decodeResource(getResources(), R.drawable.mask);
        Log.d("mask", "image witdh: " + mask.getWidth() + " height: " + mask.getHeight());
        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(original, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        mCanvas.drawBitmap(frame, 0, 0, null);
        paint.setXfermode(null);


        return BitmapDescriptorFactory.fromBitmap(result);


    }


    private void setUpMap(Bundle savedInstanceState, View rootView) {


        MapsInitializer.initialize(getActivity());

        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity())) {
            case ConnectionResult.SUCCESS:
                Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
                mMapView = (MapView) rootView.findViewById(R.id.mapview);
                mMapView.onCreate(savedInstanceState);
                // Gets to GoogleMap from the MapView and does initialization stuff
                if (mMapView != null) {
                    mGoogleMap = mMapView.getMap();
//                    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
//                    mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
//                    mGoogleMap.setMyLocationEnabled(true);
//??수정
                    double lat = 36.986828;
                    double lng = 127.936019;
                    LatLng position = new LatLng(lat, lng);


                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 7);
                    mGoogleMap.animateCamera(cameraUpdate);

                    mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

//                Intent board = new Intent(MainActivity.this, AllListDetailActivity.class);
//                board.putExtra("data", mDatas.get(Integer.parseInt(marker
//                        .getSnippet())));
//                startActivity(board);
                            return false;
                        }
                    });
                }
                break;
            case ConnectionResult.SERVICE_MISSING:
                Toast.makeText(getActivity(), "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Toast.makeText(getActivity(), "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getActivity(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /**
     * location listener 제거
     */
    public void removeLocationListener() {

        mLocationManager.removeUpdates(this);
        mIsGpsCatched = false;

    }


    @Override
    public void onLocationChanged(Location location) {
        removeLocationListener();
        mMyLocationButton.setSelected(true);
        mIsGpsCatched = true;
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
        //showHouseMarkers();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    private void getCurrentLocation() {

        mLocationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, this);
        mIsGpsCatched = false;
        //테스트용
//		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//				5000, 5, this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!mIsGpsCatched) {

                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            0, 0, UserMapFragment.this);
                }

            }
        }, DELAY_TIME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_map,
                container, false);
        setUpMap(savedInstanceState, rootView);
        return rootView;
    }

    @Override
    protected void addListenerButton() {
        // TODO Auto-generated method stub
        mMyLocationButton = (Button) mActivity.findViewById(R.id.my_location);
        mMyLocationButton.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                if (!mMyLocationButton.isSelected()) {
                    getCurrentLocation();
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

    private void doSort(int whichButton) {


        switch (whichButton) {
            case 0: sortByPopular();
                break;

            case 1: sortByRecent();
                break;



        }
    }



    private void sortByRecent() {
        getDataFromServer(FMConstants.SORT_BY_RECENT);
    }

    private void sortByPopular() {
        getDataFromServer(FMConstants.SORT_BY_POPULAR);
    }


    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case GET_USER_MAP_DATA:
                handleMapData(new FMMapParser().doIt((String) datas));
                getTotalUserPost();
                break;
            case GET_TOTAL_USER_POST:
                //파서등 수정!!
                showTotalUserPost(new FMMapParser().doIt((String) datas));
                ((UserPageActivity) mActivity).setTotalUserPost(10);
                break;
        }

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
        if(LoginChecker.isLogIn(mActivity)) { postParams.add(new BasicNameValuePair("key", getUserAuthKey()));}


        new PlusHttpClient(mActivity, this, false).execute(GET_TOTAL_USER_POST,
                FMApiConstants.GET_TOTAL_USER_POST, new PlusInputStreamStringConverter(),
                postParams);


    }

    private void handleMapData(ArrayList<FMModel> datas) {

        mGoogleMap.clear();

        for (int i = 0; i < datas.size(); i++)
        {
            fetchImageFromServer(datas.get(i), i);


        }
    }

    private void fetchImageFromServer(final FMModel mapDataModel, final int position) {
        mImageLoader.loadImage(mapDataModel.getImgUrl(),mOption,new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                showMarkers(bitmap,mapDataModel);


            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });





    }

    private void showMarkers(Bitmap bitmap,FMModel mapDataModel) {
        LatLng latLng = new LatLng(Double.parseDouble(mapDataModel.getLat()), Double.parseDouble(mapDataModel.getLon()));
        mGoogleMap.addMarker(new MarkerOptions().position(latLng).snippet(mapDataModel.getIdx())
                .icon(getMarKerImg(bitmap)).anchor(0f, 1.0f));
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
