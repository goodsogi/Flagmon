package com.gntsoft.flagmon.gcm;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.pluslibrary.common.CommonDialog;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 4. 20.
 */
public class PinPostPopupActivity extends FMCommonActivity implements PlusOnGetDataListener {
    private final int PERFORM_PIN = 45;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_pin_post);
        showContents();

    }

    private void showContents() {

    }


    public void cancel(View v) {
        finish();
    }

    @Override
    public void onSuccess(Integer from, Object datas) {

        if (datas == null) return;
        switch (from) {

            case PERFORM_PIN:
                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(this, model.getResult().contains("succes") ? "핀을 꽂았습니다" : "핀을 꽂지 못했습니다");
                if (model.getResult().contains("succes")) {
                    //추가 액션??
                    finish();
                }
                break;

        }
    }



    public void pinPost(View v) {
        String idx = getIntent().getStringExtra(FMConstants.KEY_POST_IDX);
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        postParams.add(new BasicNameValuePair("photo_idx", idx));


        new PlusHttpClient(this, this, false).execute(PERFORM_PIN,
                FMApiConstants.PERFORM_PIN, new PlusInputStreamStringConverter(),
                postParams);
    }
}