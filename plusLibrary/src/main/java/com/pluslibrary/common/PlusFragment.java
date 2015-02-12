package com.pluslibrary.common;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pluslibrary.R;
import com.pluslibrary.server.ApiConstants;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusOnGetDataListener;

public class PlusFragment extends CommonFragment implements
		PlusOnGetDataListener {

	private static final int GET_BLACKTIME_MAIN = 2;

	public PlusFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// 블랙타임 추천 브랜드 리스트
		new PlusHttpClient(mActivity, this, true).execute(GET_BLACKTIME_MAIN,
				ApiConstants.GET_BLACKTIME_MAIN, new PlusParser());

	}

	@SuppressWarnings("unchecked")
	private void makeList(Object datas) {
		ListView list = (ListView) mActivity
				.findViewById(R.id.list);
		list.setAdapter(new PlusListAdapter(mActivity,
				(ArrayList<String>) datas));
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		return rootView;
	}

	@Override
	protected void addListenerButton() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Integer from, Object datas) {
		switch (from) {
		case GET_BLACKTIME_MAIN:
			makeList(datas);
			break;

		default:
			break;
		}

	}

	

}
