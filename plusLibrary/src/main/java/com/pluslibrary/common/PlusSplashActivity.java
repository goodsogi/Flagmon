package com.pluslibrary.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;

import com.pluslibrary.PlusConstants;
import com.pluslibrary.R;
import com.pluslibrary.utils.PlusGcmRegister;
import com.pluslibrary.utils.PlusLocationFinder;

/**
 * 인트로 화면
 * 
 * @author jeff
 * 
 */
//public class PlusSplashActivity extends Activity {
//
//	final static int DELAY_MILLI_SECOND = 3000;
//	private Editor mEditor;
//	private SharedPreferences mSharedPreference;
//	private PlusLocationFinder mFinder;
//
//	/** Called when the activity is first created. */
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_splash);
//
//		// 위치등록
//		PlusLocationFinder finder = new PlusLocationFinder(this,
//				ApiConstants.SEND_LOCATION);
//		if (finder.doIt()) {
//			// 푸시 아이디 저장
//			PlusGcmIdSaver saver = new PlusGcmIdSaver(this);
//			saver.doIt();
//			moveNextActivity();
//
//		}
//
//	}
//
//	private void moveNextActivity() {
//
//		mSharedPreference = getSharedPreferences(CommonConstants.PREF_NAME,
//				Context.MODE_PRIVATE);
//		mEditor = mSharedPreference.edit();
//		boolean isFirstRun = mSharedPreference.getBoolean(
//				CommonConstants.KEY_IS_FIRST_RUN, true);
//		final Class className;
//		if (!isFirstRun) {
//
//			className = MainActivity.class;
//		} else {
//			mEditor.putBoolean(CommonConstants.KEY_IS_FIRST_RUN, false);
//			mEditor.commit();
//			// 로그인 액티비티로 이동
//			className = GuardActivity.class;
//		}
//
//		// 다음 액티비티로 이동
//		new Handler().postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//
//				finish();
//
//				Intent intent = new Intent(SplashActivity.this, className);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//						| Intent.FLAG_ACTIVITY_NEW_TASK);
//				startActivity(intent);
//
//			}
//		}, DELAY_MILLI_SECOND);
//
//	}
//
//	
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		switch (requestCode) {
//
//		case PlusConstants.REQUEST_LOCATION_AGREEMENT:
//			// 사용자가 위치 정보 액세스를 on 시켰는지 확인. 그러면 다음 액티비티도 이동. 아니면 다시 경고창 띄움
//			if (mFinder.canGetLocation()) {
//				mFinder.getCurrentLocation();
//				moveNextActivity();
//			} else {
//				mFinder.moveToSetting();
//			}
//
//			break;
//
//		}
//	}
//
//}
