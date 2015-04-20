package com.gntsoft.flagmon.friend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FriendListParser;
import com.gntsoft.flagmon.server.FriendModel;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 3. 13.
 */
public class ChooseFriendListFragment extends FMCommonFragment implements
        PlusOnGetDataListener {

    private static final int GET_FRIEND_LIST = 0;

    public ChooseFriendListFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDataFromServer();
    }

    public void getDataFromServer() {

        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));


        new PlusHttpClient(mActivity, this, false).execute(GET_FRIEND_LIST,
                FMApiConstants.GET_FRIEND_LIST, new PlusInputStreamStringConverter(),
                postParams);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_friend_list,
                container, false);
        return rootView;
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case GET_FRIEND_LIST:
                makeList(new FriendListParser().doIt((String) datas));
                break;
        }

    }

    @Override
    protected void addListenerToButton() {


    }

    private void makeList(final ArrayList<FriendModel> datas) {

        ListView list = (ListView) mActivity
                .findViewById(R.id.list_friend);

        if (list == null || datas == null) return;
        list.setAdapter(new ChooseFriendListAdapter(mActivity,
                this, datas));

    }


}