package com.gntsoft.flagmon.detail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.comment.CommentActivity;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FMListParser;
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.server.PostDetailParser;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.login.LoginActivity;
import com.gntsoft.flagmon.user.UserPageActivity;
import com.gntsoft.flagmon.utils.ScrollViewExt;
import com.gntsoft.flagmon.utils.ScrollViewListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusListHeightCalculator;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 2. 12.
 */
public class DetailActivity extends FMCommonActivity implements
        PlusOnGetDataListener, ScrollViewListener {

    private static final int REPORT_POST = 2;
    private static final int PERFORM_PIN = 22;
    private static final int GET_PHOTOS_NEARBY = 3;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private ImageView mMainImage;

    String [] menuItems = {"신고하기", "URL 복사"};
    private boolean login;
    private ScrollViewExt mScrollView;

    private static final int GET_DETAIL = 0;
    private String mImageUrl;
    private String mPhotoLat;
    private String mPhotoLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initScrollView();
        getDataFromServer();



        //addListenerToMainImage();
      }

    public void getDataFromServer() {


        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        postParams.add(new BasicNameValuePair("idx", getIntent().getStringExtra(FMConstants.KEY_POST_IDX)));


        new PlusHttpClient(this, this, false).execute(GET_DETAIL,
                FMApiConstants.GET_DETAIL, new PlusInputStreamStringConverter(),
                postParams);
    }


    private void initScrollView() {
        mScrollView = (com.gntsoft.flagmon.utils.ScrollViewExt) findViewById(R.id.scrollView);
        mScrollView.setScrollViewListener(this);

    }

    private void checkLogin() {
        if (LoginChecker.isLogIn(this)) showMenuButton();

    }

    private void showMenuButton() {
        Button menuButton = (Button) findViewById(R.id.menu_detail);
        menuButton.setVisibility(View.VISIBLE);

    }

