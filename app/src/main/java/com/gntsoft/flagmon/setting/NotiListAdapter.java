package com.gntsoft.flagmon.setting;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.server.NotiModel;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 4. 21.
 */
public class NotiListAdapter extends FMCommonAdapter<NotiModel> {


    public NotiListAdapter(Context context, ArrayList<NotiModel> datas) {
        super(context, R.layout.noti_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.noti_list_item,
                    parent, false);
        }

        NotiModel data = mDatas.get(position);
        TextView title = PlusViewHolder.get(convertView, R.id.title);



        title.setText(data.getTitle());



        return convertView;
    }
}
