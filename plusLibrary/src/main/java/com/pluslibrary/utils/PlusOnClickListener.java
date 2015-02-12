package com.pluslibrary.utils;

import android.view.View;
import android.view.View.OnClickListener;

public abstract class PlusOnClickListener implements OnClickListener {

	@Override
	public void onClick(View v) {
		PlusClickGuard.doIt(v);
		doIt();

	}

	protected abstract void doIt();
}
