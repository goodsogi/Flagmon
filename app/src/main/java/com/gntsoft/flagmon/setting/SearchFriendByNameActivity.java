package com.gntsoft.flagmon.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FriendListParser;
import com.gntsoft.flagmon.server.FriendModel;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 3. 11.
 */
public class SearchFriendByNameActivity extends FMCommonActivity implements
        PlusOnGetDataListener {

    private static final int SEARCH_fRIENDS_BY_NAME = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend_by_name);

        init();


    }

    private void init() {
        final EditText editText = (EditText) findViewById(R.id.searchInput);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch(v);
                }
                //return false라고 하면 키보드가 내려감
                return false;
            }
        });

        editText.postDelayed(new Runnable() {
            public void run() {
                InputMethodManager manager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                manager.showSoftInput(editText, 0);
            }
        }, 100);

    }

    public void doSearch(View v) {
        EditText searchView = (EditText) findViewById(R.id.searchInput);
        String keyword = searchView.getText().toString();

        if (keyword.equals("")) {
            PlusToaster.doIt(this, "검색어를 입력해주세요");
            return;

        }

        performSearch(keyword);

    }

    private void performSearch(String keyword) {

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("name", keyword));
        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));


        new PlusHttpClient(this, this, false).execute(SEARCH_fRIENDS_BY_NAME,
                FMApiConstants.SEARCH_FRIENDS_BY_NAME, new PlusInputStreamStringConverter(),
                postParams);
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case SEARCH_fRIENDS_BY_NAME:
                makeList(new FriendListParser().doIt((String) datas));
                break;
        }

    }

    private void makeList(ArrayList<FriendModel> model) {
        ListView list = (ListView) findViewById(R.id.listSearch);

        if (list == null || model == null) return;
        list.setAdapter(new SearchFriendListAdapter(this,
                model));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

    }


}
