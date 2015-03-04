package com.gntsoft.flagmon.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusStringEmailChecker;
import com.pluslibrary.utils.PlusToaster;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by johnny on 15. 2. 27.
 */
public class LoginActivity  extends Activity implements PlusOnGetDataListener {
    final int DRAWABLE_LEFT = 0;
    final int DRAWABLE_TOP = 1;
    final int DRAWABLE_RIGHT = 2;
    final int DO_LOGIN = 77;

    final int DRAWABLE_BOTTOM = 3;
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,16}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addButtonListener();
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case DO_LOGIN:
                //!!로그인 성공 실패 처리
                saveLoginInfo();
                break;
        }

    }

    private void saveLoginInfo() {

        SharedPreferences sharedPreference = getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreference.edit();
        e.putBoolean(FMConstants.KEY_IS_LOGIN, true);
        e.commit();
    }

    private void addButtonListener() {
        final EditText userEmailView = (EditText) findViewById(R.id.userEmail);
        userEmailView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int leftEdgeOfRightDrawable = userEmailView.getRight()
                            - userEmailView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    // when EditBox has padding, adjust leftEdge like
                    // leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.edittext_padding_left_right);
                    if (event.getRawX() >= leftEdgeOfRightDrawable) {
                        // clicked on clear icon
                        userEmailView.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        final EditText userPasswordView = (EditText) findViewById(R.id.userPassword);
        userPasswordView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int leftEdgeOfRightDrawable = userPasswordView.getRight()
                            - userPasswordView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    // when EditBox has padding, adjust leftEdge like
                    // leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.edittext_padding_left_right);
                    if (event.getRawX() >= leftEdgeOfRightDrawable) {
                        // clicked on clear icon
                        showPassword(userPasswordView);
                        return true;
                    }
                }
                return false;
            }
        });


    }

    private boolean showPassword(EditText userPasswordView) {

        userPasswordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        return false;
    }

   public void goToFindPassword(View v) {
       PlusClickGuard.doIt(v);

       Intent intent = new Intent(this, FindPasswordActivity.class);
       startActivity(intent);

   }

    public void doLogin(View v) {
        PlusClickGuard.doIt(v);

//        EditText userEmailView = (EditText) findViewById(R.id.userEmail);
//        String userEmail = userEmailView.getText().toString();
//
//        if(userEmail.equals("")) {
//            PlusToaster.doIt(this,"이메일을 입력해주세요.");
//            return;
//        }
//
//        if(!PlusStringEmailChecker.doIt(userEmail)) {
//            PlusToaster.doIt(this,"아이디는 이메일 주소 형식입니다.");
//            return;
//        }
//
//        EditText userPasswordView = (EditText) findViewById(R.id.userPassword);
//        String userPassword = userPasswordView.getText().toString();
//
//        if(userPassword.equals("")) {
//            PlusToaster.doIt(this,"비밀번호를 입력해주세요.");
//            return;
//        }
//
//        if(!isPasswordValid(userPassword)) {
//            PlusToaster.doIt(this,"비밀번호는 영문 숫자 조합 8자리 이상입니다.");
//            return;
//
//        }
//
//        //구현!!
//        PlusToaster.doIt(this,"준비중...");
//
        //테스트!!

        SharedPreferences sharedPreference = getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreference.edit();
        e.putBoolean(FMConstants.KEY_IS_LOGIN, true);
        e.commit();

    }

    private boolean isPasswordValid(String userPassword) {
        //8자 ~ 16자 사이 영숫자 혼합 체크
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

        Matcher matcher = pattern.matcher(userPassword);

        return matcher.matches();

    }

    public void signUp(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

    }

    public void showPolicy(View v) {
        PlusClickGuard.doIt(v);
        Intent intent = new Intent(this, PolicyActivity.class);
        intent.putExtra(FMConstants.KEY_POLICY_TYPE,FMConstants.POLICY_SERVICE);
        startActivity(intent);
    }


}
