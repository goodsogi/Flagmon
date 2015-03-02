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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.login.LoginActivity;
import com.gntsoft.flagmon.reply.ReplyActivity;
import com.gntsoft.flagmon.user.UserPageActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 2. 12.
 */
public class DetailActivity extends Activity {

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private ImageView mMainImage;

    String [] menuItems = {"신고하기", "URL 복사"};
    private boolean login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        makeGridView();
        showMainPhoto();
        checkLogin();
        //addListenerToMainImage();
      }

    private void checkLogin() {
        if (isLogin()) showMenuButton();

    }

    private void showMenuButton() {
        Button menuButton = (Button) findViewById(R.id.menu_detail);
        menuButton.setVisibility(View.VISIBLE);

    }

//    private void addListenerToMainImage() {
//        mMainImage = (ImageView) findViewById(R.id.main_img);
//        mMainImage.setOnClickListener(new PlusOnClickListener() {
//            @Override
//            protected void doIt() {
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


    private void makeGridView() {

        ArrayList<Integer> imgs = new ArrayList<>();


        imgs.add(R.drawable.sandarapark);
        imgs.add(R.drawable.sandarapark2);
        imgs.add(R.drawable.sandarapark3);
        imgs.add(R.drawable.sandarapark4);
        imgs.add(R.drawable.sandarapark5);
        imgs.add(R.drawable.sandarapark);
        imgs.add(R.drawable.sandarapark2);
        imgs.add(R.drawable.sandarapark3);
        imgs.add(R.drawable.sandarapark4);
        imgs.add(R.drawable.sandarapark5);
        imgs.add(R.drawable.sandarapark);
        imgs.add(R.drawable.sandarapark2);
        imgs.add(R.drawable.sandarapark3);
        imgs.add(R.drawable.sandarapark4);
        imgs.add(R.drawable.sandarapark5);


        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new DetailGridviewAdapter(this, imgs));
    }

    public void goBack(View v) {
        finish();
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
        getFragmentManager().beginTransaction()
                .replace(R.id.container_detail, new PhotoDetailFragment())
                .commit();


    }

    private void showMap() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container_detail, new MapDetailFragment())
                .commit();

    }

    public void goToReply(View v) {
        PlusClickGuard.doIt(v);
        Intent intent = new Intent(this, ReplyActivity.class);
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
        PlusToaster.doIt(this, "준비중...");
        //구현!!
    }

    private void goToLogin() {


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public boolean isLogin() {
        SharedPreferences sharedPreference = getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getBoolean(FMConstants.KEY_IS_LOGIN, false);
    }
}
