package com.pluslibrary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.pluslibrary.R;

/**
 * Detect internet connection
 * 
 * @author user
 * 
 */
public class PlusInternectConnectionDetector {
	public static boolean hasConnection(Context context) {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		
		if(haveConnectedWifi || haveConnectedMobile) {
			return true;
		} else {
			Toast.makeText(context, R.string.not_connected_internet,
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}
}
