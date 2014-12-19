package com.hzkjkf.more;

import com.hzkjkf.update.UpDateInfo;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class Version extends Activity {
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(Version.this, "当前无网络，请检查网络", Toast.LENGTH_SHORT)
						.show();
				break;
			case 1:
				Toast.makeText(Version.this, "您已经是最新版本，无需更新",
						Toast.LENGTH_SHORT).show();
				break;
			case -2:
				break;
			default:

				Toast.makeText(Version.this, ErrorCode.getString(msg.what),
						3000).show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_version);
		// 友盟更新组件
		// UmengUpdateAgent.setUpdateOnlyWifi(false);
		// UmengUpdateAgent.update(this);
		((TextView) findViewById(R.id.title)).setText("版本更新");

		// 自己服务器更新接口
		UpDateInfo info = new UpDateInfo(this, hd);
		info.UpDate();
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
		hd = null;
	}
}
