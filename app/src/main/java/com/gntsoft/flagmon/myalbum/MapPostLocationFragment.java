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
import com.gntsoft.flagmon.utils.FMPhotoResizer;
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

public class MapPostLocationFragment extends FMCommonFragment implements
        PlusOnGetDataListener, LocationListener, GoogleMap.OnMarkerDragListener {
    private static final long DELAY_TIME = 1000 * 10;
    private static final int GET_MAP_DATA = 0;
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    private boolean mIsGpsCatched;
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
        Double lat = this.getArguments().getDouble(FMConstants.KEY_PHOTO_LAT);
        Double lon = this.getArguments().getDouble(FMConstants.KEY_PHOTO_LON);


        LatLng position = new LatLng(lat, lon);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position,13);
        mGoogleMap.animateCamera(cameraUpdate);

        mGoogleMap.addMarker(new MarkerOptions().position(position)
                .icon(getMarKerImg(filepath)).anchor(0f, 1.0f).draggable(true));
    }



    private BitmapDescriptor getMarKerImg(String filePath) {
        //마스킹

        Bitmap scaledOriginal = FMPhotoResizer.doIt(filePath);
        Bitmap frame = BitmapFactory.decodeResource(getResources(), R.drawable.thumbnail_1_0001);
        Bitmap mask = BitmapFactory.decodeResource(getResources(), R.drawable.mask);
        // Log.d("mask", "image witdh: " + mask.getWidth() + " height: " + mask.getHeight());
        Log.d("mask", "image witdh: " + scaledOriginal.getWidth() + " height: " + scaledOriginal.getHeight());
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

                    mGoogleMap.setOnMarkerDragListener(MapPostLocationFragment.this);
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

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng dragPosition = marker.getPosition();
        ((PostSetLocationActivity) mActivity).setPhotoLat(dragPosition.latitude);
        ((PostSetLocationActivity) mActivity).setPhotoLon(dragPosition.longitude);

    }
}
