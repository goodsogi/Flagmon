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
import com.gntsoft.flagmon.server.FMModel;
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
 * Created by johnny on 15. 3. 3.
 */
public class GotFriendRequestListAdapter extends FMCommonAdapter<FriendModel> implements
        PlusOnGetDataListener {

    private static final int ACCEPT_REQUEST = 0;
    private static final int IGNORE_REQUEST = 12;

    public GotFriendRequestListAdapter(Context context, ArrayList<FriendModel> datas) {
        super(context, R.layout.got_friend_request_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.got_friend_request_list_item,
                    parent, false);
        }

        FriendModel data = mDatas.get(position);
        TextView name = PlusViewHolder.get(convertView, R.id.name);


        ImageView img = PlusViewHolder.get(convertView, R.id.img);
        mImageLoader.displayImage(data.getProfileImageUrl(), img,
                mOption);

        name.setText(data.getName());

        Button ignore = PlusViewHolder.get(convertView, R.id.ignore);
        ignore.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                ignoreRequest();
            }
        });

        Button accept = PlusViewHolder.get(convertView, R.id.accept);
        accept.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                acceptRequest();
            }
        });


        return convertView;
    }

    private void acceptRequest() {
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_FRIEND));
        if(LoginChecker.isLogIn((android.app.Activity) mContext)) { postParams.add(new BasicNameValuePair("key", ((FMCommonActivity) mContext).getUserAuthKey()));}


        new PlusHttpClient((android.app.Activity) mContext, this, false).execute(ACCEPT_REQUEST,
                FMApiConstants.ACCEPT_REQUEST, new PlusInputStreamStringConverter(),
                postParams);
    }

    private void ignoreRequest() {
//수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_FRIEND));
        if(LoginChecker.isLogIn((android.app.Activity) mContext)) { postParams.add(new BasicNameValuePair("key", ((FMCommonActivity) mContext).getUserAuthKey()));}


        new PlusHttpClient((android.app.Activity) mContext, this, false).execute(IGNORE_REQUEST,
                FMApiConstants.IGNORE_REQUEST, new PlusInputStreamStringConverter(),
                postParams);
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case ACCEPT_REQUEST:
                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(mContext,model.getResult().equals("success")?"친구 신청을 수락했습니다":"친구 신청을 수락하지 못했습니다");
                if(model.getResult().equals("success")) {
                    //추가 액션??
                }
                break;
            case IGNORE_REQUEST:
                ServerResultModel model2 = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(mContext,model2.getResult().equals("success")?"친구 신청을 무시했습니다":"친구 신청을 무시하지 못했습니다");
                if(model2.getResult().equals("success")) {
                    //추가 액션??
                }
                break;
        }

    }

}
