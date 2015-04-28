package com.gntsoft.flagmon.myalbum;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.PlaceModel;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 17.
 */
public class SearchLocationListAdapter extends FMCommonAdapter<PlaceModel> {


    public SearchLocationListAdapter(Context context, ArrayList<PlaceModel> datas) {
        super(context, R.layout.search_location_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.search_location_list_item,
                    parent, false);
        }

        PlaceModel data = mDatas.get(position);
        TextView locationName = PlusViewHolder.get(convertView, R.id.locationName);

        locationName.setText(data.getName() + "       " + data.getAddress());


        return convertView;
    }

}
