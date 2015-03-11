package com.gntsoft.flagmon.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.pluslibrary.utils.PlusToaster;

/**
 * Created by johnny on 15. 2. 26.
 */
public class PolicyActivity extends FMCommonActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        //!! 해당 약관 표시
        int policyType = getIntent().getIntExtra(FMConstants.KEY_POLICY_TYPE, FMConstants.POLICY_SERVICE);

    }


    public void showServicePolicy(View v) {
//구현!!
        PlusToaster.doIt(this, "준비중...");
    }

    public void showPrivacyPolicy(View v) {
//구현!!
        PlusToaster.doIt(this, "준비중...");
    }

    public void showLocationPolicy(View v) {
//구현!!
        PlusToaster.doIt(this, "준비중...");
    }
}
