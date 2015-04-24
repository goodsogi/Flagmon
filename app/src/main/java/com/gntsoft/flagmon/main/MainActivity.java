package com.gntsoft.flagmon.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.FMTabManager;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.friend.FriendManager;
import com.gntsoft.flagmon.login.LoginActivity;
import com.gntsoft.flagmon.login.SignUpActivity;
import com.gntsoft.flagmon.myalbum.MyAlbumManager;
import com.gntsoft.flagmon.neighbor.NeighborManager;
import com.gntsoft.flagmon.setting.OnKeyBackPressedListener;
import com.gntsoft.flagmon.setting.SettingManager;
import com.pluslibrary.utils.PlusClickGuard;


public class MainActivity extends FMCommonActivity {


    private int mPressedTabId;
    private MyAlbumManager mMyAlbumManager;
    private FriendManager mFriendManager;
    private NeighborManager mNeighborManager;
    private SettingManager mSettingManager;
    private FMTabManager mSelectedTabManager;
    private boolean mIsFirstRun;
    private OnKeyBackPressedListener mOnKeyBackPressedListener;

    public static String activeTab= FMConstants.DATA_TAB_NEIGHBOR;

    public void onResume() {
        super.onResume();
        if (mSelectedTabManager != null && !mIsFirstRun) {
            mSelectedTabManager.chooseFragment();
        }
        mIsFirstRun = false;
    }

    public void changeTab(View v) {
        if (mPressedTabId == v.getId()) return;

        Button neighbor = (Button) findViewById(R.id.tab_neighbor);
        Button friend = (Button) findViewById(R.id.tab_friend);
        Button myalbum = (Button) findViewById(R.id.tab_myalbum);
        Button setting = (Button) findViewById(R.id.tab_setting);

        neighbor.setSelected(false);
        friend.setSelected(false);
        myalbum.setSelected(false);
        setting.setSelected(false);


        switch (v.getId()) {
            case R.id.tab_neighbor:
                activeTab = FMConstants.DATA_TAB_NEIGHBOR;
                neighbor.setSelected(true);
                mNeighborManager.chooseFragment();
                mSelectedTabManager = mNeighborManager;
                break;
            case R.id.tab_friend:
                activeTab = FMConstants.DATA_TAB_FRIEND;
                friend.setSelected(true);
                mFriendManager.chooseFragment();
                mSelectedTabManager = mFriendManager;
                break;
            case R.id.tab_myalbum:
                activeTab = FMConstants.DATA_TAB_MYALBUM;
                myalbum.setSelected(true);
                mMyAlbumManager.chooseFragment();
                mSelectedTabManager = mMyAlbumManager;
                break;
            case R.id.tab_setting:
                setting.setSelected(true);
                mSettingManager.chooseFragment();
                mSelectedTabManager = mSettingManager;
                break;


        }

        mPressedTabId = v.getId();


    }

    public void setOnKeyBackPressedListener(OnKeyBackPressedListener listener) {
        mOnKeyBackPressedListener = listener;
    }

    public void logIn(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void signUp(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(mOnKeyBackPressedListener != null) mOnKeyBackPressedListener.onBack();
        else showExitDialog();
    }

//    private void setLoginFalse() {
//        SharedPreferences sharedPreference = getSharedPreferences(
//                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor e = sharedPreference.edit();
//        e.putBoolean(FMConstants.KEY_IS_LOGIN, false);
//        e.commit();
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIsFirstRun = true;
        //setLoginFalse();
        createTabManagers();
        final Button neighbor = (Button) findViewById(R.id.tab_neighbor);

        //fragment not attached to activity 오류가 발생하여 약간 delay를 줌
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeTab(neighbor);
            }
        }, 1000);



    }

    private void createTabManagers() {
        mNeighborManager = new NeighborManager(this);
        mFriendManager = new FriendManager(this);
        mMyAlbumManager = new MyAlbumManager(this);
        mSettingManager = new SettingManager(this);
    }

    private void showExitDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle(getString(R.string.wanna_exit));
        ab.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        ab.show();
    }


}
