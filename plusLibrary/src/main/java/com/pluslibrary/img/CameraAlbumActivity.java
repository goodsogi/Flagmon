package com.pluslibrary.img;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

/**
 * 카메라, 앨범에서 이미지 가져오기
 * 
 * @author user
 * 
 */
public class CameraAlbumActivity extends Activity {

	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;

	private Uri mImageUri;
	private String mFullPath;
	private String mCurrentPhotoPath;
	private static final String TYPE_IMAGE = "image/*";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 이미지를 가져올 소스 체크(앨범, 카메라)
		Intent intent = getIntent();
		String type = intent.getStringExtra(PlusImageConstants.KEY_IMAGE_SOURCE);
		if (type.equals(PlusImageConstants.SOURCE_CAMERA))
			doTakePhotoAction();
		else
			doTakeAlbumAction();
	}

	/**
	 * 카메라 촬영
	 */
	public void doTakePhotoAction() {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 촬영 가능한지 체크
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// 이미지를 저장할 파일 생성
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
			}
			// 파일이 정상적으로 생성되면 아래 처리
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
			}
		}

	}

	/**
	 * 앨범에서 이미지 가져오기
	 */
	public void doTakeAlbumAction() {

		Intent intent = new Intent();
		intent.setType(TYPE_IMAGE);
		intent.setAction(Intent.ACTION_PICK);
		startActivityForResult(intent, PICK_FROM_ALBUM);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			finish();
			return;
		}

		switch (requestCode) {
		case PICK_FROM_ALBUM: {
			mImageUri = data.getData();
			mFullPath = mImageUri.toString();

			break;

		}

		case PICK_FROM_CAMERA: {

			// 카메라로 찍은 사진을 앨범에 추가
			galleryAddPic();

			// mFullPath = mImageUri.getPath();
			mFullPath = mCurrentPhotoPath;
			Log.d("image", mFullPath);

			break;
		}

		}

		 Intent data2 = new Intent();
		
		 data2.putExtra(PlusImageConstants.EXTRA_IMAGE_PATH, mFullPath);
		 setResult(RESULT_OK, data2);
		 finish();

	}

	/**
	 * 이미지를 저장할 임시 파일 생성
	 * 
	 * @return
	 * @throws IOException
	 */
	private File createImageFile() throws IOException {
		// 파일 이름 생성
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// 인텐트에 사용할 path
		mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}

	/**
	 * 카메라로 찍은 사진을 앨범에 추가
	 */
	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		// 이미지 파일 경로에 "file:" 추가
		File f = new File("file:" + mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

}
