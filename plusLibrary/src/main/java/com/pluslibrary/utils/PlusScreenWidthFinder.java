package com.pluslibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

public class PlusScreenWidthFinder {
	@SuppressLint("NewApi")
	public static int doIt(Activity activity) {

		if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13) {

			Display display = activity.getWindowManager().getDefaultDisplay();
			return display.getWidth();

		} else {

			Display display = activity.getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			return size.x;

		}
	}
}