//    private void addListenerToMainImage() {
//        mMainImage = (ImageView) findViewById(R.id.main_img);
//        mMainImage.setOnClickListener(new PlusOnClickListener() {
//            @Override
//            protected void isLogIn() {
//                goToImageViewer();
//            }
//        });
//    }





    private BitmapDescriptor getMarKerImg(int imgId) {
        //마스킹
        Bitmap original = BitmapFactory.decodeResource(getResources(), imgId);
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




    public void changeMapPhoto(View v) {
        PlusClickGuard.doIt(v);
        if(!v.isSelected()) {
            v.setSelected(true);
            showMap();
        } else {
            v.setSelected(false);
            showMainPhoto();
        }

    }

    private void showMainPhoto() {

        Bundle bundle = new Bundle();
        bundle.putString(FMConstants.KEY_IMAGE_URL, mImageUrl);
        PhotoDetailFragment fragment = new PhotoDetailFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.container_detail, fragment)
                .commit();
        }

    private void showMap() {
        Bundle bundle = new Bundle();
        bundle.putString(FMConstants.KEY_IMAGE_URL, mImageUrl);
        bundle.putString(FMConstants.KEY_PHOTO_LAT, mPhotoLat);
        bundle.putString(FMConstants.KEY_PHOTO_LON, mPhotoLon);
        MapDetailFragment fragment = new MapDetailFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.container_detail, fragment)
                .commit();

    }

    public void goToCommentActivity(View v) {
        PlusClickGuard.doIt(v);
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(FMConstants.KEY_POST_IDX, getIntent().getStringExtra(FMConstants.KEY_POST_IDX));
        startActivity(intent);

    }

    public void goToUserPage(View v) {
        //!!사용자 고유번호 전달
        PlusClickGuard.doIt(v);
        Intent intent = new Intent(this, UserPageActivity.class);
        startActivity(intent); 

    }

    public void doPin(View v) {

        PlusClickGuard.doIt(v);

        if(LoginChecker.isLogIn(this)) performPin();
                else showLoginAlertDialog();



    }

    private void performPin() {
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        postParams.add(new BasicNameValuePair("idx", getIntent().getStringExtra(FMConstants.KEY_POST_IDX)));


        new PlusHttpClient(this, this, false).execute(PERFORM_PIN,
                FMApiConstants.PERFORM_PIN, new PlusInputStreamStringConverter(),
                postParams);

    }

    private void showLoginAlertDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("로그인 후 스크랩 할 수 있어요. 로그인하시겠습니까?");
        ab.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                goToLogin();
            }
        });
        ab.show();

    }

    public void showMenuPopup(View v) {

        PlusClickGuard.doIt(v);

        AlertDialog.Builder ab = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("선택해주세요.");
        ab.setItems(menuItems, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                doMenu(whichButton);

            }
        }).setNegativeButton("닫기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        ab.show();

    }

    private void doMenu(int whichButton) {
        switch(whichButton) {
            case 0:
                doReport();
                break;
            case 1:
                copyUrl();
                break;
        }
    }

    private void copyUrl() {
        PlusToaster.doIt(this, "준비중...");
        //구현!!
    }

    private void doReport() {
        //특정 사용자 아이디등 처리!!
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        //postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_NEIGHBOR));
        if(LoginChecker.isLogIn(this)) { postParams.add(new BasicNameValuePair("key", getUserAuthKey()));}


        new PlusHttpClient(this, this, false).execute(REPORT_POST,
                FMApiConstants.REPORT_POST, new PlusInputStreamStringConverter(),
                postParams);
    }

    private void goToLogin() {


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void onSuccess(Integer from, Object datas) {

        if(datas == null) return;
        switch (from) {
            case GET_DETAIL:
                handleData(new PostDetailParser().doIt((String) datas));
                break;
            case PERFORM_PIN:
                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(this,model.getResult().equals("success")?"스크랩되었습니다":"스크랩되지 못했습니다");
                if(model.getResult().equals("success")) {
                    //추가 액션??
                }
                break;
            case REPORT_POST:
                ServerResultModel model2 = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(this,model2.getResult().equals("success")?"해당 글을 신고했습니다":"해당 글을 신고지 못했습니다");
                if(model2.getResult().equals("success")) {
                    //추가 액션??
                }
                break;
            case GET_PHOTOS_NEARBY:
                makeGridView(new FMListParser().doIt((String) datas));
                break;
        }
    }

    private void makeGridView(final ArrayList<FMModel> fmModels) {
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new DetailGridviewAdapter(this, fmModels));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToDetail(fmModels.get(position).getIdx());
            }
        });


        PlusListHeightCalculator.setGridViewHeightBasedOnChildren(this,
                gridview, 5f, 66);
        //scrollview의 맨위로 올리는데 아래 코드만 작동
        gridview.post(new Runnable() {
            public void run() {
                mScrollView.fullScroll(View.FOCUS_UP);
            }
        });

    }

    private void goToDetail(String idx) {
        finish();
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(FMConstants.KEY_POST_IDX, idx);

        startActivity(intent);
    }


    private void handleData(ArrayList<FMModel> fmModels) {

        showContent(fmModels);

        getPhotosNearBy(fmModels.get(0).getLat(),fmModels.get(0).getLon());

        showMainPhoto();

        checkLogin();





    }

    private void getPhotosNearBy(String lat, String lon) {
        //수정이 필요할 수도!!
        Double photoLat = Double.parseDouble(lat);
        Double photoLon = Double.parseDouble(lon);

        String latUL = String.valueOf(photoLat + 1);
        String lonUL = String.valueOf(photoLon - 1);
        String latLR = String.valueOf(photoLat - 1);
        String lonLR = String.valueOf(photoLon + 1);

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        postParams.add(new BasicNameValuePair("idx", getIntent().getStringExtra(FMConstants.KEY_POST_IDX)));
        postParams.add(new BasicNameValuePair("latUL", latUL));
        postParams.add(new BasicNameValuePair("lonUL", lonUL));
        postParams.add(new BasicNameValuePair("latLR", latLR));
        postParams.add(new BasicNameValuePair("lonLR", lonLR));




        new PlusHttpClient(this, this, false).execute(GET_PHOTOS_NEARBY,
                FMApiConstants.GET_PHOTOS_NEARBY, new PlusInputStreamStringConverter(),
                postParams);




    }

    private void showContent(ArrayList<FMModel> fmModels) {
        FMModel data = fmModels.get(0);
        TextView userName = (TextView) findViewById(R.id.userName);
        TextView content = (TextView) findViewById(R.id.content);
        TextView date = (TextView) findViewById(R.id.date);
        TextView reply = (TextView) findViewById(R.id.reply);
        TextView pin = (TextView) findViewById(R.id.pin);
        TextView distance = (TextView) findViewById(R.id.distance);



        content.setText(data.getMemo());
        date.setText(data.getRegisterDate());
        reply.setText(data.getReplyCount());
        pin.setText(data.getScrapCount());
        //거리 처리!!
        //distance.setText(data.getDistance());

        mImageUrl = data.getImgUrl();
        mPhotoLat = data.getLat();
        mPhotoLon = data.getLon();

    }

    @Override
    public void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy) {
// We take the last son in the scrollview
        View view = (View) scrollView
                .getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
                .getScrollY()));

        // if diff is zero, then the bottom has been reached
        if (diff == 0) {
            // ScrollView 맨밑에 도달하면 리스트 아이템 추가!!
//            addItem();
//            PlusListHeightCalculator.setGridViewHeightBasedOnChildren(
//                    mActivity, mList, 2f, 229);
        }
    }
}
