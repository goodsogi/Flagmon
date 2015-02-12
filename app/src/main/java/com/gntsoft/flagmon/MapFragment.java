package com.gntsoft.flagmon;

import java.util.ArrayList;

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
import android.widget.Toast;


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

public class MapFragment extends FMCommonFragment implements
		PlusOnGetDataListener, LocationListener {
    private static final long DELAY_TIME = 1000*10;
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    private boolean mIsGpsCatched;
	private static final int GET_MAP_DATA = 0;
    private SupportMapFragment fragment;
    private MapView mMapView;

    public MapFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		 //refreshList();
        // getCurrentLocation();
        showSampleMarkers();
	}




    private void showSampleMarkers() {
        //샘플용 위치와 마커 이미지
        ArrayList<LatLng> positions = new ArrayList<>();
        LatLng position1 = new LatLng(37.587140, 126.994357);
        LatLng position2 = new LatLng(36.817490, 128.627411);
        LatLng position3 = new LatLng(35.862362, 128.585926);
        LatLng position4 = new LatLng(35.185497, 129.023048);
        LatLng position5 = new LatLng(35.415269, 127.391928);

        positions.add(position1);
        positions.add(position2);
        positions.add(position3);
        positions.add(position4);
        positions.add(position5);

        ArrayList<Integer> imgs = new ArrayList<>();


        imgs.add(R.drawable.sandarapark);
        imgs.add(R.drawable.sandarapark2);
        imgs.add(R.drawable.sandarapark3);
        imgs.add(R.drawable.sandarapark4);
        imgs.add(R.drawable.sandarapark5);


        LatLng position = null;
        for (int i = 0; i < positions.size(); i++) {
            mGoogleMap.addMarker(new MarkerOptions().position(positions.get(i)).snippet("" + i)
                    .icon(getMarKerImg(imgs.get(i))).anchor(0f, 1.0f));
            //마커 클릭처리 필요!!
        }
    }

    private BitmapDescriptor getMarKerImg(int imgId) {
        //마스킹
        Bitmap original = BitmapFactory.decodeResource(getResources(), imgId);
        Bitmap frame = BitmapFactory.decodeResource(getResources(), R.drawable.thumbnail_1_0001);
        Bitmap mask = BitmapFactory.decodeResource(getResources(),R.drawable.mask);
        Log.d("mask", "image witdh: " + mask.getWidth() + " height: " + mask.getHeight());
        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(original, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        mCanvas.drawBitmap(frame, 0, 0, null);
        paint.setXfermode(null);


        return  BitmapDescriptorFactory.fromBitmap(result);



    }


    private void setUpMap(Bundle savedInstanceState, View rootView) {
        //서울을 기본 위치로 설정
//        double lat = 37.545231;
//        double lng = 126.981310;
        //충주를 기본 위치로 설정
//        double lat = 36.986828;
//        double lng = 127.936019;
//        LatLng position = new LatLng(lat, lng);
//        GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
//
//        mGoogleMap = ((SupportMapFragment) ((FragmentActivity) mActivity).getSupportFragmentManager()
//                .findFragmentById(R.id.mapview)).getMap();
//        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
//
//        //mGoogleMap.setOnMapClickListener(this);
//
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 7));
//        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//
////                Intent board = new Intent(MainActivity.this, AllListDetailActivity.class);
////                board.putExtra("data", mDatas.get(Integer.parseInt(marker
////                        .getSnippet())));
////                startActivity(board);
//                return false;
//            }
//        });

        MapsInitializer.initialize(getActivity());

        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()) )
        {
            case ConnectionResult.SUCCESS:
                Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
                mMapView = (MapView) rootView.findViewById(R.id.mapview);
                mMapView.onCreate(savedInstanceState);
                // Gets to GoogleMap from the MapView and does initialization stuff
                if(mMapView !=null)
                {
                    mGoogleMap = mMapView.getMap();
                    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mGoogleMap.setMyLocationEnabled(true);

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
            default: Toast.makeText(getActivity(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()), Toast.LENGTH_SHORT).show();
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
                            0, 0, MapFragment.this);
                }

            }
        }, DELAY_TIME);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map,
				container, false);
        setUpMap(savedInstanceState, rootView);
		return rootView;
	}

	@Override
	protected void addListenerButton() {
		// TODO Auto-generated method stub

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
//						+ PlusPhoneNumberFinder.doIt(mActivity),
//				new OrderHistoryParser());
		
	}

}
