package com.gntsoft.flagmon.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 2. 26.
 */
public class SecondSignUpActivity extends FMCommonActivity implements PlusOnGetDataListener {
    private static final int SIGN_UP = 11;
    final int DRAWABLE_RIGHT = 2;
    String[] sexs = {"남", "여"};
    private EditText mUserSexView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_signup);

        addButtonListener();


    }

    private void addButtonListener() {
        mUserSexView = (EditText) findViewById(R.id.userSex);
        mUserSexView.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                showSexDialog();
            }
        });

        mUserSexView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int leftEdgeOfRightDrawable = mUserSexView.getRight()
                            - mUserSexView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    // when EditBox has padding, adjust leftEdge like
                    // leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.edittext_padding_left_right);
                    if (event.getRawX() >= leftEdgeOfRightDrawable) {
                        // clicked on clear icon
                        mUserSexView.setText("");
                        return true;
                    }
                }
                return false;
            }
        });


        final EditText userAgeView = (EditText) findViewById(R.id.userAge);
        userAgeView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int leftEdgeOfRightDrawable = userAgeView.getRight()
                            - userAgeView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    // when EditBox has padding, adjust leftEdge like
                    // leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.edittext_padding_left_right);
                    if (event.getRawX() >= leftEdgeOfRightDrawable) {
                        // clicked on clear icon
                        userAgeView.setText("");
                        return true;
                    }
                }
                return false;
            }
        });


        final CheckBox servicePolicy = (CheckBox) findViewById(R.id.checkbox_service);
        final CheckBox privacyPolicy = (CheckBox) findViewById(R.id.checkbox_privacy);
        final CheckBox locationPolicy = (CheckBox) findViewById(R.id.checkbox_location);
        servicePolicy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && privacyPolicy.isChecked() && locationPolicy.isChecked())
                    enableFinishButton();
            }
        });


        privacyPolicy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && servicePolicy.isChecked() && locationPolicy.isChecked())
                    enableFinishButton();
            }
        });


        locationPolicy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && privacyPolicy.isChecked() && servicePolicy.isChecked())
                    enableFinishButton();
            }
        });
    }

    private void enableFinishButton() {
        Button finish = (Button) findViewById(R.id.finishSignup);
        finish.setEnabled(true);
    }

    private void showSexDialog() {

        AlertDialog.Builder ab = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("선택해주세요.");
        ab.setItems(sexs, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                changeSex(whichButton);

            }
        }).setNegativeButton("닫기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        ab.show();
    }

    private void changeSex(int whichButton) {
        mUserSexView.setText(sexs[whichButton]);
    }

    public void showPolicy(View v) {
        PlusClickGuard.doIt(v);
        Intent intent = new Intent(this, PolicyActivity.class);
        intent.putExtra(FMConstants.KEY_POLICY_TYPE, getPolicyType(v));
        startActivity(intent);
    }

    private int getPolicyType(View v) {
        switch (v.getId()) {
            case R.id.text_service:
                return FMConstants.POLICY_SERVICE;

            case R.id.text_privacy:
                return FMConstants.POLICY_PRIVACY;
            case R.id.text_location:
                return FMConstants.POLICY_LOCATION;

        }
        return FMConstants.POLICY_SERVICE;
    }


    public void finishSignUp(View v) {
        PlusClickGuard.doIt(v);

        String userEmail = getIntent().getStringExtra(FMConstants.KEY_USER_EMAIL);
        String userPassword = getIntent().getStringExtra(FMConstants.KEY_USER_PASSWORD);
        String userName = getIntent().getStringExtra(FMConstants.KEY_USER_NAME);


        EditText userAgeView = (EditText) findViewById(R.id.userAge);
        String userAge = userAgeView.getText().toString();

        if (userAge.equals("")) {
            PlusToaster.doIt(this, "나이를 입력해주세요");
            return;
        }

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("user_email", userEmail));
        postParams.add(new BasicNameValuePair("user_pw", userPassword));
        postParams.add(new BasicNameValuePair("user_gender", getUserSex()));
        postParams.add(new BasicNameValuePair("user_name", userName));
        // postParams.add(new BasicNameValuePair("where", mArea));
        //생년월일에 나이??
        postParams.add(new BasicNameValuePair("user_birth", userAge));

//        SharedPreferences sharedPreference = getSharedPreferences(
//                PlusConstants.PREF_NAME, Context.MODE_PRIVATE);
//        String token = sharedPreference.getString(
//                PlusGcmRegister.PROPERTY_REG_ID, "");
//        postParams.add(new BasicNameValuePair("deviceid", token));
//        postParams.add(new BasicNameValuePair("alarm2", soundNoti));
//        postParams.add(new BasicNameValuePair("alarm1", vibrationNoti));

        new PlusHttpClient(this, this, false).execute(SIGN_UP,
                FMApiConstants.SIGN_UP, new PlusInputStreamStringConverter(),
                postParams);
    }

    public String getUserSex() {
        return mUserSexView.getText().toString().equals(sexs[0]) ? "M" : "W";
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null) return;
        switch (from) {

            case SIGN_UP:

                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(this, model.getResult().equals("success") ? "회원가입되었습니다" : "회원가입되지 못했습니다");
                if (model.getResult().equals("success")) finish();
                break;

        }

    }
}
