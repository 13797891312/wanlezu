package com.hzkjkf.service_receiver;

import org.json.JSONArray;

import com.hzkjkf.activity.NewcomerRedAcitivity;
import com.hzkjkf.activity.TaskInfoActivity;
import com.hzkjkf.javabean.TaskData;
import com.hzkjkf.util.DownFileUtil;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.view.View;
import android.widget.Toast;

public class AppService extends Service {
	private TaskData data;
	private CountDownTimer timer;
	/** �Ƿ��Ѿ��˳�APP ***/
	private boolean isEixt;
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(AppService.this, "�������Ӵ���", Toast.LENGTH_LONG)
						.show();
				break;
			case 1:
				Toast.makeText(AppService.this, "��ϲ���ĵ����ν���", Toast.LENGTH_LONG)
						.show();
				Intent intent = new Intent("com.hzkjkf.action.TaskOk");
				AppService.this.sendBroadcast(intent);// ���ͳɹ��㲥
				break;
			default:
				Toast.makeText(AppService.this, ErrorCode.getString(msg.what),
						Toast.LENGTH_LONG).show();
				break;
			}
			AppService.this.stopSelf();// ��������
		};
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		data = TaskInfoActivity.data;
		DownFileUtil.startAPP(data.getAPKname(), this);
		if (data.getDesc_Type() != -1) {
			Toast.makeText(this, data.getToast(), Toast.LENGTH_LONG).show();
		} else {
			stopSelf();
			return super.onStartCommand(intent, flags, startId);
		}
		timer = new CountDownTimer(60 * 60 * 1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				/*** ����˳���Ӧ�� ***/
				if (!DownFileUtil.getCurrentPk(AppService.this).equals(
						data.getAPKname())
						&& !isEixt) {
					isEixt = true;// ��ֹѭ��ִ��ʱ������
					if (60 * 60 * 1000 - millisUntilFinished > data
							.getDesc_time() * 1000) {
						/*** �ύ���� **/
						initData();
					} else {
						if (data.getDesc_Type() != -1) {
							Toast.makeText(AppService.this, "����һ����ܵõ�����Ŷ��",
									Toast.LENGTH_LONG).show();
						}
						AppService.this.stopSelf();// ��������
					}
				}
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
			}
		}.start();
		return super.onStartCommand(intent, flags, startId);
	}

	/*** �ύ���� ***/
	private void initData() {
		if (data.getDesc_Type() == -1) {
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "requestId",
								"imei", "applyUids", "logUids", "desc_Type" },
						new String[] { "10054", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken_downTask(),
								MyApp.getInstence().getImei(), data.getUids(),
								data.getLogUids(),
								String.valueOf(data.getDesc_Type()) });
				String json = HttpTool.httpGetJson1(AppService.this, url, hd);
				if (!json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					HandleUtil.sendInt(hd, 1);
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (timer != null) {
			timer.cancel();// ֹͣ��ʱ����
		}
		LogUtils.e("����ֹͣ", "stop");
	}
}
