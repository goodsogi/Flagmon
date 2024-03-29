package com.gntsoft.flagmon.neighbor;

import android.app.Activity;
import android.app.Fragment;
import android.widget.Button;
import android.widget.TextView;

import com.gntsoft.flagmon.R;
import com.pluslibrary.common.CommonDialog;
import com.pluslibrary.utils.PlusOnClickListener;

/**
 * Created by johnny on 15. 3. 30.
 */
public class FindTreasureResultPopup extends CommonDialog {
    public FindTreasureResultPopup(Activity activity, Fragment fragment, int layoutId) {
        super(activity, fragment, layoutId, R.style.CustomDialog);

    }

    public void setMessage(String message) {
        TextView messageView = (TextView) findViewById(R.id.messageView);
        messageView.setText(message);
    }

    @Override
    protected void addListenerToButton() {
        Button confirm = (Button) findViewById(R.id.confirm);


        confirm.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                FindTreasureResultPopup.this.dismiss();
            }
        });


    }
}