package com.gntsoft.flagmon.setting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.main.MainActivity;
import com.pluslibrary.utils.PlusOnClickListener;

/**
 * Created by johnny on 15. 4. 21.
 */
public class UserProfileFragment extends FMCommonFragment implements OnKeyBackPressedListener {
    public UserProfileFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //회원정보를 조회하여 화면에 표시!!

        //setLogoutHelpText();
    }

//    private void setLogoutHelpText() {
//        TextView logoutHelp =(TextView) mActivity.findViewById(R.id.logoutHelp);
//        String text = getUserEmail() + "계정을 로그아웃합니다.";
//        logoutHelp.setText(text);
//    }

//    private String getUserEmail() {
//        SharedPreferences sharedPreference = mActivity.getSharedPreferences(
//                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
//        return sharedPreference.getString(FMConstants.KEY_USER_EMAIL, "");
//
//
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate( R.layout.fragment_user_profile, container, false);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).setOnKeyBackPressedListener(this);
    } // in SearchFrag

    @Override
    public void onBack() {
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new SettingFragment())
                .commit();
    }

    @Override
    public void onDestroyView() {

        ((MainActivity) mActivity).setOnKeyBackPressedListener(null);
        super.onDestroyView();
    }

    @Override
    protected void addListenerToButton() {

        TextView logout =(TextView) mActivity.findViewById(R.id.logout);
        logout.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                logout();
            }
        });

        TextView logoutHelp =(TextView) mActivity.findViewById(R.id.logoutHelp);
        logoutHelp.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                logout();
            }
        });

    }

    private void logout() {
        SharedPreferences sharedPreference = mActivity.getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreference.edit();
        e.putBoolean(FMConstants.KEY_IS_LOGIN, false);
        e.commit();
    }
}
