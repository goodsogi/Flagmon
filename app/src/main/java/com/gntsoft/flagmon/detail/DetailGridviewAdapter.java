package com.gntsoft.flagmon.detail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMModel;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 2. 12.
 */
public class DetailGridviewAdapter extends FMCommonAdapter<FMModel> {


    public DetailGridviewAdapter(Context context, ArrayList<FMModel> datas) {
        super(context, R.layout.gridview_item, datas);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.gridview_item,
                    parent, false);
        }

        FMModel data = mDatas.get(position);

        ImageView img = PlusViewHolder.get(convertView, R.id.gridview_img);
        mImageLoader.displayImage(
                data.getImgUrl(), img,
                mOption);

        return convertView;
    }

}