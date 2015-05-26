package com.gntsoft.flagmon.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.pluslibrary.utils.PlusToaster;

/**
 * Created by johnny on 15. 5. 4.
 */
public class ChangeNameActivity extends FMCommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        showName();

    }

    private void showName() {
        String userName = getIntent().getStringExtra(FMConstants.KEY_USER_NAME);

        EditText userNameView = (EditText) findViewById(R.id.userName);

        userNameView.setText(userName);


    }

    public void complete(View v) {
        EditText userNameView = (EditText) findViewById(R.id.userName);

        String userName = userNameView.getText().toString();

        if(userName.equals("")) {
            PlusToaster.doIt(this,"이름을 입력해주세요");
            return;
        }



        Intent intent = new Intent();

        intent.putExtra(FMConstants.KEY_USER_NAME, userName);

        setResult(RESULT_OK, intent);

        finish();

    }
}
