package com.gntsoft.flagmon;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 공통 fragment
 *
 * @author jeff
 */
public abstract class FMCommonFragment extends Fragment {
    protected Activity mActivity;
    protected ImageLoader mImageLoader;
    protected DisplayImageOptions mOption;

    public FMCommonFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();
        initUIL();
        // 리스너 등록
        addListenerToButton();


    }

    /**
     * 리스너 등록
     */
    abstract protected void addListenerToButton();

    protected String getUserAuthKey() {
        SharedPreferences sharedPreference = mActivity.getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getString(FMConstants.KEY_USER_AUTH_KEY, "");
    }

    private void initUIL() {
        mImageLoader = ImageLoader.getInstance();

        mOption = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(com.pluslibrary.R.drawable.empty_photo)
                .showImageOnFail(com.pluslibrary.R.drawable.empty_photo).cacheInMemory(true)
                .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
    }

}
