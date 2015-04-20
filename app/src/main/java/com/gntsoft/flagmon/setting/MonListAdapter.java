package com.gntsoft.flagmon.setting;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.MonInfoModel;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 4. 20.
 */
public class MonListAdapter extends FMCommonAdapter<MonInfoModel> {


    public MonListAdapter(Context context, ArrayList<MonInfoModel> datas) {
        super(context, R.layout.mon_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.mon_list_item,
                    parent, false);
        }

        final MonInfoModel data = mDatas.get(position);
        TextView valueDate = PlusViewHolder.get(convertView, R.id.valueDate);
        TextView reason = PlusViewHolder.get(convertView, R.id.reason);
        TextView point = PlusViewHolder.get(convertView, R.id.point);
        TextView expireDate = PlusViewHolder.get(convertView, R.id.expireDate);

        valueDate.setText(data.getValueDate());
        reason.setText(data.getReason());
        point.setText(data.getPoint());
        expireDate.setText(data.getExpireDate());


        return convertView;
    }

}
