package com.gntsoft.flagmon.myalbum;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FMListParser;
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.google.gson.Gson;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 3. 4.
 */
public class GroupPostActivity extends FMCommonActivity implements
        PlusOnGetDataListener {
    private static final int GET_LIST_DATA = 0;
    private static final int MAKE_ALBUM = 11;
    private GroupPostListAdapter mAdapter;
    private String idxs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_post);
        addListenerToView();
        getDataFromServer();
    }

    private void addListenerToView() {

        EditText albumNameInput = (EditText) findViewById(R.id.albumNameInput);
        final Button completePost = (Button) findViewById(R.id.completePost);
        albumNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edit) {
                String s = edit.toString();
                if (s.length() > 0 && !completePost.isEnabled())
                    completePost.setEnabled(true);
            }
        });

    }

    public void getDataFromServer() {

//내 포스트 가져오기
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_NEIGHBOR));
        if (LoginChecker.isLogIn(this)) {
            postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        }


        new PlusHttpClient(this, this, false).execute(GET_LIST_DATA,
                FMApiConstants.GET_LIST_DATA, new PlusInputStreamStringConverter(),
                postParams);
    }

    private void makeList(final ArrayList<FMModel> datas) {

        //row 선택하는 기능 구현!!
        ListView list = (ListView) findViewById(R.id.list_group_post);

        if (list == null) return;
        mAdapter = new GroupPostListAdapter(this,
                datas);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });


    }


    public void completePost(View v) {
//구현!!
        PlusToaster.doIt(this, "준비중...");

        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("idxs", getIdxs()));
        if (LoginChecker.isLogIn(this)) {
            postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        }


        new PlusHttpClient(this, this, false).execute(MAKE_ALBUM,
                FMApiConstants.MAKE_ALBUM, new PlusInputStreamStringConverter(),
                postParams);
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case GET_LIST_DATA:
                makeList(new FMListParser().doIt((String) datas));
                break;

            case MAKE_ALBUM:
                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(this, model.getResult().equals("success") ? "앨범 만들었습니다" : "앨범을 만들지 못했습니다");
                if (model.getResult().equals("success")) {
                    //추가 액션??
                }
        }

    }

    public String getIdxs() {


        return new Gson().toJson(mAdapter.getSelectedPostIdxs());
    }
}
