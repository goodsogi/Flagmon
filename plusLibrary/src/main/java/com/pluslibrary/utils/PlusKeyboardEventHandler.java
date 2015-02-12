package com.pluslibrary.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public abstract class PlusKeyboardEventHandler {
	private boolean isOpened;
	
	public PlusKeyboardEventHandler(Activity activity) {
		addObserver(activity);
	}

	private void addObserver(final Activity activity) {
		final View activityRootView = activity.getWindow().getDecorView()
				.findViewById(android.R.id.content);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {

						int heightDiff = activityRootView.getRootView()
								.getHeight() - activityRootView.getHeight();
						if (heightDiff > 100) { // 99% of the time the height
												// diff will be due to a
												// keyboard.
							PlusLogger.doIt("softkeyborad up!!");

							if (isOpened == false) {
								onKeyboardShow();
							}
							isOpened = true;
						} else if (isOpened == true) {
							PlusLogger.doIt("softkeyborad Down!!!");
							isOpened = false;
							doKeyboardHide();
							
						}
					}
				});
	}

	protected abstract void onKeyboardShow();

	protected abstract void doKeyboardHide();

}
