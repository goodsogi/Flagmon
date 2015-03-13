package com.gntsoft.flagmon.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMApiConstants;
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
 * Created by johnny on 15. 2. 27.
 */
public class FindPasswordActivity  extends FMCommonActivity implements
        PlusOnGetDataListener {
    final int DRAWABLE_RIGHT = 2;
    private static final int REQUEST_PASSWORD = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        addButtonListener();
    }

    private void addButtonListener() {
        final EditText userEmailView = (EditText) findViewById(R.id.userEmail);
        userEmailView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int leftEdgeOfRightDrawable = userEmailView.getRight()
                            - userEmailView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    // when EditBox has padding, adjust leftEdge like
                    // leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.edittext_padding_left_right);
                    if (event.getRawX() >= leftEdgeOfRightDrawable) {
                        // clicked on clear icon
                        userEmailView.setText("");
                        return true;
                    }
                }
                return false;
            }
        });


    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case REQUEST_PASSWORD:
                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(this,model.getResult().equals("success")?"비밀번호 재설정 메일을 발송하였습니다.":"비밀번호 재설정 메일을 발송하지 못했습니다.");
                if(model.getResult().equals("success")) {
                    //추가 액션??
                }
                break;

        }

    }




    public void requestPassword(View v) {
        //특정 사용자 아이디등 처리!!
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        //postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_NEIGHBOR));
        if(LoginChecker.isLogIn(this)) { postParams.add(new BasicNameValuePair("key", getUserAuthKey()));}


        new PlusHttpClient(this, this, false).execute(REQUEST_PASSWORD,
                FMApiConstants.REQUEST_PASSWORD, new PlusInputStreamStringConverter(),
                postParams);

    }




    public void showPolicy(View v) {
        PlusClickGuard.doIt(v);
        Intent intent = new Intent(this, PolicyActivity.class);
        intent.putExtra(FMConstants.KEY_POLICY_TYPE,FMConstants.POLICY_SERVICE);
        startActivity(intent);
    }



}