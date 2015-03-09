package com.gntsoft.flagmon.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FMMapParser;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusStringEmailChecker;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by johnny on 15. 2. 26.
 */
public class SignUpActivity extends Activity implements PlusOnGetDataListener {
    final int DRAWABLE_LEFT = 0;
    final int DRAWABLE_TOP = 1;
    final int DRAWABLE_RIGHT = 2;
    final int DRAWABLE_BOTTOM = 3;
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,16}$";

    private static final int CHECK_EMAIL = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        addButtonListener();

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
        final EditText userNameView = (EditText) findViewById(R.id.userName);
        userNameView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int leftEdgeOfRightDrawable = userNameView.getRight()
                            - userNameView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    // when EditBox has padding, adjust leftEdge like
                    // leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.edittext_padding_left_right);
                    if (event.getRawX() >= leftEdgeOfRightDrawable) {
                        // clicked on clear icon
                        userNameView.setText("");
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


    public void checkEmail(View v) {
        PlusClickGuard.doIt(v);

        EditText userEmailView = (EditText) findViewById(R.id.userEmail);
        String userEmail = userEmailView.getText().toString();

        if (userEmail.equals("")) {
            PlusToaster.doIt(this, "이메일을 입력해주세요.");
            return;
        }

        checkIsEmailAvailable(userEmail);


    }

    private void checkIsEmailAvailable(String userEmail) {
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("user_email", userEmail));

        new PlusHttpClient(this, this, false).execute(CHECK_EMAIL,
                FMApiConstants.CHECK_EMAIL, new PlusInputStreamStringConverter(),
                postParams);
    }

    private boolean isPasswordValid(String userPassword) {
        //8자 ~ 16자 사이 영숫자 혼합 체크
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

        Matcher matcher = pattern.matcher(userPassword);

        return matcher.matches();

    }

    public void showPolicy(View v) {
        PlusClickGuard.doIt(v);
        Intent intent = new Intent(this, PolicyActivity.class);
        intent.putExtra(FMConstants.KEY_POLICY_TYPE, FMConstants.POLICY_SERVICE);
        startActivity(intent);
    }

    public void logIn(View v) {
        PlusClickGuard.doIt(v);
        //!!구현 
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        switch (from) {

            case CHECK_EMAIL:

                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(this, model.getResult().equals("success") ? "이메일을 사용할 수 있습니다" :
                        model.getMsg().equals("not email")? "이메일이 유효하지 않습니다":"이미 사용중인 이메일입니다");
                if(model.getResult().equals("success")) checkPasswordAndName();
                break;

        }

    }

    private void checkPasswordAndName() {
        EditText userEmailView = (EditText) findViewById(R.id.userEmail);
        String userEmail = userEmailView.getText().toString();

        EditText userPasswordView = (EditText) findViewById(R.id.userPassword);
        String userPassword = userPasswordView.getText().toString();

        if (userPassword.equals("")) {
            PlusToaster.doIt(this, "비밀번호를 입력해주세요.");
            return;
        }

        if (!isPasswordValid(userPassword)) {
            PlusToaster.doIt(this, "비밀번호는 영문 숫자 조합 8자리 이상입니다.");
            return;

        }

        EditText userNameView = (EditText) findViewById(R.id.userName);
        String userName = userNameView.getText().toString();

        if (userName.equals("")) {
            PlusToaster.doIt(this, "사용자 이름을 입력해주세요.");
            return;
        }

goToSecondSignUpActivity(userEmail, userPassword, userName);

    }

    private void goToSecondSignUpActivity(String userEmail, String userPassword, String userName) {
        finish();
        Intent intent = new Intent(this, SecondSignUpActivity.class);
        intent.putExtra(FMConstants.KEY_USER_EMAIL, userEmail);
        intent.putExtra(FMConstants.KEY_USER_PASSWORD, userPassword);
        intent.putExtra(FMConstants.KEY_USER_NAME, userName);

        startActivity(intent);
    }

}
