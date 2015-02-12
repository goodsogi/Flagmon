package com.pluslibrary.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.pluslibrary.PlusConstants;
import com.pluslibrary.R;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;

/**
 * 현재 위치 가져오기
 * 
 * @author jeff
 * 
 */
public class PlusLocationFinder implements android.location.LocationListener ,
PlusOnGetDataListener {

	private static final int SEND_LOCATION = 0;
	private Activity mActivity;
	private LocationManager mLocationManager;
	private String mProvider;
	private String mApiUrl;

	public PlusLocationFinder(Activity activity, String apiUrl) {
		mActivity = activity;
		mApiUrl = apiUrl;

	}
	
	
	public boolean doIt() {
		if(canGetLocation()) {
			getCurrentLocation();
			return true;
		} else {
			moveToSetting();
			return false;
		}
	}

	public boolean canGetLocation() {
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(mActivity);
		if (status != ConnectionResult.SUCCESS)
			return false;

		mLocationManager = (LocationManager) mActivity
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		mProvider = mLocationManager.getBestProvider(criteria, true);

		if (mProvider == null
				|| mProvider.equals(LocationManager.PASSIVE_PROVIDER))
			return false;

		// 구글 플레이 서비스를 사용할 수 있고 설정에서 위치 정보 액세스가 on된 경우
		return true;

	}

	/**
	 * 위치 정보 액세스 설정으로 이동
	 */

	public void moveToSetting() {

		new AlertDialog.Builder(mActivity)
				.setTitle(R.string.location_popup_title)
				.setMessage(R.string.turn_on_location_service)
				.setNeutralButton(R.string.confirm,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mActivity
										.startActivityForResult(
												new Intent(
														android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
												PlusConstants.REQUEST_LOCATION_AGREEMENT);
							}
						})
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
					}
				}).show();

	}

	/**
	 * 현재 위치 가져오기
	 */
	public void getCurrentLocation() {
		// TODO Auto-generated method stub
		// mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// 1000, 10, this);
		// 네트워크 제공자가 제공하는 위치. GPS를 사용하면 변경 필요!!
		mLocationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 1000, 10, this);

	}

	@Override
	public void onLocationChanged(final Location location) {

		mLocationManager.removeUpdates(this);
		//서버 등록
		
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair("id", PlusPhoneNumberFinder
				.doIt(mActivity)));
		postParams.add(new BasicNameValuePair("lat", String
				.valueOf(location.getLatitude())));
		postParams.add(new BasicNameValuePair("lng", String
				.valueOf(location.getLongitude())));
		
		new PlusHttpClient(mActivity, this, false).execute(SEND_LOCATION,
				mApiUrl, new PlusInputStreamStringConverter(), postParams);
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void onSuccess(Integer from, Object datas) {
		switch (from) {
		case SEND_LOCATION:
			Log.i("plus", "위치 등록 결과: " + datas);
			break;

		default:
			break;
		}
	}

}
