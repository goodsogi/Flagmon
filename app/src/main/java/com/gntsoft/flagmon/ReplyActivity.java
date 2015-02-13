package com.gntsoft.flagmon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 2. 13.
 */
public class ReplyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        makeSampleList();
    }

    private void makeSampleList() {
        ListView list = (ListView) findViewById(R.id.list_reply);

        if (list == null) return;
        list.setAdapter(new ReplyListAdapter(this,
                getSampleDatas()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });


    }

    private ArrayList<ReplyListModel> getSampleDatas() {
        ArrayList<ReplyListModel> datas = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            ReplyListModel data = new ReplyListModel();
            data.setName("산다라박");
            data.setContent("내가 제일 잘나가");
            data.setTime("2시간전");
            data.setImg(R.drawable.sandarapark);
            datas.add(data);
        }

        return datas;
    }
}
