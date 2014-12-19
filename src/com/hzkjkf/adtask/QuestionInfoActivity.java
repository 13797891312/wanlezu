package com.hzkjkf.adtask;

import com.hzkjkf.util.LogUtils;
import com.hzkjkf.wanzhuan.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class QuestionInfoActivity extends Activity {
	private String url = "";
	private WebView web;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trendsinfo);
		dialog = ProgressDialog.show(this, "", "正在加载，请稍后。。。");
		dialog.setCancelable(true);
		((TextView) findViewById(R.id.title)).setText("开始答题");
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
				if (dialog != null) {
					dialog.cancel();
				}
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
