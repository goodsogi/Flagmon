package com.gntsoft.flagmon.setting;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FriendModel;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusToaster;
import com.pluslibrary.utils.PlusViewHolder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 3. 13.
 */
public class SearchFriendListAdapter extends FMCommonAdapter<FriendModel> implements
        PlusOnGetDataListener {
    private static final int ADD_FRIEND = 0;

    public SearchFriendListAdapter(Context context, ArrayList<FriendModel> datas) {
        super(context, R.layout.search_friend_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.search_friend_list_item,
                    parent, false);
        }

        final FriendModel data = mDatas.get(position);
        TextView name = PlusViewHolder.get(convertView, R.id.name);


        ImageView img = PlusViewHolder.get(convertView, R.id.img);
        mImageLoader.displayImage(data.getProfileImageUrl(), img,
                mOption);

        name.setText(data.getName());

        Button addFriend = PlusViewHolder.get(convertView, R.id.addFriend);
        addFriend.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                addFriend(data.getUserEmail());
            }
        });


        return convertView;
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case ADD_FRIEND:
                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(mContext, model.getResult().equals("success") ? "친구로 추가했습니다" : "친구로 추가하지 못했습니다");
                if (model.getResult().equals("success")) {
                    //추가 액션??
                }
                break;
        }

    }

    private void addFriend(String userEmail) {
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("friend_email", userEmail));
        postParams.add(new BasicNameValuePair("key", ((FMCommonActivity) mContext).getUserAuthKey()));


        new PlusHttpClient((android.app.Activity) mContext, this, false).execute(ADD_FRIEND,
                FMApiConstants.ADD_FRIEND, new PlusInputStreamStringConverter(),
                postParams);
    }
}