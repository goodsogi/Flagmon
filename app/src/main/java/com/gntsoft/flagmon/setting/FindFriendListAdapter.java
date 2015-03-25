package com.gntsoft.flagmon.setting;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FriendModel;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.gntsoft.flagmon.utils.LoginChecker;
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
public class FindFriendListAdapter extends FMCommonAdapter<FriendModel> implements
        PlusOnGetDataListener {
    private static final int SEND_FRIEND_REQUEST = 0;

    public FindFriendListAdapter(Context context, ArrayList<FriendModel> datas) {
        super(context, R.layout.find_friend_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.find_friend_list_item,
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
                sendFriendRequest();
            }
        });


        return convertView;
    }

    private void sendFriendRequest() {
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_FRIEND));
        if (LoginChecker.isLogIn((android.app.Activity) mContext)) {
            postParams.add(new BasicNameValuePair("key", ((FMCommonActivity) mContext).getUserAuthKey()));
        }


        new PlusHttpClient((android.app.Activity) mContext, this, false).execute(SEND_FRIEND_REQUEST,
                FMApiConstants.SEND_FRIEND_REQUEST, new PlusInputStreamStringConverter(),
                postParams);
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case SEND_FRIEND_REQUEST:
                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(mContext, model.getResult().equals("success") ? "친구 신청을 보냈습니다" : "친구 신청을 보내지 못했습니다");
                if (model.getResult().equals("success")) {
                    //추가 액션??
                }
                break;
        }

    }
}