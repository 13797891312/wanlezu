package com.hzkjkf.more;

import com.hzkjkf.update.UpDateInfo;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class TrendsInfo extends Activity {
	private WebView web;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_trendsinfo);
		((TextView) findViewById(R.id.title)).setText("动态详情");
		url = getIntent().getStringExtra("url");
		LogUtils.e("recUrl", url);
		web = (WebView) findViewById(R.id.webview_viewpager);
		loadWeb();
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

	public void backClick(View v) {
		this.finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
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
