package com.pluslibrary.common;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.pluslibrary.R;

/**
 * 공통 어댑터
 * 
 * @author jeff
 * 
 * @param <T>
 */
public class CommonAdapter<T> extends ArrayAdapter<T> {
	protected Context mContext;
	protected LayoutInflater mLayoutInflater;
	protected ArrayList<T> mDatas;
	// 이미지 다운로드
	protected DisplayImageOptions mOptionRound;
	protected ImageLoader mImageLoader;
	protected DisplayImageOptions mOption;
	

	public CommonAdapter(Context context, int layoutId, ArrayList<T> datas) {
		super(context, layoutId, datas);
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
		mDatas = datas;
		// UIL 초기화
		mImageLoader = ImageLoader.getInstance();
		mOptionRound = new DisplayImageOptions.Builder()
				.displayer(new RoundedBitmapDisplayer(15))
				.showImageForEmptyUri(R.drawable.empty_photo)
				.showImageOnFail(R.drawable.empty_photo).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		mOption = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.empty_photo)
				.showImageOnFail(R.drawable.empty_photo).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		
		
		
	}

	
}
