package com.gntsoft.flagmon.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.internal.widget.TintEditText;
import android.view.View;
import android.widget.EditText;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.pluslibrary.utils.PlusToaster;

/**
 * Created by johnny on 15. 5. 4.
 */
public class ChangeStatusMessageActivity extends FMCommonActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_status_message);
        showStatusmessage();
    }

    private void showStatusmessage() {
        String statusMessage = getIntent().getStringExtra(FMConstants.KEY_STATUS_MESSAGE);
        if(statusMessage.equals(""))return;

        EditText statusMessageView = (EditText) findViewById(R.id.statusMessage);
        statusMessageView.setText(statusMessage);
    }

    public void complete(View v) {
        EditText statusMessageView = (EditText) findViewById(R.id.statusMessage);
        String statusMessage = statusMessageView.getText().toString();

        if(statusMessage.equals("")) {
            PlusToaster.doIt(this, "상태메시지를 입력해주세요");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(FMConstants.KEY_STATUS_MESSAGE, statusMessage);
        setResult(RESULT_OK, intent);
        finish();
    }
}
