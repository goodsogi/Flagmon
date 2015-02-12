package com.pluslibrary.utils;

public class PlusNumericChecker {
	public static boolean doIt(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
