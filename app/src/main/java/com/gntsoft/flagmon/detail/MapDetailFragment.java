package com.gntsoft.flagmon.detail;

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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pluslibrary.server.PlusOnGetDataListener;

public class MapDetailFragment extends FMCommonMapFragment implements
        PlusOnGetDataListener {
    private static final int GET_MAP_DATA = 0;


    public MapDetailFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getImageFromServer();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_detail,
                container, false);
        //setUpMap(savedInstanceState, rootView);
        return rootView;
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

    @Override
    protected void addListenerToButton() {
        // TODO Auto-generated method stub

    }

    private BitmapDescriptor getMarKerImg(Bitmap original) {
        //마스킹
        //Bitmap scaledOriginal = getScaledOriginal(original);
        Bitmap scaledOriginal = FMPhotoResizer.doIt(original);
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

    private void makeList(Object datas) {


    }


    private void getImageFromServer() {
        mImageLoader.loadImage(
                getArguments().getString(FMConstants.KEY_IMAGE_URL),
                mOption, new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onLoadingFailed(String arg0, View arg1,
                                                FailReason arg2) {


                    }

                    @Override
                    public void onLoadingComplete(String arg0, View arg1,
                                                  Bitmap bitmap) {

                        showMarker(bitmap);

                    }

                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
                        // TODO Auto-generated method stub

                    }
                });

    }

    private void showMarker(Bitmap bitmap) {

        Double lat = Double.parseDouble(getArguments().getString(FMConstants.KEY_PHOTO_LAT));
        Double lon = Double.parseDouble(getArguments().getString(FMConstants.KEY_PHOTO_LON));

        LatLng position = new LatLng(lat, lon);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 13);
        mGoogleMap.animateCamera(cameraUpdate);

        mGoogleMap.addMarker(new MarkerOptions().position(position)
                .icon(getMarKerImg(bitmap)).anchor(0f, 1.0f));

    }
}
