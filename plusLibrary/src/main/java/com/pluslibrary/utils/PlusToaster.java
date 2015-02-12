package com.pluslibrary.utils;

import android.content.Context;
import android.widget.Toast;

public class PlusToaster {
	public static void doIt(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
}
