package com.gntsoft.flagmon.comment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.login.LoginActivity;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.server.ReplyParser;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 2. 13.
 */
public class CommentActivity extends FMCommonActivity implements
        PlusOnGetDataListener {

    private static final int GET_COMMENTS = 0;
    private static final int SEND_COMMENT = 1;

    public void sendComment(View v) {
        PlusClickGuard.doIt(v);
        if (LoginChecker.isLogIn(this)) {
            sendCommentToServer();
            hideKeyboard();
            deleteComment();
        }
        else launchLoginActivity(v);
    }

    private void deleteComment() {
        EditText commentInput = (EditText) findViewById(R.id.commentInput);
        commentInput.setText("");
    }

    private void hideKeyboard() {
        EditText commentInput = (EditText) findViewById(R.id.commentInput);

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(commentInput.getWindowToken(), 0);
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null) return;
        switch (from) {

            case GET_COMMENTS:
                makeList(new ReplyParser().doIt((String) datas));

                break;
            case SEND_COMMENT:

                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(this, model.getResult().equals("success") ? "댓글을 달았습니다" : "댓글을 달지 못했습니다");

                if(model.getResult().equals("success")) getDataFromServer();
                break;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        if(LoginChecker.isLogIn(this)) deleteHint();
        getDataFromServer();
    }

    private void deleteHint() {
        EditText commentInput = (EditText) findViewById(R.id.commentInput);
        commentInput.setHint("");
    }


    private void getDataFromServer() {


        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        postParams.add(new BasicNameValuePair("photo_idx", getIntent().getStringExtra(FMConstants.KEY_POST_IDX)));
        //가장 마지막으로 조회한 idx. 만일 첫 조회인 경우는 공란??
        //postParams.add(new BasicNameValuePair("idx", "")));


        new PlusHttpClient(this, this, false).execute(GET_COMMENTS,
                FMApiConstants.GET_COMMENTS, new PlusInputStreamStringConverter(),
                postParams);
    }


    private void sendCommentToServer() {
        String userEmail = getIntent().getStringExtra(FMConstants.KEY_USER_EMAIL);
        String userPassword = getIntent().getStringExtra(FMConstants.KEY_USER_PASSWORD);
        String userName = getIntent().getStringExtra(FMConstants.KEY_USER_NAME);


        EditText commentInputView = (EditText) findViewById(R.id.commentInput);
        String commentInput = commentInputView.getText().toString();

        if (commentInput.equals("")) {
            PlusToaster.doIt(this, getString(R.string.enter_comment));
            return;
        }

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        postParams.add(new BasicNameValuePair("memo", commentInput));
        postParams.add(new BasicNameValuePair("photo_idx", getIntent().getStringExtra(FMConstants.KEY_POST_IDX)));


        new PlusHttpClient(this, this, false).execute(SEND_COMMENT,
                FMApiConstants.SEND_COMMENT, new PlusInputStreamStringConverter(),
                postParams);
    }


    private void launchLoginActivity(View v) {

        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }


    private void makeList(final ArrayList<FMModel> datas) {

        ListView list = (ListView) findViewById(R.id.list_reply);

        if (list == null || datas == null) return;
        list.setAdapter(new ListAdapter(this,
                datas));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });


    }
}
