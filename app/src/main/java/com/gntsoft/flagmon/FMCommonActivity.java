package com.gntsoft.flagmon;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by johnny on 15. 3. 11.
 */
public class FMCommonActivity extends FragmentActivity {


    public void goBack(View v) {
        finish();
    }

    public String getUserAuthKey() {
        SharedPreferences sharedPreference = getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getString(FMConstants.KEY_USER_AUTH_KEY, "");
    }
}
