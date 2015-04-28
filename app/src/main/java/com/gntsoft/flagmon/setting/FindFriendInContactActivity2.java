package com.gntsoft.flagmon.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.ContactFriendParser;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FMListParser;
import com.gntsoft.flagmon.server.FriendModel;
import com.gntsoft.flagmon.server.PostDetailParser;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 4. 28.
 */
public class FindFriendInContactActivity2 extends FMCommonActivity implements PlusOnGetDataListener{


    private static final int GET_CONTACT_FRIEND = 55;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find_friend_in_contact2);

       getContactDataOnServer();

    }

    private void getContactDataOnServer() {
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));


        new PlusHttpClient(this, this, false).execute(GET_CONTACT_FRIEND,
                FMApiConstants.GET_CONTACT_FRIEND, new PlusInputStreamStringConverter(),
                postParams);
    }

    @Override
    public void onSuccess(Integer from, Object datas) {

        if (datas == null) return;
        switch (from) {
            case GET_CONTACT_FRIEND:
                makeList(new ContactFriendParser().doIt((String) datas));
                break;

        }
    }

    private void makeList(ArrayList<FriendModel> datas) {
        ListView list = (ListView) findViewById(R.id.listContact);

        if (list == null || datas == null) return;
        list.setAdapter(new ContactListAdapter2(this,
                datas));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }


}
