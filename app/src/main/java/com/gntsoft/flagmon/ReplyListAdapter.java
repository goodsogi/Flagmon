package com.gntsoft.flagmon;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 2. 13.
 */
public class ReplyListAdapter extends FMCommonAdapter<ReplyListModel> {


    public ReplyListAdapter(Context context, ArrayList<ReplyListModel> datas) {
        super(context, R.layout.reply_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.reply_list_item,
                    parent, false);
        }

        ReplyListModel data = mDatas.get(position);
        TextView name = PlusViewHolder.get(convertView, R.id.name);
        TextView content = PlusViewHolder.get(convertView, R.id.content);
        TextView time = PlusViewHolder.get(convertView, R.id.time);

        ImageView img = PlusViewHolder.get(convertView, R.id.img);
        img.setImageResource(data.getImg());

        name.setText(data.getName());
        content.setText(data.getContent());
        time.setText(data.getTime());

//        mImageLoader.displayImage(
//                BBGGConstants.IMG_URL_HEAD + mDatas.get(position).getImg01(), main_img,
//                mOption);


        return convertView;
    }

}
