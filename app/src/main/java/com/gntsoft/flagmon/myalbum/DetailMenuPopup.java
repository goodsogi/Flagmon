package com.gntsoft.flagmon.myalbum;

import android.app.Activity;
import android.app.Fragment;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gntsoft.flagmon.R;
import com.pluslibrary.common.CommonDialog;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusToaster;

/**
 * Created by johnny on 15. 3. 30.
 */
public class DetailMenuPopup extends CommonDialog {
    public DetailMenuPopup(Activity activity, Fragment fragment, int layoutId) {
        super(activity, fragment, layoutId, R.style.MyDialogTheme);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    @Override
    protected void addListenerToButton() {
        final RadioGroup shareTypeGroup = (RadioGroup) findViewById(R.id.shareTypeGroup);


        TextView edit = (TextView) findViewById(R.id.edit);
        TextView delete = (TextView) findViewById(R.id.delete);

        edit.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                editPost(shareTypeGroup.getCheckedRadioButtonId());

            }
        });

        delete.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                ((DetailActivity) mActivity).deletePost();
            }
        });


    }

    private void editPost(int checkedRadioButtonId) {
        if (checkedRadioButtonId == 0) {
            PlusToaster.doIt(mActivity, "공유 타입을 선택하세요");
            return;
        }
        DetailMenuPopup.this.dismiss();
        ((DetailActivity) mActivity).editPost(checkedRadioButtonId);
    }
}
