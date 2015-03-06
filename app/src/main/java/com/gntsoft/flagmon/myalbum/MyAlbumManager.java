package com.gntsoft.flagmon.myalbum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.login.LoginFragment;
import com.gntsoft.flagmon.util.LoginChecker;
import com.pluslibrary.utils.PlusClickGuard;

/**
 * Created by johnny on 15. 3. 3.
 */
public class MyAlbumManager {

    private final Activity mActivity;

    public MyAlbumManager(Activity activity) {
        mActivity = activity;
    }

    public void chooseFragment() {
        if (!LoginChecker.isLogIn(mActivity)) {
            showLogin(FMConstants.TAB_MYALBUM);
        } else if (hasPost()) {
            showMyAlbumTopBar();
            addButtonListener();
            showMapMyAlbum();
        } else showSharePhotoFragment();
    }

    private void addButtonListener() {
        Button naviMenu = (Button) mActivity.findViewById(R.id.naviMenu);
        naviMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu(v);
            }
        });

        Button doPost = (Button) mActivity.findViewById(R.id.doPost);
        doPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPost(v);
            }
        });

        Button groupPost = (Button) mActivity.findViewById(R.id.groupPost);
        doPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGroupPost(v);
            }
        });
    }

    private void toggleMenu(View v) {
        if (v.isSelected()) {
            v.setSelected(false);
            showMapMyAlbum();
        } else {
            v.setSelected(true);
            showListMyAlbum();
        }
    }

    private void showMapMyAlbum() {
        hideGroupPostButton();
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new MapMyAlbumFragment())
                .commit();
    }

    private void hideGroupPostButton() {
        Button groupPost = (Button) mActivity.findViewById(R.id.groupPost);
        groupPost.setVisibility(View.GONE);
    }

    private void showListMyAlbum() {
        showGroupPostButton();
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new MapMyAlbumFragment())
                .commit();


    }

    private void showGroupPostButton() {
        Button groupPost = (Button) mActivity.findViewById(R.id.groupPost);
        groupPost.setVisibility(View.VISIBLE);
    }

    private void showMyAlbumTopBar() {
        FrameLayout topBarContainer = (FrameLayout) mActivity.findViewById(R.id.container_top_bar);
        topBarContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View myAlbumTopBar = inflater.inflate(R.layout.top_bar_my_album,null);
        topBarContainer.addView(myAlbumTopBar);
    }


    private void showSharePhotoFragment() {
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new SharePhotoFragment())
                .commit();
    }

    private void showLogin(int tabName) {

        Bundle bundle = new Bundle();
        bundle.putInt(FMConstants.KEY_TAB_NAME, tabName);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(bundle);
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, fragment)
                .commit();

    }

    private void goToPost(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(mActivity, PostActivity.class);
        mActivity.startActivity(intent);

    }

    private void goToGroupPost(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(mActivity, GroupPostActivity.class);
        mActivity.startActivity(intent);

    }

    private boolean hasPost() {
        //!!구현

        return false;
    }

}
