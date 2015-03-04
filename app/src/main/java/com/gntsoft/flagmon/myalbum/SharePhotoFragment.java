package com.gntsoft.flagmon.myalbum;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.setting.FindFriendActivity;
import com.pluslibrary.utils.PlusClickGuard;

/**
 * Created by johnny on 15. 3. 3.
 */
public class SharePhotoFragment extends FMCommonFragment {


    public SharePhotoFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showSharePhotoTopBar();

    }

    private void showSharePhotoTopBar() {
        FrameLayout topBarContainer = (FrameLayout) mActivity.findViewById(R.id.container_top_bar);
        topBarContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View inviteTopBar = inflater.inflate(R.layout.top_bar_share_photo,null);
        topBarContainer.addView(inviteTopBar);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_share_photo,
                container, false);
        return rootView;
    }

    @Override
    protected void addListenerButton() {
        Button post = (Button) mActivity.findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPost(v);
            }
        });

    }

    private void goToPost(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(mActivity, PostActivity.class);
        mActivity.startActivity(intent);

    }

}

