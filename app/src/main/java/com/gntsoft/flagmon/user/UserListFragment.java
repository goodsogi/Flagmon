package com.gntsoft.flagmon.user;

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
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.detail.DetailActivity;
import com.gntsoft.flagmon.neighbor.NeighborListAdapter;
import com.gntsoft.flagmon.neighbor.NeighborListModel;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FMListParser;
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class UserListFragment extends FMCommonFragment implements
        PlusOnGetDataListener {

    private static final int GET_USER_LIST_DATA = 0;
    String [] listOptionDatas = {"인기순","최근 등록순","거리순"};
    private int totalUserPost;

    public UserListFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDataFromServer(FMConstants.SORT_BY_POPULAR);
    }

    public void getDataFromServer(String sortType) {

//특정 사용자 이메일등 처리!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("user_email", "user@email.com"));
        postParams.add(new BasicNameValuePair("sort", sortType));
        if(LoginChecker.isLogIn(mActivity)) { postParams.add(new BasicNameValuePair("key", getUserAuthKey()));}


        new PlusHttpClient(mActivity, this, false).execute(GET_USER_LIST_DATA,
                FMApiConstants.GET_USER_LIST_DATA, new PlusInputStreamStringConverter(),
                postParams);
    }


    private void goToDetail(String idx) {
        Intent intent = new Intent(mActivity, DetailActivity.class);
        intent.putExtra(FMConstants.KEY_POST_IDX, idx);

        startActivity(intent);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_list,
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

        //sort 값 수정!!
        getDataFromServer(FMConstants.SORT_BY_DISTANCE);
    }

    private void sortByRecent() {
        getDataFromServer(FMConstants.SORT_BY_RECENT);
    }

    private void sortByPopular() {
        getDataFromServer(FMConstants.SORT_BY_POPULAR);
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case GET_USER_LIST_DATA:
                makeList(new FMListParser().doIt((String) datas));
                showTotalUserPost();
                break;
        }

    }

    private void showTotalUserPost() {
        TextView totalUserPost = (TextView) mActivity.findViewById(R.id.totalUserPost);
        totalUserPost.setText("친구를 맺으면 " + getTotalUserPost() + "개의 게시물을 함께 나눌 수 있습니다.");
    }

    private void makeList(final ArrayList<FMModel> datas) {

        ListView list = (ListView) mActivity
                .findViewById(R.id.list_user);

        if (list == null||datas==null) return;
        list.setAdapter(new NeighborListAdapter(mActivity,
                datas));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToDetail(datas.get(position).getIdx());
            }
        });
    }

    public int getTotalUserPost() {
        return ((UserPageActivity) mActivity).getTotalUserPost();


    }
}
