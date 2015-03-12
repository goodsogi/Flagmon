package com.gntsoft.flagmon.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.R;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 3.
 */
public class FindFriendActivity extends FMCommonActivity implements
        PlusOnGetDataListener {
    private static final int GET_MAIN_LIST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
        makeSampleList1();
        makeSampleList2();
    }


    public void findFriendOnFacebook(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, FindFriendInFacebookActivity.class);

        startActivity(intent);

    }

    public void findFriendOnTwitter(View v) {
        PlusClickGuard.doIt(v);
        Intent intent = new Intent(this, FindFriendInTwitterActivity.class);

        startActivity(intent);

    }


    public void findFriendInContact(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, FindFriendInContactActivity.class);

        startActivity(intent);
    }


    public void searchFriendByName(View v) {
        PlusClickGuard.doIt(v);
        Intent intent = new Intent(this, SearchFriendByNameActivity.class);

        startActivity(intent);

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

    private ArrayList<FriendModel> getSampleDatas() {
        ArrayList<FriendModel> datas = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            FriendModel data = new FriendModel();
            data.setName("Sandara Park");

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
