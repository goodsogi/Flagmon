package com.gntsoft.flagmon.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.FMTabManager;
import com.gntsoft.flagmon.friend.FriendManager;
import com.gntsoft.flagmon.myalbum.MyAlbumManager;
import com.gntsoft.flagmon.neighbor.NeighborManager;
import com.gntsoft.flagmon.setting.SettingManager;
import com.gntsoft.flagmon.login.LoginActivity;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.login.SignUpActivity;
import com.pluslibrary.utils.PlusClickGuard;


public class MainActivity extends FMCommonActivity {


 //   private boolean login;
  //  private int itemDatas;
 //   private int mainContentType;
//    String [] mapOptionDatas = {"인기순","최근 등록순"};

//    String [] mapFriendOptionDatas = {"인기순","최근 등록순", "퍼간 날짜"};
//    String [] listFriendOptionDatas = {"인기순","최근 등록순","퍼간 날짜","거리순"};
    private int mPressedTabId;
  //  private int itemDatasFriend;
    private MyAlbumManager mMyAlbumManager;
    private FriendManager mFriendManager;
    private NeighborManager mNeighborManager;
    private SettingManager mSettingManager;
    private FMTabManager mSelectedTabManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLoginFalse();
        createTabManagers();
        Button neighbor = (Button) findViewById(R.id.tab_neighbor);
        changeTab(neighbor);



    }

    private void setLoginFalse() {
        SharedPreferences sharedPreference = getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreference.edit();
        e.putBoolean(FMConstants.KEY_IS_LOGIN, false);
        e.commit();

    }


    public void onResume() {
        super.onResume();
        if(mSelectedTabManager != null) mSelectedTabManager.chooseFragment();
    }

    private void createTabManagers() {
        mNeighborManager = new NeighborManager(this);
        mFriendManager = new FriendManager(this);
        mMyAlbumManager = new MyAlbumManager(this);
        mSettingManager = new SettingManager(this);
    }

