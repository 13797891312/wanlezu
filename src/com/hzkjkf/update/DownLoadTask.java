package com.hzkjkf.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceActivity.Header;
import android.util.Log;
import android.widget.Toast;

public class DownLoadTask extends AsyncTask<String, Integer, Integer> {
	private Context context;
	private ProgressDialog dialog;
	private int size;

	public DownLoadTask(Context context, ProgressDialog dialog, int size) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.dialog = dialog;
		// this.size=size;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog.show();
		dialog.setMax(size);
		dialog.setProgressNumberFormat("%1d kb/%2d kb");
	}

	@Override
	protected Integer doInBackground(String... params) {
		File fileOld = new File(Environment.getExternalStorageDirectory(),
				"wzapp.apk");
		if (fileOld.exists()) {
			fileOld.delete();
		}
		int count = 0;
		// TODO Auto-generated method stub
		HttpClient client = new DefaultHttpClient();
		// params[0]代表连接的url
		HttpGet get = new HttpGet(params[0]);
		HttpResponse response;
		try {
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			size = (int) entity.getContentLength() / 1024;
			InputStream is = entity.getContent();
			FileOutputStream fileOutputStream = null;
			if (is != null) {
				File file = new File(Environment.getExternalStorageDirectory(),
						"wzapp.apk");
				fileOutputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024 * 100];
				int ch = -1;
				while ((ch = is.read(buf)) != -1) {
					fileOutputStream.write(buf, 0, ch);
					count += ch;
					publishProgress(count, size);
				}
			}
			fileOutputStream.flush();
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dialog.getProgress();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		dialog.setMax(size);
		dialog.setProgress(values[0] / 1000);
	}

	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		dialog.cancel();
		update();
	}

	/*** 安装APK ***/
	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File("/sdcard/wzapp.apk")),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
		((Activity) context).finish();
	}

}
