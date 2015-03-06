package com.gntsoft.flagmon.myalbum;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.R;
import com.pluslibrary.server.PlusOnGetDataListener;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 2. 13.
 */
public class MyalbumFragment extends FMCommonFragment implements
        PlusOnGetDataListener {

    private static final int GET_MAIN_LIST = 0;

    public MyalbumFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //refreshList();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myalbum,
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
//                        + PlusPhoneNumberFinder.isLogIn(mActivity),
//                new OrderHistoryParser());

    }

}

