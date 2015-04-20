package com.gntsoft.flagmon.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.R;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;

/**
 * Created by johnny on 15. 2. 13.
 */
public class SettingFragment extends FMCommonFragment {


    public SettingFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting,
                container, false);
        return rootView;
    }

    @Override
    protected void addListenerToButton() {
        LinearLayout manageFriend = (LinearLayout) mActivity.findViewById(R.id.manageFriend);
        manageFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mananageFriend(v);
            }
        });

        LinearLayout manageMon = (LinearLayout) mActivity.findViewById(R.id.manageMon);
        manageMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageMon(v);
            }
        });

        LinearLayout showUserInfo = (LinearLayout) mActivity.findViewById(R.id.showUserInfo);
        showUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserInfo(v);
            }
        });
        LinearLayout showNoti = (LinearLayout) mActivity.findViewById(R.id.showNoti);
        showNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoti(v);
            }
        });
        LinearLayout setAlarm = (LinearLayout) mActivity.findViewById(R.id.setAlarm);
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm(v);
            }
        });


    }

    private void mananageFriend(View v) {

        PlusClickGuard.doIt(v);

        Intent intent = new Intent(mActivity, FindFriendActivity.class);
        mActivity.startActivity(intent);

    }

    private void manageMon(View v) {
//구현!!
        PlusToaster.doIt(mActivity, "준비중...");


    }

    private void showUserInfo(View v) {
//구현!!
        PlusToaster.doIt(mActivity, "준비중...");
    }

    private void showNoti(View v) {
//구현!!
        PlusToaster.doIt(mActivity, "준비중...");
    }

    private void setAlarm(View v) {
//구현!!
        PlusToaster.doIt(mActivity, "준비중...");
    }


}
