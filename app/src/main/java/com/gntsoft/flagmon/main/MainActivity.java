package com.gntsoft.flagmon.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.FMTabManager;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.comment.CommentActivity;
import com.gntsoft.flagmon.detail.DetailActivity;
import com.gntsoft.flagmon.friend.FriendManager;
import com.gntsoft.flagmon.login.LoginActivity;
import com.gntsoft.flagmon.login.SignUpActivity;
import com.gntsoft.flagmon.myalbum.MyAlbumManager;
import com.gntsoft.flagmon.neighbor.NeighborManager;
import com.gntsoft.flagmon.setting.OnKeyBackPressedListener;
import com.gntsoft.flagmon.setting.SettingManager;
import com.gntsoft.flagmon.utils.FMLocationFinder;
import com.pluslibrary.utils.PlusClickGuard;


public class MainActivity extends FMCommonActivity {


    private static final int ACTIVITY_LOGIN = 23;
    private int mPressedTabId;
    private MyAlbumManager mMyAlbumManager;
    private FriendManager mFriendManager;
    private NeighborManager mNeighborManager;
    private SettingManager mSettingManager;
    private FMTabManager mSelectedTabManager;
    private boolean mIsFirstRun;
    private OnKeyBackPressedListener mOnKeyBackPressedListener;


    public static String activeTab= FMConstants.DATA_TAB_NEIGHBOR;
    private boolean mIsUpdatingProfile;
    private boolean mIsBurryTreasure;

    public void onResume() {
        super.onResume();

//        if(mIsUpdatingProfile) return;
//
//        if(mIsBurryTreasure) {
//            mIsBurryTreasure = false;
//            return;
//        }
//
//
//        if(mIsFirstRun) {
//            showNeighborMapFragment();
//            mIsFirstRun = false;
//        } else {
//            mSelectedTabManager.chooseFragment();
//        }





    }

    private void showNeighborMapFragment() {
        final Button neighbor = (Button) findViewById(R.id.tab_neighbor);

        //fragment not attached to activity 오류가 발생하여 약간 delay를 줌
        changeTab(neighbor);
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

    public void setBurryTreasureFlag(boolean b) {
        mIsBurryTreasure = b;
    }

    public void setUpdateProfileFlag(boolean isUpdating) {
        mIsUpdatingProfile = isUpdating;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case ACTIVITY_LOGIN:
                refreshActivity();
        }
    }

    public void refreshActivity() {
        mSelectedTabManager.chooseFragment();
    }

    public void setOnKeyBackPressedListener(OnKeyBackPressedListener listener) {
        mOnKeyBackPressedListener = listener;
    }

    public void logIn(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, ACTIVITY_LOGIN);
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

        if(hasPushTaget(getIntent()))
            moveToPushTarget(getIntent());

            //mIsFirstRun = true;
            //setLoginFalse();
            createTabManagers();
            startLocationFinder();
        showNeighborMapFragment();

        //원래 코드
//        if(hasPushTaget()) {
//            moveToPushTarget();
//            return;
//        } else {
//
//            mIsFirstRun = true;
//            //setLoginFalse();
//            createTabManagers();
//            //final Button neighbor = (Button) findViewById(R.id.tab_neighbor);
//
//            //fragment not attached to activity 오류가 발생하여 약간 delay를 줌
//            //changeTab(neighbor);
//        }




    }

    private void startLocationFinder() {
        FMLocationFinder locationFinder = FMLocationFinder.getInstance(this, null);
        locationFinder.getCurrentLocation();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(hasPushTaget(intent))
            moveToPushTarget(intent);
    }

    private void moveToPushTarget(Intent intent) {
        String postIdx = intent.getStringExtra(FMConstants.KEY_POST_IDX);
        String target = intent.getStringExtra(FMConstants.KEY_PUSH_TARGET);


//        private static final String VIEW_POST = "01";
//        private static final String VIEW_REPLY = "02";
//        private static final String USER_PROFILE = "03";
//        private static final String FRIEND_SETTING = "04";
//        private static final String TREASURE_POST = "05";
//        private static final String VIEW_NOTI = "06";
//        private static final String VIEW_ALARM = "07";

        if(target.equals(FMConstants.VIEW_POST)) viewDetailFromPush(postIdx);
        else if(target.equals(FMConstants.VIEW_REPLY)) viewReplyFromPush(postIdx);
        else if(target.equals(FMConstants.USER_PROFILE)) viewUserProfileFromPush(postIdx);
        else if(target.equals(FMConstants.FRIEND_SETTING)) viewFriendSettingFromPush(postIdx);
        else if(target.equals(FMConstants.TREASURE_POST)) viewDetailWithTreasure(postIdx);
        else if(target.equals(FMConstants.VIEW_NOTI)) viewNotiFromPush(postIdx);
        else if(target.equals(FMConstants.VIEW_ALARM)) viewAlarmFromPush(postIdx);
    }

    private void viewAlarmFromPush(String postIdx) {
        mSettingManager.setPushTarget(FMConstants.VIEW_ALARM);
        mSettingManager.chooseFragment();
    }

    private void viewNotiFromPush(String postIdx) {
        mSettingManager.setPushTarget(FMConstants.VIEW_NOTI);
        mSettingManager.chooseFragment();
    }

    private void viewDetailWithTreasure(String postIdx) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(FMConstants.KEY_POST_IDX, postIdx);

        startActivity(intent);
    }

    private void viewFriendSettingFromPush(String postIdx) {
        mSettingManager.setPushTarget(FMConstants.FRIEND_SETTING);
        mSettingManager.chooseFragment();
    }

    private void viewUserProfileFromPush(String postIdx) {
        mSettingManager.setPushTarget(FMConstants.USER_PROFILE);
        mSettingManager.chooseFragment();
    }

    private void viewReplyFromPush(String postIdx) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(FMConstants.KEY_POST_IDX, postIdx);

        startActivity(intent);
    }

    private void viewDetailFromPush(String postIdx) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(FMConstants.KEY_POST_IDX, postIdx);

        startActivity(intent);
    }

    private boolean hasPushTaget(Intent intent) {
        return intent.getStringExtra(FMConstants.KEY_PUSH_TARGET) != null;
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
