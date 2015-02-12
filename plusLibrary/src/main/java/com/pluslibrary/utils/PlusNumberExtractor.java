package com.pluslibrary.utils;

public class PlusNumberExtractor {
	public static int doIt(String string) {
		String tmpStr = "";
		char[] charArray = string.toCharArray();
		int length = charArray.length;
		for (int i = 0; i < length; i++) {
			if (Character.isDigit((charArray[i]))) {
				tmpStr += charArray[i];

			}

		}
		return PlusNumericChecker.doIt(tmpStr) ? Integer.parseInt(tmpStr) : 0;
	}
}
