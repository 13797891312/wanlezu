package com.hzkjkf.more;

import java.util.Map;

import com.hzkjkf.util.EbotongSecurity;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**** 修改密码 *****/
public class PassWord extends Activity {
	private EditText oldPsd, newPsd1, newPsd2;
	private Button ok_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_password);
		((TextView) findViewById(R.id.title)).setText("修改密码");
		oldPsd = (EditText) findViewById(R.id.eidtview_oldPassword);
		newPsd1 = (EditText) findViewById(R.id.edittext_newPassword);
		newPsd2 = (EditText) findViewById(R.id.edittext_again);
		ok_button = (Button) findViewById(R.id.ok_button);
		ok_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				subMit();
			}

		});
	}

	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ok_button.setClickable(true);
			if (msg.what == 0) {
				Toast.makeText(PassWord.this, "网络不给力，请检查网络", Toast.LENGTH_SHORT)
						.show();
			} else if (msg.what == 2) {
				Toast.makeText(PassWord.this, "修改成功", Toast.LENGTH_SHORT)
						.show();
				PassWord.this.finish();
			} else {
				Toast.makeText(PassWord.this, ErrorCode.getString(msg.what),
						Toast.LENGTH_SHORT).show();
			}
		};
	};

	private void subMit() {
		// TODO Auto-generated method stub
		if (!HttpTool.isGoodPWD(oldPsd.getText().toString())) {
			oldPsd.setError("密码为6-16位数值和字母组成");
			return;
		}
		if (!HttpTool.isGoodPWD(newPsd1.getText().toString())) {
			newPsd1.setError("密码为6-16位数值和字母组成");
			return;
		}
		if (newPsd1.getText().toString().equals(oldPsd.getText().toString())) {
			newPsd1.setError("原密码和新密码不能相同");
			return;
		}
		if (!newPsd2.getText().toString().equals(newPsd1.getText().toString())) {
			newPsd2.setError("两次输入的密码不一致");
			return;
		}
		ok_button.setClickable(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "requestId",
								"imei", "pwdOld", "pwd" },
						new String[] {
								"10022",
								MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(),
								MyApp.getInstence().getImei(),
								EbotongSecurity
										.MD5(oldPsd.getText().toString()),
								newPsd1.getText().toString() });
				String json = HttpTool.httpGetJson1(PassWord.this, url,
						PassWord.this.hd);
				if (!json.isEmpty()) {
					Map<String, String> map = HttpTool.jsonToMap(json);
					if (Boolean.parseBoolean(map.get("flag").toString())) {
						hd.sendEmptyMessage(2);
					} else {
						hd.sendEmptyMessage(Integer.parseInt(map
								.get("errorCode")));
					}
				} else {
					hd.sendEmptyMessage(0);
				}
			}
		}).start();
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
}
