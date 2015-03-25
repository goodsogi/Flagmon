package com.gntsoft.flagmon.setting;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FriendModel;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusToaster;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 12.
 */
public class RequestFriendListAdapter extends FMCommonAdapter<FriendModel> {


    public RequestFriendListAdapter(Context context, ArrayList<FriendModel> datas) {
        super(context, R.layout.request_friend_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.request_friend_list_item,
                    parent, false);
        }

        FriendModel data = mDatas.get(position);
        TextView name = PlusViewHolder.get(convertView, R.id.name);


        ImageView img = PlusViewHolder.get(convertView, R.id.img);
        mImageLoader.displayImage(data.getProfileImageUrl(), img,
                mOption);

        name.setText(data.getName());

        Button sendFriendRequest = PlusViewHolder.get(convertView, R.id.sendFriendRequest);
        sendFriendRequest.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                sendRequest();
            }
        });


        return convertView;
    }

    private void sendRequest() {
        //구현!!
        PlusToaster.doIt(mContext, "준비중...");
    }
}
