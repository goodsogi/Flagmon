package com.gntsoft.flagmon.gcm;

import android.app.Activity;
import android.content.Intent;
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
    public void close(View v) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm_treasure_popup);
        showContents();

    }

    public void confirm(View v) {
        launchPinPostPopup();
    }

    private void launchPinPostPopup() {
        String postIdx = getIntent().getStringExtra(FMConstants.KEY_POST_IDX);
        Intent intent = new Intent(getApplicationContext(),
                PinPostPopupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(FMConstants.KEY_POST_IDX, postIdx);

        getApplicationContext().startActivity(intent);
        finish();
    }

    private void showContents() {

        String msg = getIntent().getStringExtra(FMConstants.KEY_GCM_MSG);

        TextView pushMsg = (TextView) findViewById(R.id.pushMsg);
        pushMsg.setText(msg);



    }


}
