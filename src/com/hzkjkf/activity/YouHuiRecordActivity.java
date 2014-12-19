package com.hzkjkf.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.adapter.YouHuiRecord_listView;
import com.hzkjkf.javabean.YouHuiData;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class YouHuiRecordActivity extends Activity implements OnClickListener {
	private ListView youhui_lv;
	private ProgressDialog dialog;
	/*** 可使用，已使用，已过期 **/
	private TextView tv1, tv2, tv3;
	/*** 适配器 **/
	private YouHuiRecord_listView yla;
	private List<YouHuiData> list1 = new ArrayList<YouHuiData>();
	private List<YouHuiData> list2 = new ArrayList<YouHuiData>();
	private List<YouHuiData> list3 = new ArrayList<YouHuiData>();
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(YouHuiRecordActivity.this, "网络不给力，请检查网络", 0)
						.show();
				break;
			case 1:
				Toast.makeText(YouHuiRecordActivity.this, "加载成功", 0).show();
				yla.notifyDataSetChanged();
				break;
			default:
				Toast.makeText(YouHuiRecordActivity.this,
						ErrorCode.getString(msg.what), 0).show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_youhuirecord);
		((TextView) findViewById(R.id.title)).setText("优惠券");
		tv1 = (TextView) findViewById(R.id.textView1);
		tv2 = (TextView) findViewById(R.id.textView2);
		tv3 = (TextView) findViewById(R.id.textView3);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		youhui_lv = (ListView) findViewById(R.id.listView_youhui);
		yla = new YouHuiRecord_listView(list1, this);
		youhui_lv.setAdapter(yla);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", "正在加载，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "requestId",
								"imei", "advertiseUids" },
						new String[] { "10044", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(),
								MyApp.getInstence().getImei() });
				String json = HttpTool.httpGetJson1(YouHuiRecordActivity.this,
						url, hd);
				if (!json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					JSONArray result = HttpTool.getResult(json);
					for (int i = 0; i < result.length(); i++) {
						JSONObject object;
						try {
							object = result.getJSONObject(i);
							YouHuiData data = new YouHuiData();
							data.setEndDate(object.getString("LIMIT_DATE"));
							data.setUseTime(object.getString("USE_DATE"));
							data.setInfo(object.getString("COUPON_INFO"));
							data.setName(object.getString("COUPON_NAME"));
							data.setCode(object.getString("COUPON_CODE"));
							data.setState(object.getInt("IS_USED"));
							switch (data.getState()) {
							case 0:
								list1.add(data);
								break;
							case 1:
								list2.add(data);
								break;
							case 2:
								list3.add(data);
								break;
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					HandleUtil.sendInt(hd, 1);
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();
	}

	/*** 返回按钮监听 **/
	public void backClick(View v) {
		this.finish();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.textView1) {
			tv1.setTextColor(Color.RED);
			tv2.setTextColor(Color.GRAY);
			tv3.setTextColor(Color.GRAY);
			yla.changeData(list1);
		} else if (id == R.id.textView2) {
			tv1.setTextColor(Color.GRAY);
			tv2.setTextColor(Color.RED);
			tv3.setTextColor(Color.GRAY);
			yla.changeData(list2);
		} else if (id == R.id.textView3) {
			tv1.setTextColor(Color.GRAY);
			tv2.setTextColor(Color.GRAY);
			tv3.setTextColor(Color.RED);
			yla.changeData(list3);
		}
	}
}
