package com.pluslibrary.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.pluslibrary.server.ApiConstants;
import com.pluslibrary.server.GeoModel;
import com.pluslibrary.server.GeoParser;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusOnGetDataListener;

/**
 * 구글지도 보기
 * 
 * @author jeff
 * 
 */
public class PlusMapViewer implements PlusOnGetDataListener {
	private static final int GET_GEO = 0;
	private static Context mContext;

	public PlusMapViewer(Context context) {
		mContext = context;
	}

	public static void doIt(Context context, String latitude, String longitude) {
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
				Uri.parse("geo:" + latitude + "," + longitude + "?q="
						+ latitude + "," + longitude + "(" + "" + ")"));
		intent.setComponent(new ComponentName("com.google.android.apps.maps",
				"com.google.android.maps.MapsActivity"));
		context.startActivity(intent);
	}

	public void doIt(String address) {
		try {
			new PlusHttpClient((Activity) mContext, PlusMapViewer.this, false)
					.execute(
							GET_GEO,
							ApiConstants.GET_GEO
									+ URLEncoder.encode(address + "&key="
											+ ApiConstants.GOOGLE_KEY, "UTF-8"),
							new GeoParser());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onSuccess(Integer from, Object datas) {
		if(datas == null) return;
		switch (from) {
		case GET_GEO:

			// 지도 보기
			doIt(mContext, ((GeoModel) datas).lat, ((GeoModel) datas).lon);

			break;

		}

	}
}
