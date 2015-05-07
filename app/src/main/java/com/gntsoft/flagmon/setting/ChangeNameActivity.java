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
        String lastName = getIntent().getStringExtra(FMConstants.KEY_LAST_NAME);
        String firstName = getIntent().getStringExtra(FMConstants.KEY_FIRST_NAME);

        EditText lastNameView = (EditText) findViewById(R.id.lastName);
        EditText firstNameView = (EditText) findViewById(R.id.firstName);

        lastNameView.setText(lastName);
        firstNameView.setText(firstName);


    }

    public void complete(View v) {
        EditText lastNameView = (EditText) findViewById(R.id.lastName);
        EditText firstNameView = (EditText) findViewById(R.id.firstName);

        String lastName = lastNameView.getText().toString();
        String firstName = firstNameView.getText().toString();

        if(lastName.equals("")) {
            PlusToaster.doIt(this,"성을 입력해주세요");
            return;
        }

        if(firstName.equals("")) {
            PlusToaster.doIt(this,"이름을 입력해주세요");
            return;
        }


        Intent intent = new Intent();

        intent.putExtra(FMConstants.KEY_USER_NAME, lastName+firstName);

        setResult(RESULT_OK, intent);

        finish();

    }
}
