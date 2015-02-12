package com.pluslibrary.common;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.pluslibrary.R;
import com.pluslibrary.server.ApiConstants;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusOnGetDataListener;

public class PlusActivity extends Activity implements PlusOnGetDataListener {

	private static final int GET_BLACKTIME_MAIN = 2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_plus);

		// 블랙타임 추천 브랜드 리스트
		new PlusHttpClient(this, this, true).execute(GET_BLACKTIME_MAIN,
				ApiConstants.GET_BLACKTIME_MAIN, new PlusParser());
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

	@SuppressWarnings("unchecked")
	private void makeList(Object datas) {
		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(new PlusListAdapter(this,
				(ArrayList<String>) datas));

	}

}
