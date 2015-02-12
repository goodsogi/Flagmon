package com.pluslibrary.server;

import java.io.InputStream;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;

import com.pluslibrary.utils.PlusLogger;

import android.app.Activity;

/**
 * 서버 통신
 * 
 * @author jeff
 * 
 */
public class PlusHttpClient extends PlusHttpClientBase<Object, Void, Object> {

	private PlusOnGetDataListener mListener;
	private Integer mFrom;

	public PlusHttpClient(Activity activity, PlusOnGetDataListener listener,
			boolean showLoading) {
		super(activity, showLoading);
		// TODO Auto-generated constructor stub
		mListener = listener;
	}

	@Override
	protected Object doInBackground(Object... params) {
		mFrom = (Integer) params[0];
		String url = (String) params[1];
		PlusXmlParser parser = (PlusXmlParser) params[2];
		InputStream in = null;
		if (params.length == 3) {
			in = doGet(url);
		} else if (params.length == 4 && params[3] instanceof MultipartEntity) {
			in = doPostMultipartEntirty(url, (MultipartEntity) params[3]);
		} else {
			in = doPost(url, (List<NameValuePair>) params[3]);
		}

		if (in == null)
			return null;

		return parser.doIt(in);
	}

	@Override
	protected void onPostExecute(Object datas) {

		super.onPostExecute(datas);
//		if (datas == null)
//			return;
		mListener.onSuccess(mFrom, datas);

	}

}
