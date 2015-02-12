package com.pluslibrary.img;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * 피카사 이미지를 저장 및 표시
 * 
 * @author jeff
 * 
 */
public class PicassaImageSaveShowTask extends AsyncTask<Object, Void, String> {

	private ProgressBar mProgressBar;
	private ImageView mImageView;

	@Override
	protected String doInBackground(Object... params) {
		Activity activity = (Activity) params[0];
		mImageView = (ImageView) params[1];
		mProgressBar = (ProgressBar) params[2];
		Uri imgUri = (Uri) params[3];
		PiccasaImageSaver saver = new PiccasaImageSaver(activity);
		return saver.doIt(imgUri);

	}

	protected void onPostExecute(String imagePath) {
		// progress bar 제거
		mProgressBar.setVisibility(View.GONE);
		// 이미지 표시
		Bitmap bm = PhotoResizer.decodeFileForPhotoSize(imagePath,
				mImageView.getWidth(), mImageView.getHeight());
		mImageView.setImageBitmap(bm);
		mImageView.setTag(imagePath);
	}

}
