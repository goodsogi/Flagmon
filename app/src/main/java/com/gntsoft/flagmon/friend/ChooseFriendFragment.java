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
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.server.FriendListParser;
import com.gntsoft.flagmon.setting.FriendModel;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 3. 13.
 */
public class ChooseFriendFragment extends FMCommonFragment implements
        PlusOnGetDataListener {

    private static final int CHOOSE_FRIEND = 0;

    public ChooseFriendFragment() {
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
        postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_FRIEND));
        //postParams.add(new BasicNameValuePair("sort", sortType));
        if(LoginChecker.isLogIn(mActivity)) { postParams.add(new BasicNameValuePair("key", getUserAuthKey()));}


        new PlusHttpClient(mActivity, this, false).execute(CHOOSE_FRIEND,
                FMApiConstants.CHOOSE_FRIEND, new PlusInputStreamStringConverter(),
                postParams);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_friend,
                container, false);
        return rootView;
    }

    @Override
    protected void addListenerButton() {


    }


    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case CHOOSE_FRIEND:
                makeList(new FriendListParser().doIt((String) datas));
                break;
        }

    }

    private void makeList(final ArrayList<FriendModel> datas) {

        ListView list = (ListView) mActivity
                .findViewById(R.id.list_friend);

        if (list == null||datas==null) return;
        list.setAdapter(new ChooseFriendListAdapter(mActivity,
                this, datas));

    }



}