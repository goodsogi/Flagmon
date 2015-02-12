package com.pluslibrary.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pluslibrary.PlusConstants;
import com.pluslibrary.R;

/**
 * 웹뷰
 * 
 * @author jeff
 * 
 */
public class PlusWebviewActivity extends Activity {
	private Intent mIntent;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plus_webview);
		mIntent = getIntent();
		String url = (String) mIntent.getExtras().get(PlusConstants.KEY_URL);
		WebView webView = (WebView) findViewById(R.id.webview);
		webView.setWebViewClient(new WebViewClient() {

			// @Override
			// public void onReceivedSslError(WebView view, SslErrorHandler
			// handler, SslError error) {
			// handler.proceed(); // SSL 에러가 발생해도 계속 진행!
			// }

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// if (url.contains(BookmarkConstants.TWITTER_CALLBACK_URL)) {
				// Uri uri = Uri.parse(url);
				// String oauthVerifier = uri
				// .getQueryParameter(BookmarkConstants.URL_TWITTER_OAUTH_VERIFIER);
				// mIntent.putExtra(
				// BookmarkConstants.URL_TWITTER_OAUTH_VERIFIER,
				// oauthVerifier);
				// setResult(RESULT_OK, mIntent);
				// finish();
				// return true;
				// }
				return false;
			}
		});
		webView.loadUrl(url);
	}
}
