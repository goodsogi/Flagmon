package com.pluslibrary.common;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;

/**
 * 공통 팝업
 *
 * @author user
 */
public abstract class CommonDialog extends Dialog {

    protected Activity mActivity;
    protected Fragment mFragment;

    public CommonDialog(final Activity activity, final Fragment fragment,
                        int layoutId, int theme) {
        super(activity, theme);
        mActivity = activity;
        mFragment = fragment;
        setOwnerActivity(activity);
        setCancelable(true);
        setContentView(layoutId);
        // 버튼에 리스너 추가
        addListenerToButton();

    }

    /**
     * 팝업창 취소 여부 설정
     *
     * @param flag
     * @return
     */
    public CommonDialog cancelable(boolean flag) {
        setCancelable(flag);
        return this;
    }

    /**
     * 버튼에 리스너 추가
     */
    abstract protected void addListenerToButton();

}
