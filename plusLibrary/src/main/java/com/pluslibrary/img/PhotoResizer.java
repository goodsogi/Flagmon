package com.pluslibrary.img;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * 이미지 크기 축소
 * 
 * @author user
 * 
 */
public class PhotoResizer {

	private Activity mActivity;
	private int IMAGE_ALLOW_WIDTH = 0;
	private int IMAGE_ALLOW_HEIGHT = 0;
	private int mSrcWidth;
	private int mSrcHeight;
	private static int sFileCount;

	private static final String PHOTO_RESIZE_FILENAME = "resized_photo.jpg";

	public PhotoResizer(Activity activity) {
		mActivity = activity;
		// 축소할 크기 지정
		setResizeImageSize();
	}
	
	/**
	 * ImageView 크기에 맞춘 비트맵 가져오기
	 * 
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeFileForPhotoSize(String imgUri, int reqWidth,
			int reqHeight) {
		// 이미지 크기만 알아내기 위해 inJustDecodeBounds를 true로 지정
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgUri, options);

		// SampleSize 계산 
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// inSampleSize 속성을 사용하여 이미지 디코딩 
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(imgUri, options);

	}

	/**
	 * Sample Size 계산
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 원본 이미지의 높이와 넓이
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// 2의 배수인 Sample Size 계산. 높이와 넓이가 이미지뷰의 높이와 넓이보다 큰 값이 되게 함
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}


	/**
	 * 축소할 크기 지정
	 * 
	 * @return
	 */
	private void setResizeImageSize() {
		// 수정 필요!!
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(mActivity);

		int size = Integer.parseInt(sharedPref.getString(
				PlusImageConstants.KEY_PREF_IMAGE_SIZE, "0"));
		switch (size) {
		case 0:
			IMAGE_ALLOW_WIDTH = 1200;
			IMAGE_ALLOW_HEIGHT = 1200;
			break;

		case 1:
			IMAGE_ALLOW_WIDTH = 800;
			IMAGE_ALLOW_HEIGHT = 600;
			break;
		case 2:
			IMAGE_ALLOW_WIDTH = 320;
			IMAGE_ALLOW_HEIGHT = 480;
			break;
		}
	}

	/**
	 * 이미지 축소
	 * 
	 * @param imgUrl
	 * @return
	 */
	public String work(String imgUrl) {

		Bitmap bitmap = null;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgUrl, options);

		mSrcWidth = options.outWidth;
		mSrcHeight = options.outHeight;

		Log.d("image", "original width: " + mSrcWidth + " height: "
				+ mSrcHeight);

		if (mSrcWidth * mSrcHeight > IMAGE_ALLOW_WIDTH * IMAGE_ALLOW_HEIGHT) {

			int scaleSize = (mSrcWidth * mSrcHeight)
					/ (IMAGE_ALLOW_WIDTH * IMAGE_ALLOW_HEIGHT);

			options = new BitmapFactory.Options();
			// SampleSize 계산
			// options.inSampleSize = getSampleSize(scaleSize);

			// Option을 적용한 이미지 가져오기
			// bitmap = BitmapFactory.decodeFile(imgUrl, options);
			int sampleSize = getSampleSize(scaleSize);
			boolean done = false;
			while (!done) {
				options.inSampleSize = sampleSize++;
				try {
					bitmap = BitmapFactory.decodeFile(imgUrl, options);
					done = true;
				} catch (OutOfMemoryError e) {
					// Ignore. Try again.
				}
			}

			return createNewSizeBitmap(bitmap);
		}

