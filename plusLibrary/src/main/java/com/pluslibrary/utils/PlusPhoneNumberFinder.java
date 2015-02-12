package com.pluslibrary.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 아이디 가져오기(전화번호)
 * 
 * @author jeff
 * 
 */
public class PlusPhoneNumberFinder {

	private static final String FAKE_NUMBER = "0102477345";

	public static String doIt(Context context) {
		TelephonyManager tMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = tMgr.getLine1Number();

		if (phoneNumber != null) {
			// 0을 삽입
			String myPhoneNumber = phoneNumber.replace("+82", "0");
			return myPhoneNumber;

			//return myPhoneNumber.contains("0000") ? FAKE_NUMBER : myPhoneNumber;
		} else {
			// 폰 번호가 없는 경우
			return FAKE_NUMBER;
			//return tMgr.getDeviceId();
		}
		

	}
}
