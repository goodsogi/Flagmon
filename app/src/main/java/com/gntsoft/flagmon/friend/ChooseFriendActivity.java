package com.gntsoft.flagmon.friend;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.setting.FindFriendModel;
import com.gntsoft.flagmon.setting.SentFriendRequestListAdapter;
import com.pluslibrary.server.PlusOnGetDataListener;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 3.
 */
public class ChooseFriendActivity extends Activity implements
        PlusOnGetDataListener {
    private static final int GET_MAIN_LIST = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);
        makeSampleList1();
    }

    private void makeSampleList1() {

        ListView list = (ListView) findViewById(R.id.list_choose_friend);

        if (list == null) return;
        list.setAdapter(new ChooseFriendListAdapter(this,
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
