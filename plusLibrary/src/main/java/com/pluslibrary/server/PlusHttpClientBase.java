package com.pluslibrary.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.mime.MultipartEntity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.pluslibrary.PlusConstants;
import com.pluslibrary.R;
import com.pluslibrary.utils.PlusLogger;

public class PlusHttpClientBase<T, U, V> extends AsyncTask<T, U, V> {

	private Activity mActivity;
	private boolean mShowLoading;

	public PlusHttpClientBase(Activity activity, boolean showLoading) {
		// TODO Auto-generated constructor stub
		mActivity = activity;
		mShowLoading = showLoading;
	}

	private ProgressDialog mProgressDialog;

	@Override
	protected void onPreExecute() {
		if (mShowLoading) {
			// 로딩바 표시
			mProgressDialog = new ProgressDialog(mActivity,R.style.ServerProgessDialogTheme);
			mProgressDialog.setIndeterminate(true);

			mProgressDialog.setMessage(mActivity
					.getString(R.string.loading_message));
			mProgressDialog.show();
		}
	}

	@Override
	protected void onPostExecute(V datas) {
		super.onPostExecute(datas);

		// 로딩바 해제
		if (mShowLoading)
			mProgressDialog.dismiss();

	}

	@Override
	protected V doInBackground(T... params) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get 방식 서버 통신
	 * 
	 * @param urlString
	 * @param filter
	 * @return
	 */
	protected InputStream doGet(String urlString) {
		try {

			URL url = new URL(urlString);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);

			conn.setRequestMethod("GET");

			return conn.getInputStream();

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		}

		return null;
	}

	/**
	 * Post 방식 서버 통신
	 * 
	 * @param urlString
	 * @param postParams
	 * @return
	 */
	protected InputStream doPost(String urlString,
			List<NameValuePair> postParams) {

		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		conn.setReadTimeout(10000);
		conn.setConnectTimeout(15000);
		try {
			conn.setRequestMethod("POST");
		} catch (ProtocolException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		conn.setDoInput(true);
		conn.setDoOutput(true);

		OutputStream os = null;
		try {
			os = conn.getOutputStream();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(os,
					PlusConstants.SERVER_ENCODING_TYPE));

		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {

			writer.write(getQuery(postParams));

			writer.flush();
			writer.close();
			os.close();
			conn.connect();

			return conn.getInputStream();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * multipartentity를 사용한 파일과 문자열 전송
	 * 
	 * @param urlString
	 * @param entity
	 * @return
	 */
	protected InputStream doPostMultipartEntirty(String urlString,
			MultipartEntity entity) {

		// Set up request
		HttpURLConnection httpUrlConnection = null;
		URL url = null;
		try {
			url = new URL(urlString);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			httpUrlConnection = (HttpURLConnection) url.openConnection();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			httpUrlConnection.setReadTimeout(10000);
			httpUrlConnection.setConnectTimeout(15000);
			httpUrlConnection.setRequestMethod("POST");
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setDoOutput(true);

			httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
			httpUrlConnection.addRequestProperty("Content-length",
					entity.getContentLength() + "");
			httpUrlConnection.addRequestProperty(entity.getContentType()
					.getName(), entity.getContentType().getValue());

			OutputStream os = httpUrlConnection.getOutputStream();
			entity.writeTo(httpUrlConnection.getOutputStream());
			os.close();
			httpUrlConnection.connect();

			if (httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

				return httpUrlConnection.getInputStream();
			}

		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		return null;
	}

	private String getQuery(List<NameValuePair> params)
			throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(),
					PlusConstants.SERVER_ENCODING_TYPE));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(),
					PlusConstants.SERVER_ENCODING_TYPE));
		}

		return result.toString();
	}

}
