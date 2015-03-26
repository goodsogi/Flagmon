package com.gntsoft.flagmon.friend;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.FMTabManager;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.login.LoginFragment;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FriendListParser;
import com.gntsoft.flagmon.server.FriendModel;
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
public class FriendManager implements FMTabManager, PlusOnGetDataListener {

    private static final int CHECK_IF_HAS_FRIEND = 0;
    private final Activity mActivity;

    public FriendManager(Activity activity) {
        mActivity = activity;
    }

    public void chooseFragment() {
        if (!LoginChecker.isLogIn(mActivity)) {
            showLogin(FMConstants.TAB_FRIEND);
        } else checkIfHasFriend();
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null) {
            inviteFriend(); //나중에 삭제!!
            return;
        }
        switch (from) {
            case CHECK_IF_HAS_FRIEND:
                handleIfHasFriend(new FriendListParser().doIt((String) datas));
                break;
        }

    }

    private void addListenerToButton() {
        Button naviMenu = (Button) mActivity.findViewById(R.id.naviMenu);
        naviMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu(v);
            }
        });

        Button chooseFriend = (Button) mActivity.findViewById(R.id.chooseFriend);
        chooseFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFriendListFragment(v);
            }
        });
    }

    private void launchFriendListFragment(View v) {
        PlusClickGuard.doIt(v);

        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new FriendListFragment())
                .commit();
    }

    private void toggleMenu(View v) {
        if (v.isSelected()) {
            v.setSelected(false);
            showMapFriend();
        } else {
            v.setSelected(true);
            showListFriend();
        }
    }

    private void showMapFriend() {
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new MapFriendFragment())
                .commit();
    }

    private void showListFriend() {
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new ListFriendFragment())
                .commit();


    }

    private void showFriendTopBar() {
        FrameLayout topBarContainer = (FrameLayout) mActivity.findViewById(R.id.container_top_bar);
        topBarContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View inviteTopBar = inflater.inflate(R.layout.top_bar_friend, null);
        topBarContainer.addView(inviteTopBar);
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

    private void inviteFriend() {
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new InviteFriendFragment())
                .commit();
    }

    private void checkIfHasFriend() {
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("key", ((FMCommonActivity) mActivity).getUserAuthKey()));


        new PlusHttpClient(mActivity, this, false).execute(CHECK_IF_HAS_FRIEND,
                FMApiConstants.CHECK_IF_HAS_FRIEND, new PlusInputStreamStringConverter(),
                postParams);
    }

    private void handleIfHasFriend(ArrayList<FriendModel> friendModels) {

        if (friendModels.size() > 0) {
            showFriendTopBar();
            addListenerToButton();
            showMapFriend();
        } else {
            inviteFriend();
        }
    }

}
