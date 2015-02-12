package com.pluslibrary.utils;

import android.util.Log;

public class PlusLogger {
	static public void doIt(String tag, String s) {
		Log.d(tag, s);
	}
	static public void doIt( String s) {
		Log.d("plus", s);
	}
}
