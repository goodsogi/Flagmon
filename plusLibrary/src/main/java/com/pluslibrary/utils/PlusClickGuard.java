package com.pluslibrary.utils;

import android.os.Handler;
import android.view.View;

/**
 * 이중 클릭 방지
 * 
 * @author user
 * 
 */
public class PlusClickGuard {
	public static void doIt(final View v) {
		v.setEnabled(false);
		Handler h = new Handler();
		h.postDelayed(new Runnable() {

			@Override
			public void run() {
				v.setEnabled(true);
			}
		}, 1000);
	}
}