		return imgUrl;

	}

	/**
	 * 이미지 크기 알아내기
	 * 
	 * @param uri
	 * @return
	 */
	public boolean checkSize(Uri uri) {
		IMAGE_ALLOW_WIDTH = 1200;
		IMAGE_ALLOW_HEIGHT = 1200;

		InputStream is = null;

		try {
			is = mActivity.getContentResolver().openInputStream(uri);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeStream(is, null, options);

		mSrcWidth = options.outWidth;
		mSrcHeight = options.outHeight;

		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (mSrcWidth * mSrcHeight > IMAGE_ALLOW_WIDTH * IMAGE_ALLOW_HEIGHT) ? true
				: false;
	}

	/**
	 * 피카사 이미지 처리
	 * 
	 * @param imgUrl
	 * @return
	 */
	public String workPiccasa(Uri uri) {

		int scaleSize = (mSrcWidth * mSrcHeight)
				/ (IMAGE_ALLOW_WIDTH * IMAGE_ALLOW_HEIGHT);

		int sampleSize = getSampleSize(scaleSize);

		InputStream is = null;
		try {
			is = mActivity.getContentResolver().openInputStream(uri);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		Bitmap bitmap = null;

		boolean done = false;
		while (!done) {
			options.inSampleSize = sampleSize++;
			try {
				bitmap = BitmapFactory.decodeStream(is, null, options);
				done = true;
			} catch (OutOfMemoryError e) {
				// Ignore. Try again.
			}
		}
		return createNewSizeBitmap(bitmap);

	}

	/**
	 * 새로운 크기의 비트맵 이미지 생성
	 * 
	 * @param bitmap
	 * @return
	 */
	private String createNewSizeBitmap(Bitmap bitmap) {
		if (bitmap != null) {

			// Get new width and height
			Rect resizedRect1 = getRatioSize(bitmap.getWidth(),
					bitmap.getHeight(), IMAGE_ALLOW_WIDTH, IMAGE_ALLOW_HEIGHT);
			Rect resizedRect2 = getRatioSize(bitmap.getWidth(),
					bitmap.getHeight(), IMAGE_ALLOW_HEIGHT, IMAGE_ALLOW_WIDTH);

			int newWidth = getNewWidth(bitmap, resizedRect1, resizedRect2);
			int newHeight = getNewHeight(bitmap, resizedRect1, resizedRect2);

			Log.d("resizedImage", "new width: " + newWidth + " new height: "
					+ newHeight);

			if (newWidth > 0 && newHeight > 0) {
				Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, newWidth,
						newHeight, true);
				// Save bitmap to file
				return SaveBitmapToFile(newBitmap);

			}
			bitmap.recycle();
		} else {
		}
		return null;

	}

	/**
	 * 새 파일 생성
	 * 
	 * @return
	 */
	public File createNewFile() {

		File dir = new File(mActivity.getFilesDir() + File.separator
				+ "plusImg" + File.separator + "img");
		dir.mkdirs(); // create folders where write files
		File resizedFile = new File(dir, "my_photo" + (sFileCount++) + ".jpg");

		try {
			resizedFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resizedFile;
	}

	/**
	 * 비트맵을 파일에 저장
	 * 
	 * @param bitmap
	 * @return
	 */
	private String SaveBitmapToFile(Bitmap bitmap) {

		File resizedFile = createNewFile();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(resizedFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);

		// 파일의 절대 경로 반환
		return resizedFile.getAbsolutePath();

	}

	// 비트맵의 새 높이 알아내기
	private int getNewHeight(Bitmap bitmap, Rect resizedRect1, Rect resizedRect2) {
		int newHeight = 0;

		if (resizedRect1.width() * resizedRect1.height() > resizedRect2.width()
				* resizedRect2.height()) {
			newHeight = resizedRect1.height();
		} else {
			newHeight = resizedRect2.height();
		}
		return newHeight;
	}

	// 비트맵의 새 넓이 알아내기
	private int getNewWidth(Bitmap bitmap, Rect resizedRect1, Rect resizedRect2) {
		int newWidth = 0;

		if (resizedRect1.width() * resizedRect1.height() > resizedRect2.width()
				* resizedRect2.height()) {
			newWidth = resizedRect1.width();
		} else {
			newWidth = resizedRect2.width();
		}
		return newWidth;
	}

	/**
	 * Sample Size 계산
	 * 
	 * @return
	 */
	private int getSampleSize(int scaleSize) {
		int sampleSize = 1;
		switch (scaleSize) {
		case 1:
			sampleSize = 1;
			break;
		case 2:
		case 3:
			sampleSize = 1;
			break;
		case 4:
		case 5:
		case 6:
		case 7:
			sampleSize = 2;
			break;
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
			sampleSize = 4;
			break;
		default:
			sampleSize = 8;
			break;
		}
		return sampleSize;
	}

	/**
	 * 캐시 폴더 생성
	 */
	public void createCacheFolder() {
		if (isAvailableExternalMemory() == false)
			return;

		File file = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Android");
		if (file.exists() == false) {
			if (file.mkdir() == false) {
				// mIsCreatedCacheFolder = false;
				// return;
			}
		}
		file = new File(file.getAbsolutePath() + "/data");
		if (file.exists() == false) {
			if (file.mkdir() == false) {
				// mIsCreatedCacheFolder = false;
				// return;
			}
		}
		file = new File(file.getAbsolutePath() + "/"
				+ mActivity.getPackageName());
		if (file.exists() == false) {
			if (file.mkdir() == false) {
				// mIsCreatedCacheFolder = false;
				// return;
			}
		}
		File cachefile = new File(file.getAbsolutePath() + "/cache");
		if (cachefile.exists() == false) {
			if (cachefile.mkdir() == false) {
				// mIsCreatedCacheFolder = false;
				// return;
			}
		}
		File imageFile = new File(file.getAbsoluteFile() + "/images");
		if (imageFile.exists() == false) {
			imageFile.mkdir();
		}
		// mIsCreatedCacheFolder = true;
	}

	/**
	 * Ratio 계산
	 * 
	 * @param imageWidth
	 * @param imageHeight
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static Rect getRatioSize(int imageWidth, int imageHeight,
			int maxWidth, int maxHeight) {
		int newWidth = 0;
		int newHeight = 0;
		float fRatioW = (float) maxWidth / (float) imageWidth;
		float fRatioH = (float) maxHeight / (float) imageHeight;

		if (fRatioW > fRatioH) {
			newHeight = maxHeight;
			newWidth = (int) (fRatioH * imageWidth);
		} else {
			newWidth = maxWidth;
			newHeight = (int) (fRatioW * imageHeight);
		}
		return new Rect(0, 0, newWidth, newHeight);
	}

	/**
	 * 외부 이미지 폴더 가져오기
	 * 
	 * @return
	 */
	public File getExternalImagesFolder() {
		// if (mIsCreatedCacheFolder == false)
		// return null;
		if (isAvailableExternalMemory() == false)
			return null;

		File folder = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/Android"
				+ "/data"
				+ "/"
				+ mActivity.getPackageName() + "/images");
		return folder;
	}

	/**
	 * 외부 저장 매제를 사용할 수 있는지 체크
	 * 
	 * @return
	 */
	public boolean isAvailableExternalMemory() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// 매제에 읽고 쓰기 가능
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// 읽기만 가능
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// 매체 사용 불가능
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		return (mExternalStorageAvailable == true && mExternalStorageWriteable == true);
	}

}
