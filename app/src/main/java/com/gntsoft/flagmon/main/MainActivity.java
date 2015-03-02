package com.gntsoft.flagmon.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.login.LoginActivity;
import com.gntsoft.flagmon.login.LoginFragment;
import com.gntsoft.flagmon.myalbum.MyalbumFragment;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.search.SearchActivity;
import com.gntsoft.flagmon.setting.SettingFragment;
import com.gntsoft.flagmon.login.SignUpActivity;
import com.gntsoft.flagmon.friend.FriendFragment;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;


public class MainActivity extends FragmentActivity {


    private boolean login;
    private int itemDatas;
    private int mainContentType;
    String [] mapOptionDatas = {"인기순","최근 등록순"};
    String [] listOptionDatas = {"인기순","최근 등록순","거리순"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showMainTopBar();
        showMap();
        setNeighbotSelected();

    }

    private void showMainTopBar() {
        FrameLayout topBarContainer = (FrameLayout) findViewById(R.id.container_top_bar);
        topBarContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(this);
        View topBar = inflater.inflate(R.layout.top_bar_main,null);
        topBarContainer.addView(topBar);
    }

    private void setNeighbotSelected() {
        Button neighbor = (Button) findViewById(R.id.tab_neighbor);
        neighbor.setSelected(true);
    }

    public void changeTab(View v) {
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
                showMainTopBar();
                showNeighbor();
                break;
            case R.id.tab_friend:
                friend.setSelected(true);
                if(isLogin()) showFriend(); else showLogin(FMConstants.TAB_FRIEND);
                break;
            case R.id.tab_myalbum:
                myalbum.setSelected(true);
                if(isLogin()) showMyalbum(); else showLogin(FMConstants.TAB_MYALBUM);
                break;
            case R.id.tab_setting:
                setting.setSelected(true);
                if(isLogin()) showSetting(); else showLogin(FMConstants.TAB_SETTING);
                break;


        }


    }

    private void showLogin(int tabName) {

        Bundle bundle = new Bundle();
        bundle.putInt(FMConstants.KEY_TAB_NAME, tabName);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.container_main, fragment)
                .commit();

    }



    private void showNeighbor() {
        Button menu = (Button) findViewById(R.id.navi_menu);
        if (menu.isSelected()) {
            //menu.setSelected(false);
            showList();

        } else {
           // menu.setSelected(true);
            showMap();
        }
    }

    private void showFriend() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new FriendFragment())
                .commit();
    }

    private void showMyalbum() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new MyalbumFragment())
                .commit();
    }

    private void showSetting() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new SettingFragment())
                .commit();
    }

    public void toggleMenu(View v) {

        if (v.isSelected()) {
            v.setSelected(false);
            showMap();
        } else {
            v.setSelected(true);
            showList();
        }

    }

    private void showList() {
        mainContentType = FMConstants.CONTENT_LIST;
        getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new MainListFragment())
                .commit();


    }

    private void showMap() {
        mainContentType = FMConstants.CONTENT_MAP;
        getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new MapFragment())
                .commit();

    }

    public void logIn(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void showSortPopup(View v) {
        PlusClickGuard.doIt(v);

        AlertDialog.Builder ab = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("정렬방식을 선택해주세요.");
        ab.setItems(getItemDatas(), new DialogInterface.OnClickListener() {
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

    private void doSort(int whichButton) {


        switch (whichButton) {
            case 0: sortByPopular();
                break;

            case 1: sortByRecent();
                break;

            case 2:sortByDistance();
                break;

        }
    }

    private void sortByDistance() {
        PlusToaster.doIt(this,"준비중...");
        //구현!!
    }

    private void sortByRecent() {
        PlusToaster.doIt(this,"준비중...");
        //구현!!
    }

    private void sortByPopular() {
        PlusToaster.doIt(this,"준비중...");
        //구현!!
    }


    public void goToSearch(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(FMConstants.KEY_MAIN_CONTENT_TYPE,mainContentType);
        startActivity(intent);
    }

    public void signUp(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


    public boolean isLogin() {

        SharedPreferences sharedPreference = getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getBoolean(FMConstants.KEY_IS_LOGIN, false);
    }

    public String[] getItemDatas() {


        return mainContentType == FMConstants.CONTENT_MAP? mapOptionDatas:listOptionDatas;
    }
}
