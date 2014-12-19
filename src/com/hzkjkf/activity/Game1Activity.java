package com.hzkjkf.activity;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.activity.MainActivity;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.util.MySound;
import com.hzkjkf.view.GameView;
import com.hzkjkf.view.GameView.Data;
import com.hzkjkf.wanzhuan.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Game1Activity extends Activity implements OnClickListener {
	private TextView count_textview, surplus_textview;
	private Button start_button;
	private int count = 5;
	/**** �Ƿ��м��ع� *****/

	/** �н���� **/
	private float bonusMoney = 0.0f;
	/*** ��ʼ�齱��ʱ���Ѿ��������ݣ����û����ת�̾Ͳ���ͣ ***/
	private boolean isOk;
	private String message;
	private MySound mySound;
	GameView gv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game1);
		((TextView) findViewById(R.id.title)).setText("���˴�ת��");
		gv = (GameView) findViewById(R.id.gemeView);
		surplus_textview = (TextView) findViewById(R.id.textview_surplus_value);
		count_textview = (TextView) findViewById(R.id.textView_count);
		start_button = (Button) findViewById(R.id.button_start);
		start_button.setOnClickListener(this);
		mySound = MySound.getInstence(this.getApplicationContext());
		surplus_textview.setText(MyApp.getInstence().getSurplus() + "���");
		initData();
		count_textview.setText("���컹ʣ" + count + "�γ齱����");
	}

	private void initData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei" }, new String[] {
						"10013", MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei() });
				String json = HttpTool
						.httpGetJson1(Game1Activity.this, url, hd);
				if (!json.isEmpty()) {
					JSONArray result = HttpTool.getResult(json);
					try {
						JSONObject object = result.getJSONObject(0);
						count = object.getInt("remainGameCount");
						HandleUtil.sendInt(hd, 4);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();

	}

	Data lastData;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (count <= 0) {
			Toast.makeText(this, "������ĳ齱���������꣬����������", 2000).show();
			return;
		} else if (MyApp.getInstence().getSurplusFloat() < 0.1) {
			Toast.makeText(this, "��������", 2000).show();
			return;
		}
		// ���ͳ齱����
		httpPost();
		// ��ʼ�齱
		startBonus();

	}

	/*** ���ͳ齱���� ***/
	private void httpPost() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei" }, new String[] {
						"10012", MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei() });
				String json = HttpTool
						.httpGetJson1(Game1Activity.this, url, hd);
				if (!json.isEmpty()) {
					JSONArray result = HttpTool.getResult(json);
					try {
						JSONObject object = result.getJSONObject(0);
						bonusMoney = Float.parseFloat(object
								.getString("picturePosition"));
						message = object.getString("rewardMsg");
						HandleUtil.sendInt(hd, 3);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();
	}

	/** ��ʼת�� ***/
	private void startBonus() {
		start_button.setEnabled(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int count = 0;
				int time = 500;
				int num = 0;
				if (lastData != null) {
					count = lastData.getCount();
				}
				boolean isStop = false;
				boolean isSlow = false;

				int slowNum = 20 + new Random().nextInt(14);// ����һ���������������ת�̼���
				while (!isStop) {
					if (count >= gv.getList().size()) {
						count = 0;
					}
					gv.getList().get(count).setChecked(true);
					if (lastData != null) {
						lastData.setChecked(false);

					}
					lastData = gv.getList().get(count);
					HandleUtil.sendInt(hd, 1);
					try {
						if (time > 100 && num == 0) {
							time -= 50;
						} else {
							num++;
						}
						if (isOk && num >= slowNum && time <= 500
								&& lastData.getInfo() == bonusMoney) {
							isSlow = true;
						}
						if (isSlow) {
							time += 20;
						}
						if (time >= 400 && lastData.getInfo() == bonusMoney
								&& isSlow) {
							isStop = true;
							isSlow = false;
							HandleUtil.sendInt(hd, 2);
						}
						Thread.sleep(time);
						count++;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				Toast.makeText(Game1Activity.this, "������ϣ����γ齱����", 3000).show();
				isOk = true;
			} else if (msg.what == 1) {
				mySound.play(mySound.getSound1());
				gv.invalidate();
			} else if (msg.what == 2) {
				MyApp.getInstence().setSurplus(
						MyApp.getInstence().getSurplusFloat() + bonusMoney);
				if (bonusMoney > 0) {
					mySound.play(mySound.getSound2());
					Toast.makeText(
							Game1Activity.this,
							"��ϲ���н����"
									+ FormatStringUtil.getDesplay(bonusMoney
											+ "") + "���", 3000).show();
				} else {
					mySound.play(mySound.getSound3());
					Toast.makeText(Game1Activity.this, "���ٽ�������", 3000).show();
				}
				bonusMoney = 0;
				surplus_textview.setText(MyApp.getInstence().getSurplus()
						+ "���");
				count_textview.setText("���컹ʣ" + count + "�γ齱����");
				isOk = false;
				start_button.setEnabled(true);
			} else if (msg.what == 3) {// �õ�����ɹ���ת�̿�ʼת��
				MyApp.getInstence().setSurplus(
						MyApp.getInstence().getSurplusFloat() - 0.1f);
				count--;
				surplus_textview.setText(MyApp.getInstence().getSurplus()
						+ "���");
				count_textview.setText("���컹ʣ" + count + "�γ齱����");
				isOk = true;

			} else if (msg.what == 4) {// �õ�����ɹ���ת�̿�ʼת��
				count_textview.setText("���컹ʣ" + count + "�γ齱����");
			}
		};
	};

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		hd = null;
	}

	public void backClick(View v) {
		this.finish();
	}
}
