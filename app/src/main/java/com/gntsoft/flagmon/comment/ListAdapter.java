package com.gntsoft.flagmon.comment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMModel;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 2. 13.
 */
public class ListAdapter extends FMCommonAdapter<FMModel> {


    public ListAdapter(Context context, ArrayList<FMModel> datas) {
        super(context, R.layout.reply_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.reply_list_item,
                    parent, false);
        }

        FMModel data = mDatas.get(position);
        TextView name = PlusViewHolder.get(convertView, R.id.name);
        TextView content = PlusViewHolder.get(convertView, R.id.content);
        TextView time = PlusViewHolder.get(convertView, R.id.time);

        ImageView img = PlusViewHolder.get(convertView, R.id.img);
        mImageLoader.displayImage(
                data.getImgUrl(), img,
                mOption);

        name.setText(data.getUserName());
        content.setText(data.getMemo());
        time.setText(data.getRegisterDate());


        return convertView;
    }

}
