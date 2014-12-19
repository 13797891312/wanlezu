package com.hzkjkf.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class Register1Activity extends Activity implements OnClickListener,
		TextWatcher, OnCheckedChangeListener {
	/*** ������֤�밴ť ***/
	private Button getcode_btn;
	/**** ��һ����ť *****/
	private Button btn_next;
	/***** �ֻ��ű༭�� *****/
	private EditText phone_edittext;
	/***** ��֤��༭�� ****/
	private EditText getcode_edittext;
	private ProgressDialog dialog;
	/*** ��ʾ���һ�����ҳ�滹��ע��ҳ�棬��Ϊ���ò��֣����Լ򻯴��빫��һ��activity **/
	private int type;
	private TextView tv;
	private CheckBox cb;
	private boolean isSoundCode = false;
	private EditText edittext_newPWD, edittext_newPWD2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_1);
		((TextView) findViewById(R.id.title)).setText("�ֻ���֤");
		type = this.getIntent().getIntExtra("type", 1);
		edittext_newPWD = (EditText) findViewById(R.id.edittext_newPWD);
		edittext_newPWD2 = (EditText) findViewById(R.id.edittext_newPWD2);
		switch (type) {
		case 1:
			findViewById(R.id.rel_newPWD).setVisibility(View.GONE);
			findViewById(R.id.rel_newPWD2).setVisibility(View.GONE);
			break;
		case 2:
			findViewById(R.id.rel_newPWD).setVisibility(View.VISIBLE);
			findViewById(R.id.rel_newPWD2).setVisibility(View.VISIBLE);
			break;
		}
		tv = (TextView) findViewById(R.id.textView1);
		tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tv.setOnClickListener(this);
		cb = (CheckBox) findViewById(R.id.checkBox1);
		cb.setOnCheckedChangeListener(this);
		getcode_btn = (Button) findViewById(R.id.button_getcode);
		btn_next = (Button) findViewById(R.id.button_next);
		getcode_btn.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		phone_edittext = (EditText) findViewById(R.id.edittext_phone);
		getcode_edittext = (EditText) findViewById(R.id.edittext_code);
		phone_edittext.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.button_getcode) {
			getCode();
		} else if (id == R.id.button_next) {
			next();
		} else {
		}
	}

	private int count = 60;
	private Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null)
				dialog.cancel();
			if (msg.what == 0) {
				Toast.makeText(Register1Activity.this, "���粻��������������",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 1) {
				getcode_btn.setEnabled(false);
				getcode_btn.setText("�ѷ���(" + count + ")");
				hd.postDelayed(runnable, 1000);
			} else if (msg.what == 2) {
				switch (type) {
				case 2:
					Toast.makeText(Register1Activity.this, "�޸ĳɹ������������ܺ��½",
							Toast.LENGTH_SHORT).show();
					Register1Activity.this.finish();
					break;
				case 1:
					Intent intent = new Intent(Register1Activity.this,
							Register2Activity.class);
					intent.putExtra("phone", phone_edittext.getText()
							.toString());
					intent.putExtra("randomCode", getcode_edittext.getText()
							.toString());
					/**** ��¼��ǰActivity������һ��������¼ֱ�ӷ��ص�¼��������ٵ�ǰActivity ***/
					MyApp.map.put("regist1", Register1Activity.this);
					Register1Activity.this.startActivity(intent);
					break;
				}
			} else {
				Toast.makeText(Register1Activity.this,
						ErrorCode.getString(msg.what), Toast.LENGTH_SHORT)
						.show();
			}

		};
	};
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			count--;
			getcode_btn.setText("�ѷ���(" + count + ")");
			if (count > 0) {
				hd.postDelayed(this, 1000);
			} else {
				getcode_btn.setEnabled(true);
				getcode_btn.setText("������֤��");
				isSoundCode = true;
				count = 60;
			}
		}
	};

	/** ���ذ�ť���� ***/
	public void backClick(View v) {
		this.finish();
	}

	/** ��һ�� ***/
	private void next() {
		// TODO Auto-generated method stub
		if (phone_edittext.getText().toString().length() < 11) {
			phone_edittext.setError("��������ȷ���ֻ���");
			return;
		}
		if (getcode_edittext.getText().toString().length() < 4) {
			getcode_edittext.setError("��������ȷ����֤��");
			return;
		}
		if (type == 2) {
			if (!HttpTool.isGoodPWD(edittext_newPWD.getText().toString())) {
				edittext_newPWD.setError("����Ϊ6-16λ��ֵ����ĸ���");
				return;
			}
			if (!edittext_newPWD2.getText().toString()
					.equals(edittext_newPWD.getText().toString())) {
				edittext_newPWD2.setError("������������벻һ��");
				return;
			}
		}
		dialog = ProgressDialog.show(this, "", "������֤�����Ժ�....");
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// getCity();// ��ȡ������
				String url;
				if (type == 1) {
					url = HttpTool.getUrl(
							new String[] { "classId", "phoneNumber",
									"randomType", "imei", "randomCode" },
							new String[] { "10024",
									phone_edittext.getText().toString(),
									String.valueOf(type),
									MyApp.getInstence().getImei(),
									getcode_edittext.getText().toString() });
				} else {
					url = HttpTool.getUrl(
							new String[] { "classId", "phoneNumber",
									"randomType", "imei", "randomCode", "pwd" },
							new String[] { "10052",
									phone_edittext.getText().toString(),
									String.valueOf(type),
									MyApp.getInstence().getImei(),
									getcode_edittext.getText().toString(),
									edittext_newPWD2.getText().toString() });
				}
				String json = HttpTool.httpGetJson1(Register1Activity.this,
						url, hd);
				if (!json.isEmpty()) {
					Map<String, String> map = HttpTool.jsonToMap(json);
					if ((Boolean.parseBoolean(map.get("flag").toString()))) {
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

	/*** ��ȡ�ֻ������� ***/
	private void getCity() {
		String url = "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getMobileCodeInfo?mobileCode="
				+ phone_edittext.getText().toString() + "&userID=";
		try {
			HttpURLConnection connection = (HttpURLConnection) (new URL(url)
					.openConnection());
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			InputStream is = connection.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			// String line = "";
			// StringBuffer sb = new StringBuffer();
			// while ((line = reader.readLine()) != null) {
			// sb.append(line);
			// }
			// // is.close();
			// Log.e("sdf", sb.toString());

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			Document document;
			document = db.parse(is);

			Element el = document.getDocumentElement();
			String node = el.getTextContent();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** ��ȡ��֤�� ***/
	private void getCode() {
		if (phone_edittext.getText().toString().length() < 11
				|| !HttpTool.isPhone(phone_edittext.getText().toString())) {
			phone_edittext.setError("��������ȷ���ֻ���");
			return;
		}
		dialog = ProgressDialog.show(this, "", "���ڷ��ͣ����Ժ�....");
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "randomType",
								"imei", "sign" },
						new String[] { "10001",
								phone_edittext.getText().toString(),
								String.valueOf(type), MyApp.getInstence().getImei(),
								isSoundCode ? "2" : "1" });
				String json = HttpTool.httpGetJson1(Register1Activity.this,
						url, hd);
				if (!json.isEmpty()) {
					Map<String, String> map = HttpTool.jsonToMap(json);
					if ((Boolean.parseBoolean(map.get("flag").toString()))) {
						hd.sendEmptyMessage(1);
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

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if (s.length() == 11 && cb.isChecked()) {
			getcode_btn.setEnabled(true);
		} else {
			getcode_btn.setEnabled(false);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (phone_edittext.getText().length() == 11) {
			getcode_btn.setEnabled(isChecked);
		}
		btn_next.setEnabled(isChecked);
	}

}
