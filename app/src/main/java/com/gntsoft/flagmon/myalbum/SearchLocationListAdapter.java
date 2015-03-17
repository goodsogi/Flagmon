package com.gntsoft.flagmon.myalbum;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMModel;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 17.
 */
public class SearchLocationListAdapter extends FMCommonAdapter<FMModel> {


    public SearchLocationListAdapter(Context context, ArrayList<FMModel> datas) {
        super(context, R.layout.search_location_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.search_location_list_item,
                    parent, false);
        }

        FMModel data = mDatas.get(position);
        TextView locationName = PlusViewHolder.get(convertView, R.id.locationName);

        //수정!!
        locationName.setText(data.getUserName());




        return convertView;
    }

}
