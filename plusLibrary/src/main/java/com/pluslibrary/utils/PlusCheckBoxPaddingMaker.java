package com.pluslibrary.utils;

import android.content.Context;
import android.view.View;

public class PlusCheckBoxPaddingMaker {
	public static void doIt(Context context, View view) {
		final float scale = context.getResources().getDisplayMetrics().density;
		view.setPadding(view.getPaddingLeft() + (int)(10.0f * scale + 0.5f),
				view.getPaddingTop(),
				view.getPaddingRight(),
				view.getPaddingBottom());
	}
}
