package com.gntsoft.flagmon.login;

import android.app.Activity;
import android.os.Bundle;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;

/**
 * Created by johnny on 15. 2. 26.
 */
public class PolicyActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        //!! 해당 약관 표시
        int policyType = getIntent().getIntExtra(FMConstants.KEY_POLICY_TYPE, FMConstants.POLICY_SERVICE);

    }
}
