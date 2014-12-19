package com.hzkjkf.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;

public class UpDateInfo {
	private Context context;
	private Handler hd;
	private String adress;
	private String versionName = "";
	private String text = "";
	private ProgressDialog pBar;
	private View view;
	private Dialog dialog;
	private boolean isDowning;
	private boolean isForce;
	private int size;
	private String newVersion;
	private String newTime;

	public UpDateInfo(Context context, Handler hd) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.hd = hd;
	}

	public void UpDate() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId", "appOs",
						"currentver" }, new String[] { "10027", "Android",
						getCurrentVersion() });
				// TODO Auto-generated method stub
				String json = HttpTool.httpGetJson1(context, url, hd);
				// String json = HttpTool.readJson(context, R.raw.json_version);
				if (!json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					if (HttpTool.getErrorCode(json) == 10031) {
						HandleUtil.sendInt(hd, 1);
						return;
					}
					JSONArray result = HttpTool.getResult(json);
					for (int i = 0; i < result.length(); i++) {
						try {
							JSONObject object = result.getJSONObject(i);
							newVersion = object.getString("versionNum");
							newTime = object.getString("versionTime");
							adress = object.getString("fileLsh");
							text = object.getString("versionDes");
							isForce = (object.getString("versionType").equals(
									"normal") ? false : true);
							HandleUtil.post(hd, new UpDateRunnble());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					if (hd != null) {
						hd.sendEmptyMessage(-1);
					}
				}
			}
		}).start();
	}

	public String getCurrentVersion() {
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		MyApp.getInstence().version = versionName;
		return versionName;
	}

	private void createView() {
		// TODO Auto-generated method stub
		view = View.inflate(context, R.layout.pop_updateinfo, null);
		((TextView) view.findViewById(R.id.textView_text)).setText(text);
		Button yes_button = (Button) view.findViewById(R.id.button_yes);
		yes_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isDowning = true;
				pBar = new ProgressDialog(context);
				pBar.setTitle("");
				pBar.setMessage("正在下载，请稍候…");
				pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pBar.setCanceledOnTouchOutside(false);
				pBar.setCancelable(false);
				dialog.cancel();
				DownLoadTask task = new DownLoadTask(context, pBar, size);
				task.execute("http://www.wanzhuan6.com/down/wanzhuan.apk");
			}
		});
		Button no_button = (Button) view.findViewById(R.id.button_no);
		no_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (!isForce) {
					HandleUtil.sendInt(hd, -2);
				}
			}
		});
	}

	class UpDateRunnble implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// if
			// (Float.parseFloat(newVersion)-Float.parseFloat(getCurrentVersion())>=0.02)
			// {
			// isForce=true;
			// Toast.makeText(context, "您的版本太老了，已经不能运行了，请更新最新版本",
			// Toast.LENGTH_LONG).show();
			// }
			createView();
			dialog = new Dialog(context, R.style.MyDialogStyle);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					if (!isDowning && isForce) {
						// hd.sendEmptyMessage(1);
						((Activity) context).finish();
					}
				}
			});
			dialog.setContentView(view);
			dialog.show();
		}
	}

	public static String getLocationMethod(String reqUrl, Context context) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String location = null;
		int responseCode = 0;
		try {
			final HttpGet request = new HttpGet(reqUrl);
			HttpParams params = new BasicHttpParams();
			params.setParameter("http.protocol.handle-redirects", false); // 默认不让重定向
																			// 这样就能拿到Location头了
			request.setParams(params);
			HttpResponse response = httpclient.execute(request);
			responseCode = response.getStatusLine().getStatusCode();
			Header[] headers = response.getAllHeaders();

			if (responseCode == 302) {
				Header locationHeader = response.getFirstHeader("Location");
				if (locationHeader != null) {
					location = locationHeader.getValue();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}

}
