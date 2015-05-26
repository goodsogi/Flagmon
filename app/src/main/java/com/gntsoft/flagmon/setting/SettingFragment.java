package com.gntsoft.flagmon.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.neighbor.ListFragment;
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

        if(hasPushTaget())
            moveToPushTarget();
    }

    private void moveToPushTarget() {

        String target = getArguments().getString(FMConstants.KEY_TARGET);

        if(target.equals(FMConstants.VIEW_ALARM)) viewAlarmFromPush();
        else if(target.equals(FMConstants.VIEW_NOTI)) viewNotiFromPush();
        else if(target.equals(FMConstants.FRIEND_SETTING)) viewFriendSettingFromPush();
        else if(target.equals(FMConstants.USER_PROFILE)) viewUserProfileFromPush();

    }

    private void viewUserProfileFromPush() {
        LinearLayout showUserInfo = (LinearLayout) mActivity.findViewById(R.id.showUserInfo);
        showUserInfo(showUserInfo);
    }

    private void viewFriendSettingFromPush() {
        LinearLayout manageFriend = (LinearLayout) mActivity.findViewById(R.id.manageFriend);
        mananageFriend(manageFriend);
    }

    private void viewNotiFromPush() {
        LinearLayout showNoti = (LinearLayout) mActivity.findViewById(R.id.showNoti);
        showNoti(showNoti);
    }

    private void viewAlarmFromPush() {
        LinearLayout setAlarm = (LinearLayout) mActivity.findViewById(R.id.setAlarm);
        setAlarm(setAlarm);
    }

    private boolean hasPushTaget() {
       return getArguments().getString(FMConstants.KEY_TARGET) != null && !getArguments().getString(FMConstants.KEY_TARGET).equals("");

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


        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new ManageMonFragment())
                .commit();
    }

    private void showUserInfo(View v) {

        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new UserProfileFragment())
                .commit();
    }

    private void showNoti(View v) {
        Intent intent = new Intent(mActivity, NotiActivity.class);
        startActivity(intent);

    }

    private void setAlarm(View v) {
        Intent intent = new Intent(mActivity, AlarmSettingActivity.class);
        startActivity(intent);
    }


}
