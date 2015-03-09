package com.gntsoft.flagmon.myalbum;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
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
import com.pluslibrary.server.PlusOnGetDataListener;

import java.io.File;
import java.io.IOException;

public class MapPostLocationFragment extends FMCommonFragment implements
        PlusOnGetDataListener, LocationListener {
    private static final long DELAY_TIME = 1000 * 10;
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    private boolean mIsGpsCatched;
    private static final int GET_MAP_DATA = 0;
    private SupportMapFragment fragment;
    private MapView mMapView;
    private Button mMyLocationButton;

    public MapPostLocationFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        showMarker();
    }


    private void showMarker() {
        String filepath =  this.getArguments().getString(FMConstants.KEY_IMAGE_PATH);
        PhotoLocation photoLocation =
                getPhotoLocation(filepath);

        LatLng position = new LatLng(photoLocation.getLat(), photoLocation.getLon());

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position,13);
        mGoogleMap.animateCamera(cameraUpdate);

        mGoogleMap.addMarker(new MarkerOptions().position(position)
                .icon(getMarKerImg(filepath)).anchor(0f, 1.0f));
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


        }else {
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

    private BitmapDescriptor getMarKerImg(String filePath) {
        //마스킹
        File imgFile = new File(filePath);
        Bitmap original = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        Bitmap scaledOriginal = getScaledOriginal(original);
        Bitmap frame = BitmapFactory.decodeResource(getResources(), R.drawable.thumbnail_1_0001);
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

    private Bitmap getScaledOriginal(Bitmap original) {

        int viewHeight = 142;

        float width = original.getWidth();
        float height = original.getHeight();



// Calculate image's size by maintain the image's aspect ratio
        if(height > viewHeight)
        {
            float percente = (float)(height / 100);
            float scale = (float)(viewHeight / percente);
            width *= (scale / 100);
            height *= (scale / 100);
        }



// Resizing image

        return Bitmap.createScaledBitmap(original, (int) width, (int) height, true);

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


                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position,8);
                    mGoogleMap.moveCamera(cameraUpdate);

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
                            0, 0, MapPostLocationFragment.this);
                }

            }
        }, DELAY_TIME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_post_location,
                container, false);
        setUpMap(savedInstanceState, rootView);
        return rootView;
    }

    @Override
    protected void addListenerButton() {
        // TODO Auto-generated method stub
//        mMyLocationButton = (Button) mActivity.findViewById(R.id.my_location);
//        mMyLocationButton.setOnClickListener(new PlusOnClickListener() {
//            @Override
//            protected void isLogIn() {
//                if (!mMyLocationButton.isSelected()) {
//                    getCurrentLocation();
//                }
//            }
//        });
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case GET_MAP_DATA:
                makeList(datas);
                break;
        }

    }

    private void makeList(Object datas) {

//		ListView list = (ListView) mActivity
//				.findViewById(R.id.list_order_history);
//
//		if(list==null) return;
//		list.setAdapter(new OrderHistoryListAdapter(mActivity,this,
//				(ArrayList<OrderHistoryModel>) datas));
    }

    public void refreshList() {
//		new PlusHttpClient(mActivity, this, false).execute(
//				GET_MAP_DATA,
//				ApiConstants.GET_MAP_DATA + "?id="
//						+ PlusPhoneNumberFinder.isLogIn(mActivity),
//				new OrderHistoryParser());

    }

}