//    private void showMainTopBar() {
//        FrameLayout topBarContainer = (FrameLayout) findViewById(R.id.container_top_bar);
//        topBarContainer.removeAllViews();
//
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View topBar = inflater.inflate(R.layout.top_bar_main,null);
//        topBarContainer.addView(topBar);
//    }
//
//    private void setNeighbotSelected() {
//        Button neighbor = (Button) findViewById(R.id.tab_neighbor);
//        neighbor.setSelected(true);
//    }

    public void changeTab(View v) {
        if(mPressedTabId == v.getId()) return;

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
                neighbor.setSelected(true);
                mNeighborManager.chooseFragment();
                mSelectedTabManager = mNeighborManager;
                break;
            case R.id.tab_friend:
                friend.setSelected(true);
                mFriendManager.chooseFragment();
                mSelectedTabManager = mFriendManager;
                break;
            case R.id.tab_myalbum:
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

//    private void showFriendTopBar() {
//        FrameLayout topBarContainer = (FrameLayout) findViewById(R.id.container_top_bar);
//        topBarContainer.removeAllViews();
//
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View inviteTopBar = inflater.inflate(R.layout.top_bar_friend,null);
//        topBarContainer.addView(inviteTopBar);
//    }
//
//    private void inviteFriend() {
//        getFragmentManager().beginTransaction()
//                .replace(R.id.container_main, new InviteFriendFragment())
//                .commit();
//    }
//
//    private boolean hasFriend() {
//        //수정!!
//        return true;
//    }

//    private void showLogin(int tabName) {
//
//        Bundle bundle = new Bundle();
//        bundle.putInt(FMConstants.KEY_TAB_NAME, tabName);
//        LoginFragment fragment = new LoginFragment();
//        fragment.setArguments(bundle);
//        getFragmentManager().beginTransaction()
//                .replace(R.id.container_main, fragment)
//                .commit();
//
//    }



//    private void showNeighbor() {
//        Button menu = (Button) findViewById(R.id.navi_menu);
//        if (menu.isSelected()) {
//            //menu.setSelected(false);
//            showList();
//
//        } else {
//           // menu.setSelected(true);
//            showMap();
//        }
//    }



//    private void showMyalbum() {
//        getFragmentManager().beginTransaction()
//                .replace(R.id.container_main, new MyalbumFragment())
//                .commit();
//    }

//    private void showSetting() {
//        getFragmentManager().beginTransaction()
//                .replace(R.id.container_main, new SettingFragment())
//                .commit();
//    }

//    public void goToFriendManagement(View v) {
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(FMConstants.KEY_IS_GO_TO_FRIEND_MANAGEMENT, true);
//        SettingFragment fragment = new SettingFragment();
//        fragment.setArguments(bundle);
//        getFragmentManager().beginTransaction()
//                .replace(R.id.container_main, fragment)
//                .commit();
//
//    }

//    public void toggleMenu(View v) {
//
//        if (v.isSelected()) {
//            v.setSelected(false);
//            showMap();
//        } else {
//            v.setSelected(true);
//            showList();
//        }
//
//    }

//    public void toggleMenuFriend(View v) {
//
//        if (v.isSelected()) {
//            v.setSelected(false);
//            showMapFriend();
//        } else {
//            v.setSelected(true);
//            showListFriend();
//        }
//
//    }
//
//    private void showListFriend() {
//        mainContentType = FMConstants.CONTENT_LIST;
//        getFragmentManager().beginTransaction()
//                .replace(R.id.container_main, new ListFriendFragment())
//                .commit();
//
//
//    }
//
//    private void showMapFriend() {
//        mainContentType = FMConstants.CONTENT_MAP;
//        getFragmentManager().beginTransaction()
//                .replace(R.id.container_main, new MapFriendFragment())
//                .commit();
//
//    }


//    private void showList() {
//        mainContentType = FMConstants.CONTENT_LIST;
//        getFragmentManager().beginTransaction()
//                .replace(R.id.container_main, new ListNeighborFragment())
//                .commit();
//
//
//    }
//
//    private void showMap() {
//        mainContentType = FMConstants.CONTENT_MAP;
//        getFragmentManager().beginTransaction()
//                .replace(R.id.container_main, new MapNeighborFragment())
//                .commit();
//
//    }

    public void logIn(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

//    public void showSortPopupFriend(View v) {
//        PlusClickGuard.isLogIn(v);
//
//        AlertDialog.Builder ab = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
//        ab.setTitle("정렬방식을 선택해주세요.");
//        ab.setItems(getItemDatasFriend(), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                doSortFriend(whichButton);
//
//            }
//        }).setNegativeButton("닫기",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                    }
//                });
//        ab.show();
//    }

//    private void doSortFriend(int whichButton) {
//        switch (whichButton) {
//            case 0: sortByPopular();
//                break;
//
//            case 1: sortByRecent();
//                break;
//            case 2: sortByPin();
//                break;
//            case 3:sortByDistance();
//                break;
//
//        }
//    }

//    private void sortByPin() {
//        PlusToaster.isLogIn(this,"준비중...");
//        //구현!!
//    }

//    public void showSortPopup(View v) {
//        PlusClickGuard.isLogIn(v);
//
//        AlertDialog.Builder ab = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
//        ab.setTitle("정렬방식을 선택해주세요.");
//        ab.setItems(getItemDatas(), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                doSort(whichButton);
//
//            }
//        }).setNegativeButton("닫기",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                    }
//                });
//        ab.show();
//    }
//
//    private void doSort(int whichButton) {
//
//
//        switch (whichButton) {
//            case 0: sortByPopular();
//                break;
//
//            case 1: sortByRecent();
//                break;
//
//            case 2:sortByDistance();
//                break;
//
//        }
//    }
//
//    private void sortByDistance() {
//        PlusToaster.isLogIn(this,"준비중...");
//        //구현!!
//    }
//
//    private void sortByRecent() {
//        PlusToaster.isLogIn(this,"준비중...");
//        //구현!!
//    }
//
//    private void sortByPopular() {
//        PlusToaster.isLogIn(this,"준비중...");
//        //구현!!
//    }




    public void signUp(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

//    public void goToChooseFriend(View v) {
//        PlusClickGuard.isLogIn(v);
//
//        Intent intent = new Intent(this, ChooseFriendActivity.class);
//        startActivity(intent);
//    }




//    public String[] getItemDatas()
//    {
//
//
//        return mainContentType == FMConstants.CONTENT_MAP? mapOptionDatas:listOptionDatas;
//    }

//    public String[] getItemDatasFriend() {
//        return mainContentType == FMConstants.CONTENT_MAP? mapOptionDatas:listOptionDatas;
//    }
}
