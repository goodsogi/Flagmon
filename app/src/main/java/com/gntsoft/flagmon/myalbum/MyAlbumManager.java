package com.gntsoft.flagmon.myalbum;

import android.app.Activity;
import android.os.Bundle;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.login.LoginFragment;
import com.gntsoft.flagmon.util.LoginChecker;

/**
 * Created by johnny on 15. 3. 3.
 */
public class MyAlbumManager {

    private final Activity mActivity;

    public MyAlbumManager(Activity activity) {
        mActivity = activity;
    }

    public void chooseFragment() {
        if (!LoginChecker.doIt(mActivity)) {
            showLogin(FMConstants.TAB_MYALBUM);
        } else if (hasPost()) {
            goToMyAlbumFragment();
        } else showSharePhotoFragment();
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

    private void goToMyAlbumFragment() {
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new MyalbumFragment())
                .commit();
    }

    private boolean hasPost() {
        //!!구현

        return false;
    }

}
