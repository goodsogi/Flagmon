package com.gntsoft.flagmon.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.gntsoft.flagmon.FMConstants;

/**
 * Created by johnny on 15. 3. 3.
 */
public class LoginChecker {
    public static boolean isLogIn(Activity activity) {
        SharedPreferences sharedPreference = activity.getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getBoolean(FMConstants.KEY_IS_LOGIN, false);


    }
}