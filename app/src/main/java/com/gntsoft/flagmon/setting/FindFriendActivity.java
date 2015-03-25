package com.gntsoft.flagmon.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FriendModel;
import com.gntsoft.flagmon.server.GotFriendRequestParser;
import com.gntsoft.flagmon.server.SentFriendRequestParser;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 3. 3.
 */
public class FindFriendActivity extends FMCommonActivity implements
        PlusOnGetDataListener {
    private static final int GET_SENT_FRIEND_REQUEST = 0;
    private static final int GET_GOT_FRIEND_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
        getGotFriendRequestDataFromServer();
        getSentFriendRequestDataFromServer();
    }

    private void getSentFriendRequestDataFromServer() {
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));


        new PlusHttpClient(this, this, false).execute(GET_SENT_FRIEND_REQUEST,
                FMApiConstants.GET_SENT_FRIEND_REQUEST, new PlusInputStreamStringConverter(),
                postParams);
    }

    private void getGotFriendRequestDataFromServer() {
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));


        new PlusHttpClient(this, this, false).execute(GET_GOT_FRIEND_REQUEST,
                FMApiConstants.GET_GOT_FRIEND_REQUEST, new PlusInputStreamStringConverter(),
                postParams);
    }


    public void findFriendOnFacebook(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, FindFriendInFacebookActivity.class);

        startActivity(intent);

    }

    public void findFriendOnTwitter(View v) {
        PlusClickGuard.doIt(v);
        Intent intent = new Intent(this, FindFriendInTwitterActivity.class);

        startActivity(intent);

    }


    public void findFriendInContact(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, FindFriendInContactActivity.class);

        startActivity(intent);
    }


    public void searchFriendByName(View v) {
        PlusClickGuard.doIt(v);
        Intent intent = new Intent(this, SearchFriendByNameActivity.class);

        startActivity(intent);

    }


    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case GET_GOT_FRIEND_REQUEST:
                makeGotFriendRequestList(new GotFriendRequestParser().doIt((String) datas));
                break;
            case GET_SENT_FRIEND_REQUEST:
                makeSentFriendRequestList(new SentFriendRequestParser().doIt((String) datas));
                break;
        }

    }

    private void makeSentFriendRequestList(final ArrayList<FriendModel> datas) {
        ListView list = (ListView)
                findViewById(R.id.list_sent_friend_request);

        if (list == null) return;
        list.setAdapter(new GotFriendRequestListAdapter(this,
                datas));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

    }

    private void makeGotFriendRequestList(final ArrayList<FriendModel> datas) {
        ListView list = (ListView) findViewById(R.id.list_got_friend_request);

        if (list == null) return;
        list.setAdapter(new SentFriendRequestListAdapter(this,
                datas));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }
}
