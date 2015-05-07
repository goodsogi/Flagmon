package com.gntsoft.flagmon.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;

/**
 * Created by johnny on 15. 3. 2.
 */
public class LoginFragment extends FMCommonFragment {


    public LoginFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showInviteTopBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login,
                container, false);
        int tabName = this.getArguments().getInt(FMConstants.KEY_TARGET);
        setBackgound(rootView, tabName);
        return rootView;
    }

    @Override
    protected void addListenerToButton() {
        // TODO Auto-generated method stub

    }

    private void showInviteTopBar() {
        FrameLayout topBarContainer = (FrameLayout) mActivity.findViewById(R.id.container_top_bar);
        topBarContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View inviteTopBar = inflater.inflate(R.layout.top_bar_invite, null);
        topBarContainer.addView(inviteTopBar);
    }

    private void setBackgound(View rootView, int tabName) {
        FrameLayout frameLayout = (FrameLayout) rootView.findViewById(R.id.background_login_fragment);
        frameLayout.setBackgroundResource(getBackgroundResource(tabName));
    }

    private int getBackgroundResource(int tabName) {

        switch (tabName) {
            case FMConstants.TAB_FRIEND:
                return R.drawable.p09_bg;

            case FMConstants.TAB_MYALBUM:
                return R.drawable.p10_bg;

            case FMConstants.TAB_SETTING:
                return R.drawable.p11_bg;


        }
        return R.drawable.p09_bg;
    }


}
