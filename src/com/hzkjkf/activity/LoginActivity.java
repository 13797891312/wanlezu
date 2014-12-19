package com.hzkjkf.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.util.SaveDate;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	private Button login_button;
	private Button register_button;
	private EditText phone_edittext;
	private EditText password_edittext, add_edit;
	private ProgressDialog dialog;
	private CheckBox record_checkbox;
	private TextView lose_textview;
	private SaveDate sd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 友盟更新组件
		// UmengUpdateAgent.setUpdateOnlyWifi(false);
		// UmengUpdateAgent.update(this);
		setContentView(R.layout.activity_login);
		MyApp.map.put("login", this);// 记录当前ACTIVITY
		add_edit = (EditText) findViewById(R.id.editText1);
		phone_edittext = (EditText) findViewById(R.id.phone_edittext);
		password_edittext = (EditText) findViewById(R.id.password_edittext);
		record_checkbox = (CheckBox) findViewById(R.id.record_checkbox);
		login_button = (Button) findViewById(R.id.button_login);
		register_button = (Button) findViewById(R.id.register_btn);
		lose_textview = (TextView) findViewById(R.id.lose_textview);
		lose_textview.setOnClickListener(this);
		login_button.setOnClickListener(this);
		register_button.setOnClickListener(this);
		initEdit();
	}

	/** 先判读是否有记录密码，如果有记录直接显示 ***/
	private void initEdit() {
		// TODO Auto-generated method stub
		sd = SaveDate.getInstence(this.getApplicationContext());
		record_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						sd.setRecord(isChecked);
					}
				});
		record_checkbox.setChecked(sd.isRecord());
		if (sd.isRecord()) {
			phone_edittext.setText(sd.getPhone());
			password_edittext.setText(sd.getPassword());
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (!add_edit.getText().toString().isEmpty()) {// 用于测试时选择服务器
			MyApp.getInstence().address = add_edit.getText().toString();
		}
		int id = v.getId();
		if (id == R.id.button_login) {
			login();
		} else if (id == R.id.register_btn) {
			Intent intent2 = new Intent(this, Register1Activity.class);
			intent2.putExtra("type", 1);
			this.startActivityForResult(intent2, 0);
		} else if (id == R.id.lose_textview) {
			Intent intent1 = new Intent(this, Register1Activity.class);
			intent1.putExtra("type", 2);
			this.startActivityForResult(intent1, 0);
		} else {
		}
	}

	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			if (msg.what == 1) {
				LoginActivity.this.startActivity(new Intent(LoginActivity.this,
						MainActivity.class));
				LoginActivity.this.finish();
				MyApp.map.remove("login");
			} else if (msg.what == 0) {
				Toast.makeText(LoginActivity.this, "登陆失败，请检查网络连接", 1000).show();
			} else {
				Toast.makeText(LoginActivity.this,
						ErrorCode.getString(msg.what), 1000).show();
			}
		};
	};

	/**** 发送登陆请求 ***/
	private void login() {
		if (phone_edittext.getText().length() < 11
				|| !HttpTool.isPhone(phone_edittext.getText().toString())) {
			phone_edittext.setError("请输入正确的手机号");
			return;
		}
		if (password_edittext.getText().length() < 6
				|| !HttpTool.isGoodPWD(password_edittext.getText().toString())) {
			password_edittext.setError("请输入正确的密码");
			return;
		}

		if (!isSimExist()) {
			Toast.makeText(this, "未检测到SIM卡，请插入SIM", Toast.LENGTH_SHORT).show();
			return;
		}
		MyApp.getInstence().setImei(((TelephonyManager) this
				.getSystemService(TELEPHONY_SERVICE)).getDeviceId());
		dialog = ProgressDialog.show(this, "", "正在登陆，请稍后....");
		if (record_checkbox.isChecked()) {
			sd.setPhone(phone_edittext.getText().toString(), password_edittext
					.getText().toString());
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "pwd", "imei" },
						new String[] { "10003",
								phone_edittext.getText().toString(),
								password_edittext.getText().toString(),
								MyApp.getInstence().getImei() });
				String json = HttpTool.httpGetJson1(LoginActivity.this, url,
						LoginActivity.this.hd);
				if (!json.isEmpty()) {
					if (HttpTool.getFlag(json)) {
						JSONObject object1;
						try {
							object1 = new JSONArray(json).getJSONObject(0);
							JSONObject object = object1.getJSONObject("result");
							MyApp.getInstence().setToken(
									object1.getString("requestId"));
							MyApp.getInstence().setPhone(
									object1.getString("phoneNumber"));
							MyApp.getInstence().setSurplus(
									Float.parseFloat(object
											.getString("accountBalance")));
							MyApp.getInstence().maxMoney = Float
									.parseFloat(object.getString("makeMoney"));
							MyApp.getInstence().name = object
									.getString("userName") == null ? ""
									: object.getString("userName");
							MyApp.getInstence().aliCount = object
									.getString("aliCount") == null ? ""
									: object.getString("aliCount");
							MyApp.getInstence().InviteCode = object
									.getString("userInviteCode");
							MyApp.getInstence().QQ = object.getString("qqNum") == null ? ""
									: object.getString("qqNum");
							MyApp.getInstence().birthday = object
									.getString("birthday");
							MyApp.getInstence().email = object
									.getString("email");
							MyApp.getInstence().expPosition = object
									.getString("expPosition") == null ? ""
									: object.getString("expPosition");
							MyApp.getInstence().profession = object
									.getString("profession") == null ? ""
									: object.getString("profession");
							MyApp.getInstence().edu = object
									.getString("education") == null ? ""
									: object.getString("education");
							MyApp.getInstence().expName = object
									.getString("expName") == null ? "" : object
									.getString("expName");
							MyApp.getInstence().lv = FormatStringUtil
									.getLvInt(object.getString("userLvl"));
							MyApp.getInstence().sex = object.getString("sex")
									.equals("1") ? "男" : "女";
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						hd.sendEmptyMessage(1);
					} else {
						hd.sendEmptyMessage(HttpTool.getErrorCode(json));
					}
				} else {
					hd.sendEmptyMessage(0);
				}
			}
		}).start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == 1) {
			phone_edittext.setText(data.getStringExtra("phone"));
		}
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

	private boolean isSimExist() {
		boolean isSim = false;
		TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		int simState = mTelephonyManager.getSimState();
		isSim = !(simState == TelephonyManager.SIM_STATE_ABSENT);
		LogUtils.e("sim", isSim + " ");
		return isSim;
	}
}
