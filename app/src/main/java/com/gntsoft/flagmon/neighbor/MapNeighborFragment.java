package com.gntsoft.flagmon.neighbor;

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

import com.gntsoft.flagmon.FMCommonMapFragment;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.detail.DetailActivity;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FMMapParser;
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.server.FindTreasureParser;
import com.gntsoft.flagmon.server.TreasureModel;
import com.gntsoft.flagmon.utils.FMLocationFinder;
import com.gntsoft.flagmon.utils.FMLocationListener;
import com.gntsoft.flagmon.utils.FMPhotoResizer;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class MapNeighborFragment extends FMCommonMapFragment implements
        PlusOnGetDataListener, FMLocationListener {
    private static final int FIND_TREASURE = 11;
    private static final int GET_MAP_DATA = 0;
    String[] mapOptionDatas = {"인기순", "최근 등록순"};

    private Button mMyLocationButton;

    public MapNeighborFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        checkLogin();
        getDataFromServer(FMConstants.SORT_BY_POPULAR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_neighbor,
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
            case GET_MAP_DATA:
                handleMapData(new FMMapParser().doIt((String) datas));
                break;

            case FIND_TREASURE:
                showTreasures(new FindTreasureParser().doIt((String) datas));
                break;
        }

    }

    public void getDataFromServer(String sortType) {


        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_NEIGHBOR));
        postParams.add(new BasicNameValuePair("sort", sortType));
        if (LoginChecker.isLogIn(mActivity)) {
            postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        }


        new PlusHttpClient(mActivity, this, false).execute(GET_MAP_DATA,
                FMApiConstants.GET_MAP_DATA, new PlusInputStreamStringConverter(),
                postParams);
    }

    @Override
    public void onGPSCatched(Location location) {
        mMyLocationButton.setSelected(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
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

    private void checkLogin() {
        if (LoginChecker.isLogIn(mActivity)) showTreasureButton();

    }

    private void showTreasureButton() {
        Button treasureButton = (Button) mActivity.findViewById(R.id.treasureBox);
        treasureButton.setVisibility(View.VISIBLE);
        treasureButton.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                showTreasureAlertDialog();
            }
        });
    }

    private void showTreasureAlertDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(mActivity, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("현재 지도 범위 내 보물상자를 검색합니다." +
                " 검색시 mon 1개가 차감됩니다");
        ab.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("검색", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                findTreasure();
            }
        });
        ab.show();
    }

    private void findTreasure() {
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_NEIGHBOR));
        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));


        new PlusHttpClient(mActivity, this, false).execute(FIND_TREASURE,
                FMApiConstants.FIND_TREASURE, new PlusInputStreamStringConverter(),
                postParams);

    }

    private void handleMapData(ArrayList<FMModel> datas) {

        mGoogleMap.clear();

        for (int i = 0; i < datas.size(); i++) {
            fetchImageFromServer(datas.get(i));


        }
    }

    private void fetchImageFromServer(final FMModel mapDataModel) {
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
                .icon(getMarKerImg(bitmap, mapDataModel.getPostType())).anchor(0f, 1.0f));
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

    private BitmapDescriptor getMarKerImg(Bitmap original, String postType) {

        //마스킹 이미지를 xxhdpi 폴더에 넣으면 마스킹이 안됨, xhdpi 폴더에 넣어야 함
        //마스킹
        Bitmap scaledOriginal = FMPhotoResizer.doIt(original);
        Bitmap frame = BitmapFactory.decodeResource(getResources(), postType.equals("0") ? R.drawable.thumbnail_1_0001 : R.drawable.thumbnail_1_0002);//0: 포스팅, 1: 앨범
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

    private void getCurrentLocation() {
        FMLocationFinder locationFinder = FMLocationFinder.getInstance(mActivity, this);
        locationFinder.doIt();
    }

    private void showTreasures(ArrayList<TreasureModel> datas) {
        String message = "500개의 보물상자와 100개의 mon이 검색되었습니다.";
        showTreasureResultAlertDialog(message);

        //보물상자 버튼 아이콘은 gone시키고 검색된 보물상자 아이콘 표시
        Button treasureBox = (Button) mActivity.findViewById(R.id.treasureBox);
        treasureBox.setVisibility(View.GONE);

        for (int i = 0; i < datas.size(); i++) {
            showTreasureMarkers(datas.get(i));


        }

    }

    private void showTreasureMarkers(TreasureModel data) {
        LatLng latLng = new LatLng(Double.parseDouble(data.getLat()), Double.parseDouble(data.getLon()));
        mGoogleMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.treasure)).anchor(0f, 1.0f));
        //idx가 없음!!
//        mGoogleMap.addMarker(new MarkerOptions().position(latLng).snippet(data.getIdx())
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.treasure)).anchor(0f, 1.0f));
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                goToDetail(marker.getSnippet());
                return false;
            }
        });
    }

    private void showTreasureResultAlertDialog(String message) {
        AlertDialog.Builder ab = new AlertDialog.Builder(mActivity, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("검색결과");
        ab.setMessage(message);
        ab.setNeutralButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        ab.show();
    }
}
