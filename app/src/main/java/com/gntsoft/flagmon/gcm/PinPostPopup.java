package com.gntsoft.flagmon.gcm;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;

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
public class PinPostPopup extends CommonDialog implements PlusOnGetDataListener {
    private final int PERFORM_PIN = 45;
    private String mIdx;

    public PinPostPopup(Activity activity, Fragment fragment, int layoutId) {
        super(activity, fragment, layoutId, R.style.CustomDialog);

    }

    public void setIdx(String idx) {
        mIdx = idx;
    }

    @Override
    public void onSuccess(Integer from, Object datas) {

        if (datas == null) return;
        switch (from) {

            case PERFORM_PIN:
                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(mActivity, model.getResult().contains("succes") ? "스크랩되었습니다" : "스크랩되지 못했습니다");
                if (model.getResult().contains("succes")) {
                    //추가 액션??
                }
                break;

        }
    }

    public String getUserAuthKey() {
        SharedPreferences sharedPreference = mActivity.getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getString(FMConstants.KEY_USER_AUTH_KEY, "");
    }

    @Override
    protected void addListenerToButton() {
        Button pinPost = (Button) findViewById(R.id.pinPost);
        Button cancel = (Button) findViewById(R.id.cancel);

        pinPost.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                PinPostPopup.this.dismiss();
                pinPost();
            }
        });

        cancel.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                PinPostPopup.this.dismiss();
            }
        });


    }

    private void pinPost() {
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        postParams.add(new BasicNameValuePair("photo_idx", mIdx));


        new PlusHttpClient(mActivity, this, false).execute(PERFORM_PIN,
                FMApiConstants.PERFORM_PIN, new PlusInputStreamStringConverter(),
                postParams);
    }
}