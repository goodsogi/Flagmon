package com.gntsoft.flagmon.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ListSearchFragment extends FMCommonFragment implements
        PlusOnGetDataListener {

    private static final int GET_LIST_DATA = 0;

    public ListSearchFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDataFromServer(getArguments().getString(FMConstants.KEY_SORT_TYPE));
    }

    public void getDataFromServer(String sortType) {
//srchPost에 검색어 넣나?? 수정!!??

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_NEIGHBOR));
        postParams.add(new BasicNameValuePair("sort", sortType));
        postParams.add(new BasicNameValuePair("srchPost", getArguments().getString(FMConstants.KEY_KEYWORD)));
        if(LoginChecker.isLogIn(mActivity)) { postParams.add(new BasicNameValuePair("key", getUserAuthKey()));}


        new PlusHttpClient(mActivity, this, false).execute(GET_LIST_DATA,
                FMApiConstants.GET_LIST_DATA, new PlusInputStreamStringConverter(),
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
        View rootView = inflater.inflate(R.layout.fragment_list_search,
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
            case GET_LIST_DATA:
                makeList(new FMListParser().doIt((String) datas));
                break;
        }

    }

    private void makeList(final ArrayList<FMModel> datas) {

        ListView list = (ListView) mActivity
                .findViewById(R.id.list_search);

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


}
