package com.gntsoft.flagmon.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.login.LoginFragment;
import com.gntsoft.flagmon.utils.LoginChecker;

/**
 * Created by johnny on 15. 3. 3.
 */
public class SettingManager {

    private final Activity mActivity;

    public SettingManager(Activity activity) {
        mActivity = activity;
    }

    public void chooseFragment() {
        if (!LoginChecker.isLogIn(mActivity)) {
            showLogin(FMConstants.TAB_SETTING);
        } else {

            showSettingTopBar(); //수정!!
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




    private void showSettingTopBar() {
        FrameLayout topBarContainer = (FrameLayout) mActivity.findViewById(R.id.container_top_bar);
        topBarContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View settingBar = inflater.inflate(R.layout.top_bar_setting, null);
        topBarContainer.addView(settingBar);
    }


}
