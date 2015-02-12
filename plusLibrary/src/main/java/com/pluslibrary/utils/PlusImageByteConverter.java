package com.pluslibrary.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.entity.mime.content.ByteArrayBody;

import com.pluslibrary.PlusConstants;

/**
 * 이미지 파일을 byte array로 변환
 * 
 * @author jeff
 * 
 */
public class PlusImageByteConverter {

	public static ByteArrayBody doIt(String fileUri) {

		ByteArrayBody imgBody = null;
		try {
			// 파일명 추출
			String[] tokens = fileUri.split("/");
			String fileName = tokens[tokens.length - 1];
			imgBody = new ByteArrayBody(getByte(fileUri),
					PlusConstants.SERVER_IMAGE_TYPE, fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return imgBody;

	}

	private static byte[] getByte(String fileUri) throws IOException {

		// this dynamically extends to take the bytes you read
		File file = new File(fileUri);
		InputStream in = null;
		ByteArrayOutputStream byteBuffer = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));

			byteBuffer = new ByteArrayOutputStream();

			// this is storage overwritten on each iteration with bytes
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];

			// we need to know how may bytes were read to write them to the
			// byteBuffer
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				byteBuffer.write(buffer, 0, len);
			}
		} catch (Exception e) {
		}

		// and then we can return your byte array.
		return byteBuffer.toByteArray();
	}

}
