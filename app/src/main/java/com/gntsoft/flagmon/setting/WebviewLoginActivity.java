package com.gntsoft.flagmon.setting;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;


public class WebviewLoginActivity extends Activity {
	private Intent mIntent;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview_login);
		mIntent = getIntent();
		String url = (String) mIntent.getExtras().get(FMConstants.KEY_URL);
		WebView webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.contains(FMConstants.TWITTER_CALLBACK_URL)) {
					Uri uri = Uri.parse(url);
					String oauthVerifier = uri
							.getQueryParameter(FMConstants.URL_TWITTER_OAUTH_VERIFIER);
					mIntent.putExtra(
                            FMConstants.URL_TWITTER_OAUTH_VERIFIER,
							oauthVerifier);
					setResult(RESULT_OK, mIntent);
					finish();
					return true;
				}



				return false;
			}
		});
		webView.loadUrl(url);
	}
}
