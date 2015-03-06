package com.gntsoft.flagmon;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 공통 fragment
 * 
 * @author jeff
 * 
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
		// 리스너 등록
		addListenerButton();
        initUIL();
		
	}

    private void initUIL() {
        mImageLoader = ImageLoader.getInstance();

        mOption = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(com.pluslibrary.R.drawable.empty_photo)
                .showImageOnFail(com.pluslibrary.R.drawable.empty_photo).cacheInMemory(true)
                .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    /**
	 * 리스너 등록
	 */
	abstract protected void addListenerButton();

	

}
