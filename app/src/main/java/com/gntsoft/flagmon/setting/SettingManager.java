package com.gntsoft.flagmon.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.login.LoginFragment;
import com.gntsoft.flagmon.util.LoginChecker;

/**
 * Created by johnny on 15. 3. 3.
 */
public class SettingManager {

    private final Activity mActivity;

    public SettingManager(Activity activity) {
        mActivity = activity;
    }

    public void chooseFragment() {
        if (!LoginChecker.doIt(mActivity)) {
            showLogin(FMConstants.TAB_SETTING);
        } else {

            showMainTopBar(); //수정!!
            addButtonListener();
            showSettingFragment();
        }
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

    private void addButtonListener() {
        Button naviMenu = (Button) mActivity.findViewById(R.id.naviMenu);
        naviMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu(v);
            }
        });
    }

    private void toggleMenu(View v) {
//        if (v.isSelected()) {
//            v.setSelected(false);
//            showMap();
//        } else {
//            v.setSelected(true);
//            showList();
//        }
    }

    private void showSettingFragment() {
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new SettingFragment())
                .commit();
    }




    private void showMainTopBar() {
        FrameLayout topBarContainer = (FrameLayout) mActivity.findViewById(R.id.container_top_bar);
        topBarContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View inviteTopBar = inflater.inflate(R.layout.top_bar_neighbor, null);
        topBarContainer.addView(inviteTopBar);
    }


}
