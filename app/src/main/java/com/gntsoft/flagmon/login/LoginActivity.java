package com.gntsoft.flagmon.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.gcm.FMGcmRegister;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusStringEmailChecker;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by johnny on 15. 2. 27.
 */
public class LoginActivity extends FMCommonActivity implements PlusOnGetDataListener {
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,16}$";
    private static final int LOG_IN = 11;
    private static final int REGISTER_DEVICE = 12;
    final int DRAWABLE_RIGHT = 2;
    private String mUserEmail;

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case LOG_IN:
                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                Log.d("flagmon", "user auth key: " + model.getMsg());
                if (model.getResult().equals("success")) {
                    PlusToaster.doIt(this, "로그인되었습니다");
                    saveLoginInfo(model.getMsg());
                    //푸시 아이디 서버에 저장
                    savePushIdToServer();
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                        PlusToaster.doIt(this,model.getMsg().contains("invalid")? "이메일 주소나 비밀번호가 맞지 않습니다": model.getMsg());
                }
                break;
            case REGISTER_DEVICE:
                Log.d("flagmon", "push registeration result: " + (String) datas);
                break;
        }

    }

    public void launchFindPasswordActivity(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, FindPasswordActivity.class);
        startActivity(intent);

    }

    public void doLogin(View v) {
        PlusClickGuard.doIt(v);

        EditText userEmailView = (EditText) findViewById(R.id.userEmail);
        mUserEmail = userEmailView.getText().toString();

        if (mUserEmail.equals("")) {
            PlusToaster.doIt(this, "이메일을 입력해주세요.");
            return;
        }

        if (!PlusStringEmailChecker.doIt(mUserEmail)) {
            PlusToaster.doIt(this, "아이디는 이메일 주소 형식입니다.");
            return;
        }

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

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("user_email", mUserEmail));
        postParams.add(new BasicNameValuePair("user_pw", userPassword));


        new PlusHttpClient(this, this, false).execute(LOG_IN,
                FMApiConstants.LOG_IN, new PlusInputStreamStringConverter(),
                postParams);

    }

    public void signUp(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

    }

    public void showPolicy(View v) {
        PlusClickGuard.doIt(v);
        Intent intent = new Intent(this, PolicyActivity.class);
        intent.putExtra(FMConstants.KEY_POLICY_TYPE, FMConstants.POLICY_SERVICE);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //임시로 로그인처리
//        saveLoginInfo("a0dcee3f02a21423286469e912d1902a18baec4b427b468cdb176d5f6f668e82");
//        finish();

        registerGcmId();

        addListenerToButton();
    }

    private void savePushIdToServer() {
        EditText userEmailView = (EditText) findViewById(R.id.userEmail);
        String userEmail = userEmailView.getText().toString();

        FMGcmRegister register = new FMGcmRegister(this);
        String regId = register.getRegistrationId();

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("user_email", userEmail));
        postParams.add(new BasicNameValuePair("device_id", regId));


        new PlusHttpClient(this, this, false).execute(REGISTER_DEVICE,
                FMApiConstants.REGISTER_DEVICE, new PlusInputStreamStringConverter(),
                postParams);

    }

    private void registerGcmId() {
        FMGcmRegister register = new FMGcmRegister(this);
        register.doIt();
    }


    private void saveLoginInfo(String userAuthKey) {

        SharedPreferences sharedPreference = getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreference.edit();
        e.putBoolean(FMConstants.KEY_IS_LOGIN, true);
        e.putString(FMConstants.KEY_USER_AUTH_KEY, userAuthKey);
        e.putString(FMConstants.KEY_USER_EMAIL, mUserEmail);
        e.commit();
    }

    private void addListenerToButton() {

        //아래 방법을 사용하니 EditText 눌렀을 때 위치 커서가 표시됨
//        final EditText userEmailView = (EditText) findViewById(R.id.userEmail);
//        userEmailView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    int leftEdgeOfRightDrawable = userEmailView.getRight()
//                            - userEmailView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
//                    // when EditBox has padding, adjust leftEdge like
//                    // leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.edittext_padding_left_right);
//                    if (event.getRawX() >= leftEdgeOfRightDrawable) {
//                        // clicked on clear icon
//                        userEmailView.setText("");
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
//
//        final EditText userPasswordView = (EditText) findViewById(R.id.userPassword);
//        userPasswordView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    int leftEdgeOfRightDrawable = userPasswordView.getRight()
//                            - userPasswordView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
//                    // when EditBox has padding, adjust leftEdge like
//                    // leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.edittext_padding_left_right);
//                    if (event.getRawX() >= leftEdgeOfRightDrawable) {
//                        // clicked on clear icon
//
//                        if (!userPasswordView.isSelected()) {
//                            userPasswordView.setSelected(true);
//                            showPassword(userPasswordView);
//                        } else {
//                            userPasswordView.setSelected(false);
//                            hidePassword(userPasswordView);
//                        }
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });


        ImageView userEmailDelete = (ImageView) findViewById(R.id.userEmailDelete);
        userEmailDelete.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                EditText userEmailView = (EditText) findViewById(R.id.userEmail);
                userEmailView.setText("");
            }
        });

        final ImageView userPasswordDelete = (ImageView) findViewById(R.id.userPasswordDelete);
        userPasswordDelete.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                 EditText userPasswordView = (EditText) findViewById(R.id.userPassword);

                if (!userPasswordDelete.isSelected()) {
                    userPasswordDelete.setSelected(true);
                            showPassword(userPasswordView);
                        } else {
                    userPasswordDelete.setSelected(false);
                            hidePassword(userPasswordView);
                        }
            }
        });



    }
    private void hidePassword(EditText userPasswordView) {
        //InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD 2개를 함께 사용해야 비밀번호가 안보임
        userPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }



    private boolean showPassword(EditText userPasswordView) {

        userPasswordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        return false;
    }

    private boolean isPasswordValid(String userPassword) {
        //8자 ~ 16자 사이 영숫자 혼합 체크
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

        Matcher matcher = pattern.matcher(userPassword);

        return matcher.matches();

    }


}
