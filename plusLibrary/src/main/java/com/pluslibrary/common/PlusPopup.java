package com.pluslibrary.common;

import android.app.Activity;

import com.pluslibrary.server.PlusOnGetDataListener;

/**
 * 리뷰
 * 
 * @author jeff
 * 
 */
public class PlusPopup extends CommonDialog implements PlusOnGetDataListener {
	final static int SEND_REVIEW = 0;

	public PlusPopup(Activity activity, int layoutId) {
		super(activity, null, layoutId);

	}
	
	@Override
	public void onSuccess(Integer from, Object datas) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addListenerToButton() {
		// TODO Auto-generated method stub
		
	}
}
