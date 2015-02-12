package com.pluslibrary.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;

public class PlusFileWriter2 {

	private File mFolder;
	private final String FOLDER_NAME = "PlusBikeNavi";
	private final String FILE_NAME = "log.txt";

	public PlusFileWriter2() {
		mFolder = new File(Environment.getExternalStorageDirectory() + "/"
				+ FOLDER_NAME);
		if (!mFolder.exists()) {
			mFolder.mkdirs();
		}
	}

	public void doIt(String message) {

		// file을 file.createNewFile()로 생성하면 fileNotFoundException 발생
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(
					mFolder.getAbsolutePath() + "/" + FILE_NAME, false));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			writer.write(message);
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("파일을 찾지못함");
		} catch (IOException e) {
			System.out.println("파일쓰기에서 IO에러 발생");
		}
	}
}
