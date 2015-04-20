package com.gntsoft.flagmon.neighbor;

import android.app.Activity;
import android.app.Fragment;
import android.widget.Button;

import com.gntsoft.flagmon.R;
import com.pluslibrary.common.CommonDialog;
import com.pluslibrary.utils.PlusOnClickListener;

/**
 * Created by johnny on 15. 3. 30.
 */
public class FindTreasurePopup extends CommonDialog {
    public FindTreasurePopup(Activity activity, Fragment fragment, int layoutId) {
        super(activity, fragment, layoutId, R.style.CustomDialog);

    }

    @Override
    protected void addListenerToButton() {
        Button search = (Button) findViewById(R.id.search);
        Button cancel = (Button) findViewById(R.id.cancel);

        search.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                FindTreasurePopup.this.dismiss();
                ((MapFragment) mFragment).getTreausureDataFromServer();
            }
        });

        cancel.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                FindTreasurePopup.this.dismiss();
            }
        });


    }
}
