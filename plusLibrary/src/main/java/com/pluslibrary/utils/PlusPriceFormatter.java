package com.pluslibrary.utils;

public class PlusPriceFormatter {

	public static String doIt(String str) {
		str = str.replace("원", ""); // 원 제거
		str = str.trim(); // 공백 제거
		return isNumeric(str) ? String.format("%, d",
				Integer.parseInt(str.trim())) : (str.equals("") ? "0" : str);
	}

	private static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}
