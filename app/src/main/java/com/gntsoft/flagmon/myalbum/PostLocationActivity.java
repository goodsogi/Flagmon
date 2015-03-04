package com.gntsoft.flagmon.myalbum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.detail.MapDetailFragment;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 4.
 */
public class PostLocationActivity extends Activity implements
        PlusOnGetDataListener {
    private static final int GET_MAIN_LIST = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_location);
        showMap();
    }


    private void showMap() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container_post_location, new MapPostLocationFragment())
                .commit();

    }


    public void showChooseShareTypeBar(View v) {
        PlusClickGuard.doIt(v);

        LinearLayout barChooseShareType = (LinearLayout) findViewById(R.id.barChooseShareType);
        barChooseShareType.setVisibility(View.VISIBLE);
    }

    public void goBack(View v) {
        finish();
    }


    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case GET_MAIN_LIST:
                //makeList(datas);
                break;
        }

    }
}

