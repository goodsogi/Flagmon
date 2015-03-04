package com.gntsoft.flagmon.myalbum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.neighbor.NeighborListAdapter;
import com.gntsoft.flagmon.neighbor.NeighborListModel;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 4.
 */
public class GroupPostActivity extends Activity implements
        PlusOnGetDataListener {
    private static final int GET_MAIN_LIST = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_post);
        makeSampleList();
    }

    private void makeSampleList() {

        ListView list = (ListView) findViewById(R.id.list_group_post);

        if (list == null) return;
        list.setAdapter(new GroupPostListAdapter(this,
                getSampleDatas()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //goToDetail();
            }
        });


    }

    private ArrayList<NeighborListModel> getSampleDatas() {
        ArrayList<NeighborListModel> datas = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            NeighborListModel data = new NeighborListModel();
            data.setTitle("YTN뉴스");
            data.setContent("세월호 침몰 사건");
            data.setTime("25m");
            data.setReplyCount("50");
            data.setPinCount("15");
            data.setRegisterDate("2014.04.01 12:30");
            data.setImg(R.drawable.sandarapark);
            datas.add(data);
        }

        return datas;
    }

    public void goBack(View v) {
        finish();
    }

    public void completePost(View v) {
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
