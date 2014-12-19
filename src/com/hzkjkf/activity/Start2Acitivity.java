package com.hzkjkf.activity;

import com.hzkjkf.location.MyLocation;
import com.hzkjkf.update.UpDateInfo;
import com.hzkjkf.util.BitmapUtil;
import com.hzkjkf.util.Blowfish;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.util.SaveDate;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class Start2Acitivity extends Activity {
	private Bitmap bitmap;
	/** ���ڶ�λ�Ķ��� ***/
	Runnable run = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			SaveDate sd = SaveDate.getInstence(Start2Acitivity.this
					.getApplicationContext());
			sd.readDate();
			boolean isOne = sd.isOnce();
			if (isOne) {
				sd.setIsonce();
				Start2Acitivity.this.startActivity(new Intent(
						Start2Acitivity.this, Start1Activity.class));
			} else {
				Start2Acitivity.this.startActivity(new Intent(
						Start2Acitivity.this, LoginActivity.class));
			}
			Start2Acitivity.this.finish();
		}
	};
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case -1:
				Toast.makeText(Start2Acitivity.this, "����������Ӧ,������������",
						Toast.LENGTH_SHORT).show();
				break;
			case 0:
				Toast.makeText(Start2Acitivity.this, "��ǰ�����磬��������",
						Toast.LENGTH_SHORT).show();
				break;
			case -2:
				// 2����Զ��������
				hd.postDelayed(run, 2000);
				break;
			case 1:
				hd.postDelayed(run, 2000);
				break;
			default:

				Toast.makeText(Start2Acitivity.this,
						ErrorCode.getString(msg.what), 3000).show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stubis
		super.onCreate(savedInstanceState);
		// ȫ��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		MobclickAgent.updateOnlineConfig(this.getApplicationContext());// ����ͳ��
		/* ��ȡ�ֻ�imei�� */

		SaveDate.getInstence(this).readDate();
		SaveDate.getInstence(this).setImei(MyApp.getInstence().getImei());
		// TelephonyManager tm = (TelephonyManager)
		// getSystemService(TELEPHONY_SERVICE);
		// String SimSerialNumber = tm.getSimSerialNumber();
		// Log.e("SimSerialNumber", SimSerialNumber);
		ImageView iv = new ImageView(this);
		bitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.u13_normal);
		iv.setBackgroundDrawable(new BitmapDrawable(bitmap));
		setContentView(iv);
		// hd.postDelayed(run, 2000);

		UpDateInfo info = new UpDateInfo(this, hd);
		info.UpDate();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		HandleUtil.removeCallbacks(hd, run);
	}
}
