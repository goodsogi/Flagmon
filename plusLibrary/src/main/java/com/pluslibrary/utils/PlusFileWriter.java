package com.pluslibrary.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Environment;

public class PlusFileWriter {

	public static void doIt(ArrayList<String> messageBox, String folderName) {

		File folder = new File(Environment.getExternalStorageDirectory()
				+ "/" + folderName);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		String filename = PlusTimeFormatter.doItLong();
		// file을 file.createNewFile()로 생성하면 fileNotFoundException 발생
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(folder.getAbsolutePath()
					+ "/" + filename + ".txt", false));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			for (String messge : messageBox) {

				writer.write(messge);
				writer.newLine();
			}
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("파일을 찾지못함");
		} catch (IOException e) {
			System.out.println("파일쓰기에서 IO에러 발생");
		}
	}
}
