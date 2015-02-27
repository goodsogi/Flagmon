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
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;

/**
 * Created by johnny on 15. 2. 27.
 */
public class FindPasswordActivity  extends Activity {
    final int DRAWABLE_LEFT = 0;
    final int DRAWABLE_TOP = 1;
    final int DRAWABLE_RIGHT = 2;
    final int DRAWABLE_BOTTOM = 3;
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,16}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
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

    public void requestPassword(View v) {
        PlusClickGuard.doIt(v);

        //구현!!
        PlusToaster.doIt(this, "준비중...");

    }

    public void goBackLogin(View v) {
        PlusClickGuard.doIt(v);

        finish();

    }

    public void showPolicy(View v) {
        PlusClickGuard.doIt(v);
        Intent intent = new Intent(this, PolicyActivity.class);
        intent.putExtra(FMConstants.KEY_POLICY_TYPE,FMConstants.POLICY_SERVICE);
        startActivity(intent);
    }



}