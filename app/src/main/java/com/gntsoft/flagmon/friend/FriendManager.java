package com.gntsoft.flagmon.friend;

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
public class FriendManager {

    private final Activity mActivity;

    public FriendManager(Activity activity) {
        mActivity = activity;
    }

    public void chooseFragment() {
        if (!LoginChecker.isLogIn(mActivity)) {
            showLogin(FMConstants.TAB_FRIEND);
        } else if (hasFriend()) {
            showFriendTopBar();
            addButtonListener();
            showMapFriend();
        } else inviteFriend();
    }

    private void addButtonListener() {
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
                goToChooseFriend(v);
            }
        });
    }

    private void goToChooseFriend(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(mActivity, ChooseFriendActivity.class);
        mActivity.startActivity(intent);
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
        View inviteTopBar = inflater.inflate(R.layout.top_bar_friend,null);
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

    private boolean hasFriend() {
        //!!구현

        return false;
    }

}
