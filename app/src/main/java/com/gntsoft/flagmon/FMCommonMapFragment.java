package com.gntsoft.flagmon;

import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.pluslibrary.utils.PlusToaster;

/**
 * Created by johnny on 15. 3. 24.
 */
public class FMCommonMapFragment extends FMCommonFragment {
    protected GoogleMap mGoogleMap;
    private MapView mMapView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpMap(savedInstanceState);

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

    @Override
    protected void addListenerToButton() {

    }

    private void setUpMap(Bundle savedInstanceState) {


        MapsInitializer.initialize(getActivity());

        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity())) {
            case ConnectionResult.SUCCESS:
                PlusToaster.doIt(mActivity, "SUCCESS");
                mMapView = (MapView) mActivity.findViewById(R.id.mapview);
                mMapView.onCreate(savedInstanceState);
                // Gets to GoogleMap from the MapView and does initialization stuff
                if (mMapView != null) {
                    mGoogleMap = mMapView.getMap();

                    double lat = 36.986828;
                    double lng = 127.936019;
                    LatLng position = new LatLng(lat, lng);


                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 7);
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
                PlusToaster.doIt(mActivity, "SERVICE MISSING");
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                PlusToaster.doIt(mActivity, "UPDATE REQUIRED");
                break;
            default:
        }

    }
}
