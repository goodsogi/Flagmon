package com.gntsoft.flagmon.myalbum;

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
import com.gntsoft.flagmon.friend.FriendListAdapter;
import com.gntsoft.flagmon.neighbor.NeighborListModel;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 3.
 */
public class ListMyAlbumFragment extends FMCommonFragment implements
        PlusOnGetDataListener {

    private static final int GET_MAIN_LIST = 0;
    String [] listFriendOptionDatas = {"인기순","최근 등록순","퍼간 날짜","거리순"};

    public ListMyAlbumFragment() {
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
                .findViewById(R.id.list_my_album);

        if (list == null) return;
        list.setAdapter(new MyAlbumListAdapter(mActivity,
                getSampleDatas()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //goToDetail();
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
            data.setName("산다라박");
            data.setContent("박산다라는 어린 시절 무역업에 종사하는 아버지를 따라 대한민국에서 필리핀으로 이민을 가서 생활한다. 학교 행사에서 만난, 연예계에서 활동 중이던 친구가 필리핀 방송국 ABS-CBN의 스타 서클 퀘스트라고 하는 스타가 되기 위한 서바이벌 쇼의 오디션에 응시해 볼 것을 권하여 산다라는 쇼에 참가하게 된다. 최종 다섯 명이 확정되는 마지막 탈락자 선발전에서 약 50만 통의 휴대전화 문자 득표를 기록하였고, 이는 여성 출연자 사상 최대의 기록이었다. 쇼 전체를 통틀어 적어도 2백만여 통의 문자 득표를 획득했다고 추정된다.");
            data.setDistance("25m");
            data.setReplyCount("50");
            data.setPinCount("15");
            data.setRegisterDate("2014.04.01 12:30");
            data.setImg(R.drawable.sandarapark);
            data.setBigImg(R.drawable.sandarapark2);
            datas.add(data);
        }

        return datas;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_my_album,
                container, false);
        return rootView;
    }

    @Override
    protected void addListenerButton() {
        Button sort = (Button) mActivity.findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortPopupFriend(v);
            }
        });

    }

    public void showSortPopupFriend(View v) {
        PlusClickGuard.doIt(v);

        AlertDialog.Builder ab = new AlertDialog.Builder(mActivity, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("정렬방식을 선택해주세요.");
        ab.setItems(listFriendOptionDatas, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                doSortFriend(whichButton);

            }
        }).setNegativeButton("닫기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        ab.show();
    }

    private void doSortFriend(int whichButton) {
        switch (whichButton) {
            case 0: sortByPopular();
                break;

            case 1: sortByRecent();
                break;
            case 2: sortByPin();
                break;
            case 3:sortByDistance();
                break;

        }
    }

    private void sortByDistance() {
        PlusToaster.doIt(mActivity, "준비중...");
        //구현!!
    }

    private void sortByPin() {
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
//                        + PlusPhoneNumberFinder.isLogIn(mActivity),
//                new OrderHistoryParser());

    }

}