package com.pluslibrary.common;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

/**
 * 공통 fragment
 * 
 * @author jeff
 * 
 */
public abstract class CommonFragment extends Fragment {
	protected Activity mActivity;

	public CommonFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mActivity = getActivity();
		// 리스너 등록
		addListenerButton();
		
	}

	/**
	 * 리스너 등록
	 */
	abstract protected void addListenerButton();

	

}
