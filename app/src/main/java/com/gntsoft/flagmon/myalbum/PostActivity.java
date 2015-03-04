package com.gntsoft.flagmon.myalbum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.detail.DetailGridviewAdapter;
import com.gntsoft.flagmon.setting.FindFriendActivity;
import com.gntsoft.flagmon.setting.FindFriendModel;
import com.gntsoft.flagmon.setting.GotFriendRequestListAdapter;
import com.gntsoft.flagmon.setting.SentFriendRequestListAdapter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 3.
 */
public class PostActivity extends Activity implements
        PlusOnGetDataListener {
    private static final int GET_MAIN_LIST = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        makeGridView();
    }

    private void makeGridView() {

        ArrayList<Integer> imgs = new ArrayList<>();


        imgs.add(R.drawable.sandarapark);
        imgs.add(R.drawable.sandarapark2);
        imgs.add(R.drawable.sandarapark3);
        imgs.add(R.drawable.sandarapark4);
        imgs.add(R.drawable.sandarapark5);
        imgs.add(R.drawable.sandarapark);
        imgs.add(R.drawable.sandarapark2);
        imgs.add(R.drawable.sandarapark3);
        imgs.add(R.drawable.sandarapark4);
        imgs.add(R.drawable.sandarapark5);
        imgs.add(R.drawable.sandarapark);
        imgs.add(R.drawable.sandarapark2);
        imgs.add(R.drawable.sandarapark3);
        imgs.add(R.drawable.sandarapark4);
        imgs.add(R.drawable.sandarapark5);


        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new PostGridViewAdapter(this, imgs));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                goToPostLocation(v);
            }
        });
    }

    private void goToPostLocation(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, PostLocationActivity.class);
        startActivity(intent);
    }

    public void goBack(View v) {
        finish();
    }

    public void doCamera(View v) {
//구현!!
        PlusToaster.doIt(this, "준비중...");
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
