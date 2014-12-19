package com.hzkjkf.activity;

import com.hzkjkf.util.LogUtils;
import com.hzkjkf.wanzhuan.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class HomeViewPageActivity extends Activity {
	private WebView web;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_homeviewpage);
		url = getIntent().getStringExtra("url");
		LogUtils.e("recUrl", url);
		((TextView) findViewById(R.id.title)).setText("活动页面");
		web = (WebView) findViewById(R.id.webview_viewpager);
		loadWeb();
	}

	public void backClick(View v) {
		this.finish();
	}

	private void loadWeb() {
		// TODO Auto-generated method stub
		web.getSettings().setJavaScriptEnabled(true);
		//
		web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				return super.shouldOverrideUrlLoading(web, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
			}
		});
		web.loadUrl(url);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (web != null) {
			web.clearCache(true);
			web.clearDisappearingChildren();
			web.clearFormData();
			web.clearHistory();
			web.clearMatches();
			web.clearSslPreferences();
			web.clearView();
		}
	}
}
