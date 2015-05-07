package com.gntsoft.flagmon.myalbum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.FMTabManager;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.login.LoginFragment;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FMListParser;
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 3. 3.
 */
public class MyAlbumManager implements FMTabManager, PlusOnGetDataListener {

    private static final int GET_LIST_DATA = 0;
    private final Activity mActivity;

    public MyAlbumManager(Activity activity) {
        mActivity = activity;
    }

    public void chooseFragment() {
        if (!LoginChecker.isLogIn(mActivity))
            showLogin(FMConstants.TAB_MYALBUM);
        else checkIfHasPost();


    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null) return;
        switch (from) {
            case GET_LIST_DATA:
                handleHasPost(new FMListParser().doIt((String) datas));
                break;
        }

    }

    protected String getUserAuthKey() {
        SharedPreferences sharedPreference = mActivity.getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getString(FMConstants.KEY_USER_AUTH_KEY, "");
    }

    private void addListenerToButton() {
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
                launchPostChoosePhotoActivity(v);
            }
        });

        Button groupPost = (Button) mActivity.findViewById(R.id.groupPost);
        groupPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGroupPostActivity(v);
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
                .replace(R.id.container_main, new MapFragment())
                .commit();
    }

    private void hideGroupPostButton() {
        Button groupPost = (Button) mActivity.findViewById(R.id.groupPost);
        groupPost.setVisibility(View.GONE);
    }

    private void showListMyAlbum() {
        showGroupPostButton();
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new ListFragment())
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
        View myAlbumTopBar = inflater.inflate(R.layout.top_bar_my_album, null);
        topBarContainer.addView(myAlbumTopBar);
    }

    private void showSharePhotoFragment() {
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new SharePhotoFragment())
                .commit();
    }

    private void showLogin(int tabName) {

        Bundle bundle = new Bundle();
        bundle.putInt(FMConstants.KEY_TARGET, tabName);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(bundle);
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, fragment)
                .commit();

    }

    private void launchPostChoosePhotoActivity(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(mActivity, PostChoosePhotoActivity.class);
        mActivity.startActivity(intent);

    }

    private void launchGroupPostActivity(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(mActivity, GroupPostActivity.class);
        mActivity.startActivity(intent);

    }

    private boolean checkIfHasPost() {

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_MYALBUM));
        if (LoginChecker.isLogIn(mActivity)) {
            postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        }


        new PlusHttpClient(mActivity, this, false).execute(GET_LIST_DATA,
                FMApiConstants.GET_LIST_DATA, new PlusInputStreamStringConverter(),
                postParams);

        return false;
    }

    private void handleHasPost(ArrayList<FMModel> fmModels) {
        if (fmModels != null && fmModels.size() > 0) {
            showMapMyAlbumFragment();
        } else {
            showSharePhotoFragment();
        }
    }

    private void showMapMyAlbumFragment() {
        showMyAlbumTopBar();
        addListenerToButton();
        showMapMyAlbum();
    }

}
