package com.gntsoft.flagmon.friend;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.setting.FriendModel;
import com.gntsoft.flagmon.user.UserPageActivity;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusToaster;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 3.
 */
public class ChooseFriendListAdapter extends FMCommonAdapter<FriendModel> {


    public ChooseFriendListAdapter(Context context, ArrayList<FriendModel> datas) {
        super(context, R.layout.choose_friend_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.choose_friend_list_item,
                    parent, false);
        }

        FriendModel data = mDatas.get(position);
        TextView name = PlusViewHolder.get(convertView, R.id.name);


        ImageView img = PlusViewHolder.get(convertView, R.id.img);
        mImageLoader.displayImage(data.getProfileImageUrl(), img,
                mOption);
        img.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                goToUserPage();
            }
        });
        name.setText(data.getName());

        Button delete = PlusViewHolder.get(convertView, R.id.delete);
        delete.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                deleteFriend();
            }
        });

        Button select = PlusViewHolder.get(convertView, R.id.select);
        select.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                selectFriend();
            }
        });


//        mImageLoader.displayImage(
//                BBGGConstants.IMG_URL_HEAD + mDatas.get(position).getImg01(), main_img,
//                mOption);





        return convertView;
    }

    private void goToUserPage() {
        //!!사용자 고유번호 전달
        Intent intent = new Intent(mContext, UserPageActivity.class);
        mContext.startActivity(intent);

    }

    private void deleteFriend() {
        //구현!!
        PlusToaster.doIt(mContext, "준비중...");
    }

    private void selectFriend() {
//구현!!
        PlusToaster.doIt(mContext, "준비중...");
    }

}
