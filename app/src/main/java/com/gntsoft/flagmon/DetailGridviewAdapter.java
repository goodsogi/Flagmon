package com.gntsoft.flagmon;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 2. 12.
 */
public class DetailGridviewAdapter extends FMCommonAdapter<Integer> {


    public DetailGridviewAdapter(Context context, ArrayList<Integer> datas) {
        super(context, R.layout.gridview_item, datas);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.gridview_item,
                    parent, false);
        }
        ImageView img = PlusViewHolder.get(convertView, R.id.gridview_img);
        img.setImageResource(mDatas.get(position));

        return convertView;
    }

}