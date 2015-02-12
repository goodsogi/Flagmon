package com.pluslibrary.common;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pluslibrary.R;
import com.pluslibrary.utils.PlusViewHolder;

/**
 * 어댑터
 * 
 * @author jeff
 * 
 */
public class PlusListAdapter extends CommonAdapter<String> {

	public PlusListAdapter(Context context, ArrayList<String> datas) {
		super(context, R.layout.list_item_plus, datas);
		// TODO Auto-generated constructor stub

	}

	public int getCount() {
		// 수정!!
		return 20;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.list_item_plus,
					parent, false);
		}

		// 이미지
		ImageView img = PlusViewHolder.get(convertView, R.id.img);
		mImageLoader.displayImage(mDatas.get(position), img, mOption);

		// 제목
		TextView title = PlusViewHolder.get(convertView, R.id.title);
		title.setText(mDatas.get(position));

		return convertView;
	}

}
