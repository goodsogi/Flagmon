package com.gntsoft.flagmon.setting;

import android.widget.Button;
import android.widget.ListAdapter;
import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusToaster;
import com.pluslibrary.utils.PlusViewHolder;

import com.gntsoft.flagmon.server.FriendModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 4. 28.
 */
public class ContactListAdapter2 extends FMCommonAdapter<FriendModel> implements
        PlusOnGetDataListener {

    private static final int SEND_FRIEND_REQUEST = 0;

    public ContactListAdapter2(Context context, ArrayList<FriendModel> datas) {
        super(context, R.layout.request_friend_list_item, datas);
        // TODO Auto-generated constructor stub

    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.request_friend_list_item,
                    parent, false);
        }

        ImageView img = PlusViewHolder.get(convertView, R.id.img);
        mImageLoader.displayImage(mDatas.get(position).getProfileImageUrl(), img, mOption);

        TextView title = PlusViewHolder.get(convertView, R.id.title);
        title.setText(mDatas.get(position).getName());

        Button sendFriendRequest = PlusViewHolder.get(convertView, R.id.sendFriendRequest);
        sendFriendRequest.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                sendFriendRequest(mDatas.get(position).getUserEmail());
            }
        });

        return convertView;
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

    private void sendFriendRequest(String userEmail) {
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("friend_email", userEmail));
        if (LoginChecker.isLogIn((android.app.Activity) mContext)) {
            postParams.add(new BasicNameValuePair("key", ((FMCommonActivity) mContext).getUserAuthKey()));
        }


        new PlusHttpClient((android.app.Activity) mContext, this, false).execute(SEND_FRIEND_REQUEST,
                FMApiConstants.SEND_FRIEND_REQUEST, new PlusInputStreamStringConverter(),
                postParams);
    }


}