package com.gntsoft.flagmon.login;

import android.app.Activity;
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

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.login.PolicyActivity;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusOnClickListener;

/**
 * Created by johnny on 15. 2. 26.
 */
public class SecondSignUpActivity extends Activity {
    private EditText mUserSexView;
    String[] sexs = {"남", "여"};
    final int DRAWABLE_LEFT = 0;
    final int DRAWABLE_TOP = 1;
    final int DRAWABLE_RIGHT = 2;
    final int DRAWABLE_BOTTOM = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_signup);

        addButtonListener();
        //!! 첫번째 가입화면의 사용자 정보 가져와서 처리
        String userEmail = getIntent().getStringExtra(FMConstants.KEY_USER_EMAIL);
        String userPassword = getIntent().getStringExtra(FMConstants.KEY_USER_PASSWORD);
        String userName = getIntent().getStringExtra(FMConstants.KEY_USER_NAME);


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
                if(isChecked&&privacyPolicy.isChecked()&&locationPolicy.isChecked()) enableFinishButton();
            }
        });


        privacyPolicy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked&&servicePolicy.isChecked()&&locationPolicy.isChecked()) enableFinishButton();
            }
        });


        locationPolicy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked&&privacyPolicy.isChecked()&&servicePolicy.isChecked()) enableFinishButton();
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
        intent.putExtra(FMConstants.KEY_POLICY_TYPE,getPolicyType(v));
        startActivity(intent);
    }

    private int getPolicyType(View v) {
        switch(v.getId()) {
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
        //!!구현
    }
}
