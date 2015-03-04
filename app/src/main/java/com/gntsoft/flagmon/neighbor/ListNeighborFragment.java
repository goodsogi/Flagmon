package com.gntsoft.flagmon.neighbor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.detail.DetailActivity;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;

import java.util.ArrayList;

public class ListNeighborFragment extends FMCommonFragment implements
        PlusOnGetDataListener {

    private static final int GET_MAIN_LIST = 0;
    String [] listOptionDatas = {"인기순","최근 등록순","거리순"};

    public ListNeighborFragment() {
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
        list.setAdapter(new NeighborListAdapter(mActivity,
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_list,
                container, false);
        return rootView;
    }

    @Override
    protected void addListenerButton() {
        Button sort = (Button) mActivity.findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortPopup(v);
            }
        });

    }

    public void showSortPopup(View v) {
        PlusClickGuard.doIt(v);

        AlertDialog.Builder ab = new AlertDialog.Builder(mActivity, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("정렬방식을 선택해주세요.");
        ab.setItems(listOptionDatas, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                doSort(whichButton);

            }
        }).setNegativeButton("닫기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        ab.show();
    }

    private void doSort(int whichButton) {


        switch (whichButton) {
            case 0: sortByPopular();
                break;

            case 1: sortByRecent();
                break;

            case 2:sortByDistance();
                break;

        }
    }

    private void sortByDistance() {
        PlusToaster.doIt(mActivity, "준비중...");
        //구현!!
    }

    private void sortByRecent() {
        PlusToaster.doIt(mActivity,"준비중...");
        //구현!!
    }

    private void sortByPopular() {
        PlusToaster.doIt(mActivity,"준비중...");
        //구현!!
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
