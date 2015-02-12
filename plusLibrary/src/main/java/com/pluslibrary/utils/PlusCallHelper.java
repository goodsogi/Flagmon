package com.pluslibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 전화걸기
 * 
 * @author jeff
 * 
 */
public class PlusCallHelper {
	public static void doIt(Context context, String number) {
		Intent phoneIntent = new Intent(Intent.ACTION_CALL);
		phoneIntent.setData(Uri.parse("tel:" + number));
		context.startActivity(phoneIntent);
	}
}
