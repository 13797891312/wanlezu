package com.hzkjkf.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.adapter.Collect_gridview_Adapter;
import com.hzkjkf.adapter.Game2Record_listview_adapter;
import com.hzkjkf.imageloader.Imageloader;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Game2RecordActivity extends Activity implements OnClickListener {
	private ProgressDialog dialog;
	private TextView last_tv, next_tv, number_tv, luckNum_tv, myCode_tv;
	private ListView game2Record_listView;
	private Game2Record_listview_adapter gla;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private String lastNumber = "";
	private String nextNumber = "";
	private String luckCode = "";
	private String myCode = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game2record);
		((TextView) findViewById(R.id.title)).setText(this.getIntent()
				.getStringExtra("�������н���ѯ"));
		last_tv = (TextView) findViewById(R.id.textView_last);
		next_tv = (TextView) findViewById(R.id.textView_next);
		last_tv.setOnClickListener(this);
		next_tv.setOnClickListener(this);
		luckNum_tv = (TextView) findViewById(R.id.textView_luck);
		myCode_tv = (TextView) findViewById(R.id.textView_myCode);
		number_tv = (TextView) findViewById(R.id.textView_3);
		game2Record_listView = (ListView) findViewById(R.id.listview);
		number_tv.setText("��" + getIntent().getStringExtra("number") + "��");
		luckNum_tv.setText("�����н����� �� " + getIntent().getStringExtra("luck"));
		initDate(getIntent().getStringExtra("number"));
		listAddTitle();
		gla = new Game2Record_listview_adapter(this, list);
		game2Record_listView.setAdapter(gla);
	}

	private void listAddTitle() {
		Map<String, String> map_title = new HashMap<String, String>();
		map_title.put("name", "�н��û�");
		map_title.put("code", "Ͷע����");
		map_title.put("money", "�н����");
		map_title.put("rank", "�н�����");
		map_title.put("phone", "");
		list.add(map_title);
	}

	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(Game2RecordActivity.this, "���粻��������������",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				if (lastNumber.isEmpty()) {
					last_tv.setVisibility(View.GONE);
				} else {
					last_tv.setVisibility(View.VISIBLE);
				}
				if (nextNumber.isEmpty()) {
					next_tv.setVisibility(View.GONE);
				} else {
					next_tv.setVisibility(View.VISIBLE);
				}
				myCode_tv.setText("�ҵ�Ͷע��" + myCode);
				luckNum_tv.setText("�����н����� �� " + luckCode);
				gla.notifyDataSetChanged();
				break;
			}
		};
	};

	/** ��ʼ������ **/
	private void initDate(final String number) {
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", "���ڲ�ѯ�����Ժ󡣡���");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "requestId",
								"imei", "beforeIssue" },
						new String[] { "10047", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(),
								MyApp.getInstence().getImei(), number });
				String json = HttpTool.httpGetJson1(Game2RecordActivity.this,
						url, hd);
				if (!json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					HandleUtil.post(hd, new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							list.clear();
							listAddTitle();
						}
					});
					JSONArray result = HttpTool.getResult(json);
					try {
						JSONObject object1 = result.getJSONObject(0);
						lastNumber = object1.getString("currentBeforeIssue");
						nextNumber = object1.getString("currentAfterIssue");
						luckCode = object1.getString("luckCode");
						myCode = FormatStringUtil.isEmpty(object1
								.getString("betNumber")) ? "δͶע" : object1
								.getString("betNumber");
						JSONArray array = object1
								.getJSONArray("beforeBetWinList");
						final List<Map<String, String>> temp = new ArrayList<Map<String, String>>();
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = array.getJSONObject(i);
							Map<String, String> map = new HashMap<String, String>();
							map.put("rank", String.valueOf(i + 1));
							map.put("phone", object.getString("userPhone"));
							map.put("code", object.getString("betNumber"));
							map.put("name",
									FormatStringUtil.isEmpty(object
											.getString("name")) ? "����" : object
											.getString("name"));
							map.put("money", FormatStringUtil.getDesplay(object
									.getString("awardMoney")));
							temp.add(map);
						}
						HandleUtil.post(hd, new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								list.addAll(temp);
							}
						});
						HandleUtil.sendInt(hd, 1);
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

	/*** ���ذ�ť���� **/
	public void backClick(View v) {
		this.finish();
		hd = null;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		hd = null;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.textView_last) {
			number_tv.setText("��" + lastNumber + "��");
			initDate(lastNumber);
		} else if (id == R.id.textView_next) {
			number_tv.setText("��" + nextNumber + "��");
			initDate(nextNumber);
		}

	}
}
