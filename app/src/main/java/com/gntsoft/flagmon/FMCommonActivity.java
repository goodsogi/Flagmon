package com.gntsoft.flagmon;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by johnny on 15. 3. 11.
 */
public class FMCommonActivity extends FragmentActivity {


    private double mLatUL;
    private double mLonUL;
    private double mLatLR;
    private double mLonLR;

    public void goBack(View v) {
        finish();
    }

    public String getUserAuthKey() {
        SharedPreferences sharedPreference = getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getString(FMConstants.KEY_USER_AUTH_KEY, "");
    }

    public double getLatUL() {
        return mLatUL;
    }

    public void setLatUL(double latUL) {
        mLatUL = latUL;
    }

    public double getLonUL() {
        return mLonUL;
    }

    public void setLonUL(double lonUL) {
        mLonUL = lonUL;
    }

    public double getLatLR() {
        return mLatLR;
    }

    public void setLatLR(double latLR) {
        mLatLR = latLR;
    }

    public double getLonLR() {
        return mLonLR;
    }

    public void setLonLR(double lonLR) {
        mLonLR = lonLR;
    }
}
