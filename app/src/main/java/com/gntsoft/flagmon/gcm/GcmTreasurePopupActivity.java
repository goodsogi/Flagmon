package com.gntsoft.flagmon.gcm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;

/**
 * 비콘 쿠폰 팝업
 *
 * @author jeff
 */
public class GcmTreasurePopupActivity extends Activity {


    /**
     * 팝업창 닫기
     *
     * @param v
     */
    public void doClose(View v) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm_treasure_popup);
        showContents();

    }

    private void showContents() {

        String msg = getIntent().getStringExtra(FMConstants.KEY_GCM_MSG);

        TextView pushMsg = (TextView) findViewById(R.id.pushMsg);
        pushMsg.setText(msg);


    }


}
