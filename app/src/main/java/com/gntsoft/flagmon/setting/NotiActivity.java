package com.gntsoft.flagmon.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.friend.ListAdapter;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FMListParser;
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.server.NotiModel;
import com.gntsoft.flagmon.server.NotiParser;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 4. 21.
 */
public class NotiActivity extends FMCommonActivity implements PlusOnGetDataListener {

    final int GET_NOTI_DATA = 23;

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case GET_NOTI_DATA:
                makeList(new NotiParser().doIt((String) datas));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti);
        getNotiDataOnServer();
    }

    private void getNotiDataOnServer() {



        new PlusHttpClient(this, this, false).execute(GET_NOTI_DATA,
                FMApiConstants.GET_NOTI_DATA, new PlusInputStreamStringConverter());
    }

    private void makeList(final ArrayList<NotiModel> datas) {

        ListView list = (ListView) findViewById(R.id.listNoti);

        if (list == null || datas == null) return;
        list.setAdapter(new NotiListAdapter(this,
                datas));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

}
