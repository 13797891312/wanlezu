package com.hzkjkf.service_receiver;

import com.hzkjkf.activity.TaskInfoActivity;
import com.hzkjkf.util.LogUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

public class AppReceiver extends BroadcastReceiver {
	private static final int PACKAGE_NAME_START_INDEX = 8;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null) {
			return;
		}
		if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
			String data = intent.getDataString();

			if (data == null || data.length() <= PACKAGE_NAME_START_INDEX) {
				return;
			}
			String packageName = data.substring(PACKAGE_NAME_START_INDEX);
			if (TaskInfoActivity.downMap.containsKey(packageName)) {
				Intent intent2 = new Intent(context, AppService.class);
				intent2.putExtra("packageName", packageName);
				context.startService(intent2);
			} else {
				LogUtils.e("packageName", packageName);
			}
		}
	}
}
