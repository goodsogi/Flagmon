package com.gntsoft.flagmon.friend;

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
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.detail.DetailActivity;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FMListParser;
import com.gntsoft.flagmon.server.FMMapParser;
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 3. 3.
 */
public class ListFriendFragment extends FMCommonFragment implements
        PlusOnGetDataListener {

    private static final int GET_LIST_DATA = 0;
    String [] listFriendOptionDatas = {"인기순","최근 등록순","퍼간 날짜","거리순"};

    public ListFriendFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDataFromServer();
    }

    public void getDataFromServer() {


        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_FRIEND));
        if(LoginChecker.isLogIn(mActivity)) { postParams.add(new BasicNameValuePair("key", getUserAuthKey()));}


        new PlusHttpClient(mActivity, this, false).execute(GET_LIST_DATA,
                FMApiConstants.GET_LIST_DATA, new PlusInputStreamStringConverter(),
                postParams);
    }


    private void goToDetail() {
        Intent intent = new Intent(mActivity, DetailActivity.class);
        startActivity(intent);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_friend,
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
            case GET_LIST_DATA:
                makeList(new FMListParser().doIt((String) datas));
                break;
        }

    }

    private void makeList(ArrayList<FMModel> datas) {

        ListView list = (ListView) mActivity
                .findViewById(R.id.list_friend);

        if (list == null||datas==null) return;
        list.setAdapter(new FriendListAdapter(mActivity,
                datas));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToDetail();
            }
        });
    }



}