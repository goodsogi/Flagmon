package com.gntsoft.flagmon.myalbum;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gntsoft.flagmon.FMCommonMapFragment;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.utils.FMPhotoResizer;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapPostLocationFragment extends FMCommonMapFragment implements
        GoogleMap.OnMarkerDragListener {
    private static final int MAP_ZOOM_LEVEL = 12;
    private static final int MARKER_ANIMATION_DURATION = 2000;
    private Marker mMarker;

    public MapPostLocationFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGoogleMap.setOnMarkerDragListener(MapPostLocationFragment.this);


        showMarker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_post_location,
                container, false);
        return rootView;
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

    public void moveMarkerToPostion(Double lat, Double lng) {

        LatLng position = new LatLng(lat, lng);


        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, MAP_ZOOM_LEVEL);
        //mGoogleMap.moveCamera(cameraUpdate, MARKER_ANIMATION_DURATION, null);
        mGoogleMap.moveCamera(cameraUpdate);
                mMarker.setPosition(position);
    }

    @Override
    protected void addListenerToButton() {
        // TODO Auto-generated method stub

    }

    private void showMarker() {
        String filepath = this.getArguments().getString(FMConstants.KEY_IMAGE_PATH);
        Double lat = this.getArguments().getDouble(FMConstants.KEY_PHOTO_LAT);
        Double lon = this.getArguments().getDouble(FMConstants.KEY_PHOTO_LON);


        LatLng position = new LatLng(lat, lon);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 13);
        mGoogleMap.moveCamera(cameraUpdate);

        mMarker = mGoogleMap.addMarker(new MarkerOptions().position(position)
                .icon(getMarKerImg(filepath)).anchor(0f, 1.0f).draggable(true));
    }

    private BitmapDescriptor getMarKerImg(String filePath) {
        //마스킹

        Bitmap scaledOriginal = FMPhotoResizer.doIt(mActivity,filePath);
        Bitmap frame = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.thumbnail_1_0001);
        Bitmap mask = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.mask);
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
}
