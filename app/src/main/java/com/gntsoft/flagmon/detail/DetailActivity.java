package com.gntsoft.flagmon.detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.comment.CommentActivity;
import com.gntsoft.flagmon.login.LoginActivity;
import com.gntsoft.flagmon.main.MainActivity;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FMListParser;
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.server.PostDetailParser;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.gntsoft.flagmon.user.UserActivity;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.gntsoft.flagmon.utils.ScrollViewExt;
import com.gntsoft.flagmon.utils.ScrollViewListener;
import com.kakao.AppActionBuilder;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusListHeightCalculator;
import com.pluslibrary.utils.PlusLogger;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 2. 12.
 */
public class DetailActivity extends FMCommonActivity implements
        PlusOnGetDataListener, ScrollViewListener, PlusSwipeDetector {

    private static final int REPORT_POST = 2;
    private static final int PERFORM_PIN = 22;
    private static final int GET_PHOTOS_NEARBY = 3;
    private static final int GET_DETAIL = 0;
    String[] menuItems = {"신고하기", "URL 복사"};

    private ScrollViewExt mScrollView;
    private String mImageUrl;
    private String mPhotoLat;
    private String mPhotoLon;
    private String mUserEmail;
    private String mUserName;
    private String mPreviousPostId;
    private String mNextPostId;



    @Override
    public void onSuccess(Integer from, Object datas) {

        if (datas == null) return;
        switch (from) {
            case GET_DETAIL:
                handleData(new PostDetailParser().doIt((String) datas));
                break;
            case PERFORM_PIN:
                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(this, model.getResult().contains("succes") ? "스크랩되었습니다" : "스크랩되지 못했습니다");
                if (model.getResult().contains("succes")) {
                    //추가 액션??
                }
                break;
            case REPORT_POST:
                ServerResultModel model2 = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(this, model2.getResult().equals("success") ? "해당 글을 신고했습니다" : "해당 글을 신고지 못했습니다");
                if (model2.getResult().equals("success")) {
                    //추가 액션??
                }
                break;
            case GET_PHOTOS_NEARBY:
                makeGridView(new FMListParser().doIt((String) datas));
                break;
        }
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

    public void launchCommentActivity(View v) {
        PlusClickGuard.doIt(v);
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(FMConstants.KEY_POST_IDX, getIntent().getStringExtra(FMConstants.KEY_POST_IDX));
        startActivity(intent);

    }

    public void launchUserPageActivity(View v) {
        PlusClickGuard.doIt(v);
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra(FMConstants.KEY_USER_EMAIL, mUserEmail);
        intent.putExtra(FMConstants.KEY_USER_NAME, mUserName);

        startActivity(intent);

    }

    public void doPin(View v) {

        PlusClickGuard.doIt(v);

        if (LoginChecker.isLogIn(this)) performPin();
        else showLoginAlertDialog();


    }


    public void changeMapPhoto(View v) {
        PlusClickGuard.doIt(v);
        if (!v.isSelected()) {
            v.setSelected(true);
            showMap();
        } else {
            v.setSelected(false);
            showMainPhoto();
        }

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

    @Override
    public void onLeftToRightSwiped() {
        getPreviousAlbumPost();
    }

    @Override
    public void onRightToLeftSwiped() {
        getNextAlbumPost();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlusLogger.doIt("detail activity onCreate");
        setContentView(R.layout.activity_detail);
        String idx = null;
        String isFromKakao = null;

        Uri uri = getIntent().getData();
        if(uri != null) isFromKakao = uri.getQueryParameter("fromKakao");
        if (isFromKakao != null && Boolean.parseBoolean(isFromKakao)){
             idx = getIntent().getData().getQueryParameter("idx");

        }else{
            idx= getIntent().getStringExtra(FMConstants.KEY_POST_IDX);
        }


        initScrollView();
        getDataFromServer(idx);


    }

    private void getDataFromServer(String idx) {


        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        postParams.add(new BasicNameValuePair("idx", idx));


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


    private void showMainPhoto() {
        //posttype에 따라 PhotoDetailFragment와 AlbumPhotoDetailFragment를 선택!!

        Bundle bundle = new Bundle();
        bundle.putString(FMConstants.KEY_IMAGE_URL, mImageUrl);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.container_detail, fragment)
                .commit();
    }

    private void showMap() {
        //posttype에 따라 MapDetailFragment와 AlbumMapDetailFragment를 선택!!
        Bundle bundle = new Bundle();
        bundle.putString(FMConstants.KEY_IMAGE_URL, mImageUrl);
        bundle.putString(FMConstants.KEY_PHOTO_LAT, mPhotoLat);
        bundle.putString(FMConstants.KEY_PHOTO_LON, mPhotoLon);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.container_detail, fragment)
                .commit();

    }


    private void performPin() {
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        postParams.add(new BasicNameValuePair("photo_idx", getIntent().getStringExtra(FMConstants.KEY_POST_IDX)));


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
                launchLoginActivity();
            }
        });
        ab.show();

    }


    private void doMenu(int whichButton) {
        switch (whichButton) {
            case 0:
                doReport();
                break;
            case 1:
                copyUrl();
                break;
        }
    }

    private void copyUrl() {

        try {
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink();
            final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink
                    .createKakaoTalkLinkMessageBuilder();
            kakaoTalkLinkMessageBuilder.addText("테스트");
            kakaoTalkLinkMessageBuilder.addAppButton(
                    "앱으로 이동",
                    new AppActionBuilder()
                            .setAndroidExecuteURLParam("fromKakao=true&idx=" +getIntent().getStringExtra(FMConstants.KEY_POST_IDX))
                            .build());

            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(),
                    this);

        } catch (KakaoParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//        sendIntent.setType("text/plain");
//        startActivity(sendIntent);
    }

    private void doReport() {
        //특정 사용자 아이디등 처리!!
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        //postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_NEIGHBOR));
        if (LoginChecker.isLogIn(this)) {
            postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        }


        new PlusHttpClient(this, this, false).execute(REPORT_POST,
                FMApiConstants.REPORT_POST, new PlusInputStreamStringConverter(),
                postParams);
    }

    private void launchLoginActivity() {


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    private void makeGridView(final ArrayList<FMModel> fmModels) {
        if(fmModels == null) return;
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new GridviewAdapter(this, fmModels));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchDetailActivity(fmModels.get(position).getIdx());
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

    private void launchDetailActivity(String idx) {
        finish();
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(FMConstants.KEY_POST_IDX, idx);

        startActivity(intent);
    }


    private void handleData(ArrayList<FMModel> fmModels) {

        FMModel model = fmModels.get(0);

        showContent(model);



        getPhotosNearBy(model.getLat(), model.getLon());

        showMainPhoto();

        checkLogin();


        if(model.getPostType().equals("1")) {
            mPreviousPostId = model.getAlbumPreviousPostId();
            mNextPostId = model.getAlbumNextPostId();
            showAlbumTitle(model);
            //onTouch가 호출안됨
            //enableSwipe();
        }


    }

    private void enableSwipe() {
        FrameLayout containerDetail = (FrameLayout) findViewById(R.id.container_detail);
        containerDetail.setOnTouchListener(new View.OnTouchListener() {
            public static final float MIN_DISTANCE = 150;
            public float x1;
            public float x2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;

                        if (Math.abs(deltaX) > MIN_DISTANCE)
                        {
                            // Left to Right swipe action
                            if (x2 > x1)
                            {
                            getPreviousAlbumPost();
                            }

                            // Right to left swipe action
                            else
                            {
                                getNextAlbumPost();
                            }

                        }
                        else
                        {
                            // consider as something else - a screen tap for example
                        }
                        break;
                }
                return true;
            }
        });
    }



    private void getNextAlbumPost() {
        if(mNextPostId == null || mNextPostId.equals("0")) return;
        getDataFromServer(mNextPostId);
    }

    private void getPreviousAlbumPost() {
        if(mPreviousPostId == null|| mPreviousPostId.equals("0")) return;
        getDataFromServer(mPreviousPostId);
    }

    private void showAlbumTitle(FMModel model) {
        LinearLayout containerAlbumTitle = (LinearLayout) findViewById(R.id.containerAlbumTitle);
        containerAlbumTitle.setVisibility(View.VISIBLE);


        TextView albumName = (TextView) findViewById(R.id.albumName);
        albumName.setText(model.getAlbumName());

        TextView currentPage = (TextView) findViewById(R.id.currentPage);
        currentPage.setText(model.getAlbumCurrentPosition());

        TextView totalPage = (TextView) findViewById(R.id.totalPage);
        totalPage.setText(model.getAlbumTotalPost());



    }

    private void getPhotosNearBy(String lat, String lon) {
        //수정이 필요할 수도!!
        Double photoLat = Double.parseDouble(lat);
        Double photoLon = Double.parseDouble(lon);

//        String latUL = String.valueOf(photoLat + 1);
//        String lonUL = String.valueOf(photoLon - 1);
//        String latLR = String.valueOf(photoLat - 1);
//        String lonLR = String.valueOf(photoLon + 1);

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        //postParams.add(new BasicNameValuePair("idx", getIntent().getStringExtra(FMConstants.KEY_POST_IDX)));
        postParams.add(new BasicNameValuePair("list_menu", MainActivity.activeTab));
        postParams.add(new BasicNameValuePair("lat", lat));
        postParams.add(new BasicNameValuePair("lon", lon));



//        postParams.add(new BasicNameValuePair("latUL", latUL));
//        postParams.add(new BasicNameValuePair("lonUL", lonUL));
//        postParams.add(new BasicNameValuePair("latLR", latLR));
//        postParams.add(new BasicNameValuePair("lonLR", lonLR));


        new PlusHttpClient(this, this, false).execute(GET_PHOTOS_NEARBY,
                FMApiConstants.GET_PHOTOS_NEARBY, new PlusInputStreamStringConverter(),
                postParams);


    }

    private void showContent(FMModel data) {

        TextView userName = (TextView) findViewById(R.id.userName);
        TextView content = (TextView) findViewById(R.id.content);
        TextView date = (TextView) findViewById(R.id.date);
        TextView reply = (TextView) findViewById(R.id.replyAlarm);
        TextView pin = (TextView) findViewById(R.id.pin);
        TextView distance = (TextView) findViewById(R.id.distance);
        TextView viewCount = (TextView) findViewById(R.id.viewCount);

        userName.setText(data.getUserName());
        content.setText(data.getMemo());
        date.setText(data.getRegisterDate());
        reply.setText(data.getReplyCount());
        pin.setText(data.getScrapCount());
        viewCount.setText("조회수 " + data.getHitCount());
        //거리 처리!!
        //distance.setText(data.getDistance());

        mImageUrl = data.getImgUrl();
        mPhotoLat = data.getLat();
        mPhotoLon = data.getLon();
        mUserEmail = data.getUserId();
        mUserName = data.getUserName();


    }


}
