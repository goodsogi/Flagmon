package com.pluslibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class PlusScreenMeasureTool {
	/**
	 * height of status bar
	 * @param context
	 * @return
	 */
	static public int getStatusBarHeight(Context context) {
	    int result = 0;
	    int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
	    if (resourceId > 0) {
	        result = context.getResources().getDimensionPixelSize(resourceId);
	    }
	    return result;
	}
	
	/**
	 * height of screen
	 * @param context
	 * @return
	 */
	static public int getScreenHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	
	/**
	 * height of real screen
	 * @param context
	 * @return
	 */
	static public int getRealScreenHeight(Activity activity) {
		
		 int statusBarHeight = 0;
		    int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
		    if (resourceId > 0) {
		    	statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
		    }
		    
		    
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels - statusBarHeight;
	}
	
	
	/**
	 * width of screen
	 * @param context
	 * @return
	 */
	static public int getScreenWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
}
