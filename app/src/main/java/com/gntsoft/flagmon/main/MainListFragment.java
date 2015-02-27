package com.gntsoft.flagmon.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.detail.DetailActivity;
import com.pluslibrary.server.PlusOnGetDataListener;

import java.util.ArrayList;

public class MainListFragment extends FMCommonFragment implements
        PlusOnGetDataListener {

    private static final int GET_MAIN_LIST = 0;

    public MainListFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        makeSampleList();
        //refreshList();
    }

    private void makeSampleList() {

        ListView list = (ListView) mActivity
                .findViewById(R.id.list_main);

        if (list == null) return;
        list.setAdapter(new MainListAdapter(mActivity,
                getSampleDatas()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToDetail();
            }
        });


    }

    private void goToDetail() {
        Intent intent = new Intent(mActivity, DetailActivity.class);
        startActivity(intent);
    }

    private ArrayList<MainListModel> getSampleDatas() {
        ArrayList<MainListModel> datas = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            MainListModel data = new MainListModel();
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_list,
                container, false);
        return rootView;
    }

    @Override
    protected void addListenerButton() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case GET_MAIN_LIST:
                makeList(datas);
                break;
        }

    }

    private void makeList(Object datas) {

//        ListView list = (ListView) mActivity
//                .findViewById(R.id.list_main);
//
//        if(list==null) return;
//        list.setAdapter(new OrderHistoryListAdapter(mActivity,this,
//                (ArrayList<OrderHistoryModel>) datas));
    }

    public void refreshList() {
//        new PlusHttpClient(mActivity, this, false).execute(
//                GET_ORDER_HISTORY,
//                ApiConstants.GET_ORDER_HISTORY + "?id="
//                        + PlusPhoneNumberFinder.doIt(mActivity),
//                new OrderHistoryParser());

    }

}
