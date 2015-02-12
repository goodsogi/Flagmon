package com.pluslibrary.img;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

/**
 * 비트맵을 파일로 변환 
 * 사용법: PlusBitmapFileSaver.doIt(bitmap,
 * Environment.getExternalStorageDirectory() + "/BlackTime/", "sns_img.jpg");
 * 
 * @author jeff
 * 
 */
public class PlusBitmapFileSaver {
	/**
	 * 저장
	 * 
	 * @param uri
	 * @return
	 */
	public static String doIt(Bitmap bitmap, String strFilePath, String filename) {
		File file = new File(strFilePath);

		// If no folders
		if (!file.exists()) {
			file.mkdirs();
			// Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
		}

		File fileCacheItem = new File(strFilePath + filename);
		OutputStream out = null;

		try {
			fileCacheItem.createNewFile();
			out = new FileOutputStream(fileCacheItem);

			bitmap.compress(CompressFormat.JPEG, 100, out);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return strFilePath + filename;
	}
}
