package com.pluslibrary.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Inputstream을 String으로 변환
 * 
 * @author jeff
 * 
 */
public class PlusInputStreamStringConverter extends PlusXmlParser {
	// convert InputStream to String
	public Object doIt(InputStream in) {
		
		if(in==null) return null;

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(in));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

}
