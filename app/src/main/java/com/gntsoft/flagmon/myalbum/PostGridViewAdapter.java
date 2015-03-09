package com.gntsoft.flagmon.myalbum;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.R;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 2. 12.
 */
public class PostGridViewAdapter extends FMCommonAdapter<GalleryPhotoModel> {


    public PostGridViewAdapter(Context context, ArrayList<GalleryPhotoModel> datas) {
        super(context, R.layout.post_gridview_item, datas);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.post_gridview_item,
                    parent, false);
        }
        ImageView img = PlusViewHolder.get(convertView, R.id.gridview_img);
        img.setImageBitmap(mDatas.get(position).getThumnail());



        return convertView;
    }

}