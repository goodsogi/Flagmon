package com.gntsoft.flagmon.login;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;

/**
 * Created by johnny on 15. 2. 26.
 */
public class PolicyActivity extends FMCommonActivity implements
        PlusOnGetDataListener {

    private static final int GET_POLICY = 0;

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case GET_POLICY:
                showContent((String) datas);
                break;
        }

    }

    public void showServicePolicy(View v) {
        PlusClickGuard.doIt(v);
        getDataFromServer(FMConstants.POLICY_SERVICE);
    }

    public void showPrivacyPolicy(View v) {
        PlusClickGuard.doIt(v);
        getDataFromServer(FMConstants.POLICY_PRIVACY);
    }

    public void showLocationPolicy(View v) {
        PlusClickGuard.doIt(v);
        getDataFromServer(FMConstants.POLICY_LOCATION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        //!! 해당 약관 표시
        int policyType = getIntent().getIntExtra(FMConstants.KEY_POLICY_TYPE, FMConstants.POLICY_SERVICE);

        getDataFromServer(policyType);

    }

    private void getDataFromServer(int policyType) {
        new PlusHttpClient(this, this, false).execute(GET_POLICY,
                getPolicyUrl(policyType), new PlusInputStreamStringConverter());
    }

    private String getPolicyUrl(int policyType) {

        //url 수정!!
        switch (policyType) {
            case FMConstants.POLICY_SERVICE:
                return FMApiConstants.GET_POLICY_SERVICE;

            case FMConstants.POLICY_LOCATION:
                return FMApiConstants.GET_POLICY_LOCATION;

            case FMConstants.POLICY_PRIVACY:
                return FMApiConstants.GET_POLICY_PRIVACY;


        }


        return null;
    }

    private void showContent(String datas) {
        TextView content = (TextView) findViewById(R.id.policyContent);
        content.setText(Html.fromHtml(datas));
    }
}
