package com.gntsoft.flagmon.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gntsoft.flagmon.R;
import com.pluslibrary.server.PlusOnGetDataListener;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 3.
 */
public class FindFriendActivity extends Activity implements
        PlusOnGetDataListener {
    private static final int GET_MAIN_LIST = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
        makeSampleList1();
        makeSampleList2();
    }

    private void makeSampleList1() {

        ListView list = (ListView) findViewById(R.id.list_got_friend_request);

        if (list == null) return;
        list.setAdapter(new SentFriendRequestListAdapter(this,
                getSampleDatas()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });


    }

    private void makeSampleList2() {

        ListView list = (ListView)
                findViewById(R.id.list_sent_friend_request);

        if (list == null) return;
        list.setAdapter(new GotFriendRequestListAdapter(this,
                getSampleDatas()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });


    }

    private ArrayList<FindFriendModel> getSampleDatas() {
        ArrayList<FindFriendModel> datas = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            FindFriendModel data = new FindFriendModel();
            data.setName("Sandara Park");

            data.setImg(R.drawable.sandarapark);
            datas.add(data);
        }

        return datas;
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
