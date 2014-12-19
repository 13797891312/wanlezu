package com.hzkjkf.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register2Activity extends Activity implements OnClickListener {
	// private Button btn_login;
	private Button button_register;
	private TextView phone_edittext;
	private EditText password_1;
	private EditText password_2;
	private ProgressDialog dialog;
	private String phone;
	private boolean isOk;
	private String randomCode;
	private String password1;
	private String password2;
	private String code_editview1;
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			button_register.setEnabled(true);
			if (dialog != null) {
				dialog.cancel();
			}
			if (msg.what == 0) {
				Toast.makeText(Register2Activity.this, "网络不给力，请检测网络或重新连接",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 1) {
				password_1.setText("");
				password_2.setText("");
				phone_edittext.setText("");
				button_register.setText("登陆");
				isOk = true;
				((TextView) findViewById(R.id.textView_log))
						.setText("注册成功，请点击登录按钮");
				((TextView) findViewById(R.id.textView_log))
						.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(Register2Activity.this,
						ErrorCode.getString(msg.what), Toast.LENGTH_SHORT)
						.show();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_2);
		((TextView) findViewById(R.id.title)).setText("欢迎注册玩赚");
		button_register = (Button) findViewById(R.id.button_register);
		password_1 = (EditText) findViewById(R.id.edittext_password1);
		password_2 = (EditText) findViewById(R.id.edittext_password2);
		// code_editview = (EditText) findViewById(R.id.edittext_code);
		button_register.setOnClickListener(this);
		phone_edittext = (TextView) findViewById(R.id.edittext_phone);
		phone = this.getIntent().getStringExtra("phone");
		randomCode = this.getIntent().getStringExtra("randomCode");
		phone_edittext.setText(phone);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.button_register) {
			if (isOk) {
				login();
			} else {
				register();
			}
		} else {
		}
	}

	/** 注册 **/
	private void register() {
		// TODO Auto-generated method stub
		password1 = password_1.getText().toString();
		password2 = password_2.getText().toString();
		code_editview1 = getCode();
		if (code_editview1.isEmpty()) {
			code_editview1 = stringFromJNI1(code_editview1);
		}
		LogUtils.e("asdfasdf", "code_editview1" + code_editview1);
		if (password1.length() < 6 || password1.length() > 16
				|| !HttpTool.isGoodPWD(password1)) {
			password_1.setError("密码为6-16位数值和字母组成");
			return;
		} else if (!password1.equals(password2)) {
			password_2.setError("两次输入的密码不一致");
			return;
		}
		dialog = ProgressDialog.show(this, "", "正在注册，请稍后。。。。");
		button_register.setEnabled(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "pwd", "inviteCode", "imei",
						"randomCode" },
						new String[] { "10002", phone,
								password_2.getText().toString(),
								code_editview1, MyApp.getInstence().getImei(),
								randomCode });
				String json = HttpTool.httpGetJson1(Register2Activity.this,
						url, hd);
				if (!json.isEmpty()) {
					Map<String, String> map = HttpTool.jsonToMap(json);
					if (Boolean.parseBoolean(map.get("flag").toString())) {// 如果注册成功
						hd.sendEmptyMessage(1);
					} else {// 如果注册失败
						hd.sendEmptyMessage(Integer.parseInt(map
								.get("errorCode")));
					}
				} else {// 没有json返回
					hd.sendEmptyMessage(0);
				}
			}
		}).start();
	}

	private String getCode() {
		// TODO Auto-generated method stub
		String code = "";
		try {
			InputStream is = this.getResources().getAssets().open("code.txt");
			BufferedReader buf = new BufferedReader(new InputStreamReader(is));
			code = buf.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			code = "";
		}
		return code;
	}

	/** 返回按钮监听 ***/
	public void backClick(View v) {
		this.finish();
	}

	/**** 发送登陆请求 ***/
	private void login() {
		dialog = ProgressDialog.show(this, "", "正在登陆，请稍后....");
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "pwd", "imei" }, new String[] { "10003",
						phone, password1, MyApp.getInstence().getImei() });
				String json = HttpTool.httpGetJson1(Register2Activity.this,
						url, Register2Activity.this.hd_login);
				if (!json.isEmpty()) {
					Map<String, String> map = HttpTool.jsonToMap(json);
					if (Boolean.parseBoolean(map.get("flag").toString())) {
						MyApp.getInstence().setToken(map.get("requestId"));
						MyApp.getInstence().setPhone(map.get("phoneNumber"));
						hd_login.sendEmptyMessage(1);
					} else {
						hd_login.sendEmptyMessage(Integer.parseInt(map
								.get("errorCode")));
					}
				} else {
					hd_login.sendEmptyMessage(0);
				}
			}
		}).start();
	}

	Handler hd_login = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			if (msg.what == 1) {
				Register2Activity.this.startActivity(new Intent(
						Register2Activity.this, MainActivity.class));
				if (MyApp.map.get("login") != null) {
					MyApp.map.get("login").finish();
					MyApp.map.remove("login");
				}
				if (MyApp.map.get("regist1") != null) {
					MyApp.map.get("regist1").finish();
					MyApp.map.remove("regist1");
				}
				isOk = false;
				Register2Activity.this.finish();
			} else if (msg.what == 0) {
				Toast.makeText(Register2Activity.this, "登陆失败，请检查网络连接", 1000)
						.show();
			} else {
				Toast.makeText(Register2Activity.this,
						ErrorCode.getString(msg.what), 1000).show();
			}
		};
	};

	public native String stringFromJNI(String code);

	public native String stringFromJNI1(String code);

	static {
		System.loadLibrary("hello-jni");
	}
}
