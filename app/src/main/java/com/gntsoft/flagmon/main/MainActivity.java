package com.gntsoft.flagmon.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gntsoft.flagmon.login.LoginActivity;
import com.gntsoft.flagmon.myalbum.MyalbumFragment;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.setting.SettingFragment;
import com.gntsoft.flagmon.login.SignUpActivity;
import com.gntsoft.flagmon.friend.FriendFragment;
import com.pluslibrary.utils.PlusClickGuard;


public class MainActivity extends FragmentActivity {


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
                showInviteTopBar();
                showFriend();
                break;
            case R.id.tab_myalbum:
                myalbum.setSelected(true);
                showInviteTopBar();
                showMyalbum();
                break;
            case R.id.tab_setting:
                setting.setSelected(true);
                showInviteTopBar();
                showSetting();
                break;


        }


    }

    private void showInviteTopBar() {
        FrameLayout topBarContainer = (FrameLayout) findViewById(R.id.container_top_bar);
        topBarContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(this);
        View inviteTopBar = inflater.inflate(R.layout.top_bar_invite,null);
        topBarContainer.addView(inviteTopBar);
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

        getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new MainListFragment())
                .commit();


    }

    private void showMap() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new MapFragment())
                .commit();

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


}
