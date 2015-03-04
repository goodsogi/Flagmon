package com.gntsoft.flagmon.setting;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.R;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusToaster;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 3.
 */
public class GotFriendRequestListAdapter extends FMCommonAdapter<FindFriendModel> {


public GotFriendRequestListAdapter(Context context, ArrayList<FindFriendModel> datas) {
        super(context, R.layout.got_friend_request_list_item, datas);

        }


public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
        convertView = mLayoutInflater.inflate(R.layout.got_friend_request_list_item,
        parent, false);
        }

    FindFriendModel data = mDatas.get(position);
        TextView name = PlusViewHolder.get(convertView, R.id.name);


        ImageView img = PlusViewHolder.get(convertView, R.id.img);
        img.setImageResource(data.getImg());

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


//        mImageLoader.displayImage(
//                BBGGConstants.IMG_URL_HEAD + mDatas.get(position).getImg01(), main_img,
//                mOption);





        return convertView;
        }

    private void acceptRequest() {
        //구현!!
        PlusToaster.doIt(mContext, "준비중...");
    }

    private void ignoreRequest() {
//구현!!
        PlusToaster.doIt(mContext, "준비중...");
    }

}
