package com.gntsoft.flagmon.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.MonInfoModel;
import com.gntsoft.flagmon.server.MonInfoParser;
import com.gntsoft.flagmon.server.TotalMonParser;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusOnClickListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 4. 20.
 */
public class ShowMonInfoFragment extends FMCommonFragment implements PlusOnGetDataListener{

    private  final int GET_MON_INFO = 77;
    private  final int GET_INITIAL_MON_INFO = 88;

    public ShowMonInfoFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getInitialMonInfoFromServer();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_show_mon_info,
                container, false);
        return rootView;


    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null) return;
        switch (from) {

            case GET_MON_INFO:
               // showHelp();
                //showTotalMon(new TotalMonParser().doIt((String) datas));
                makeList(new MonInfoParser().doIt((String) datas));
                break;

            case GET_INITIAL_MON_INFO:
                showHelp();
                showTotalMon(new TotalMonParser().doIt((String) datas));
                //makeList(new MonInfoParser().doIt((String) datas));
                break;

        }
    }

    private void showHelp() {
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString(FMConstants.KEY_USER_EMAIL, "");

        TextView notiMonInfo = (TextView) mActivity.findViewById(R.id.notiMonInfo);
        notiMonInfo.setText(userEmail + "님께 알려드립니다. 무료로 지급된 몬의 경우 유효기간이 존재하므로, 만료일 확인 후 사용해 주시기 바랍니다.");

    }

    private void showTotalMon(String totalMon) {
        TextView totalMonView = (TextView) mActivity.findViewById(R.id.totalMon);
        totalMonView.setText(totalMon);
    }

    private void makeList(ArrayList<MonInfoModel> models) {

        ListView list = (ListView) mActivity.findViewById(R.id.listMonInfo);
        if (list == null || models == null) return;


        View header = mActivity.getLayoutInflater().inflate(R.layout.header, null, false);
        list.addHeaderView(header);

        list.setAdapter(new MonListAdapter(mActivity,
                models));

        list.setVisibility(View.VISIBLE);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            }
//        });
    }


      @Override
    protected void addListenerToButton() {

        final LinearLayout showMonList = (LinearLayout) mActivity.findViewById(R.id.showMonList);
        showMonList.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                getMonInfoFromServer();
            }
        });
    }

    private void getMonInfoFromServer() {
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        postParams.add(new BasicNameValuePair("page", "1"));


        new PlusHttpClient(mActivity, this, false).execute(GET_MON_INFO,
                FMApiConstants.GET_MON_INFO, new PlusInputStreamStringConverter(),
                postParams);
    }

    private void getInitialMonInfoFromServer() {
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        postParams.add(new BasicNameValuePair("page", "1"));


        new PlusHttpClient(mActivity, this, false).execute(GET_INITIAL_MON_INFO,
                FMApiConstants.GET_MON_INFO, new PlusInputStreamStringConverter(),
                postParams);
    }
}
