package com.gntsoft.flagmon.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.setting.FindFriendActivity;
import com.gntsoft.flagmon.setting.SettingFragment;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusOnClickListener;

/**
 * Created by johnny on 15. 3. 3.
 */
public class InviteFriendFragment extends FMCommonFragment {


    public InviteFriendFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showInviteFriendTopBar();
    }
    private void showInviteFriendTopBar() {
        FrameLayout topBarContainer = (FrameLayout) mActivity.findViewById(R.id.container_top_bar);
        topBarContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View inviteTopBar = inflater.inflate(R.layout.top_bar_invite_friend,null);
        topBarContainer.addView(inviteTopBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invite_friend,
                container, false);
        return rootView;
    }

    @Override
    protected void addListenerButton() {
        Button findFriend = (Button) mActivity.findViewById(R.id.findFriend);
        findFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFindFriend(v);
            }
        });

    }

    private void goToFindFriend(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(mActivity, FindFriendActivity.class);
        mActivity.startActivity(intent);

    }

}
