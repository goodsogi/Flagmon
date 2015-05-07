package com.gntsoft.flagmon.setting;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.login.SecondSignUpActivity;
import com.gntsoft.flagmon.neighbor.MapFragment;
import com.pluslibrary.common.CommonDialog;
import com.pluslibrary.utils.PlusOnClickListener;

import twitter4j.User;

/**
 * Created by johnny on 15. 5. 4.
 */
public class PlusNumberPicker extends CommonDialog implements NumberPicker.OnValueChangeListener {
    public PlusNumberPicker(Activity activity, Fragment fragment) {
        super(activity, fragment, R.layout.number_picker, R.style.CustomDialog);

        init();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    private void init() {
        final NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(5);
        np.setValue(20);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);

        Button setAge = (Button) findViewById(R.id.setAge);
        Button cancel = (Button) findViewById(R.id.cancel);

        setAge.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                if(mFragment!= null) ((UserProfileFragment) mFragment).setUserAge(String.valueOf(np.getValue()));
                else ((SecondSignUpActivity) mActivity).setUserAge(String.valueOf(np.getValue()));
                PlusNumberPicker.this.dismiss();
            }

    });
        cancel.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
PlusNumberPicker.this.dismiss();
            }

        });
    }

    @Override
    protected void addListenerToButton() {


    }
}
