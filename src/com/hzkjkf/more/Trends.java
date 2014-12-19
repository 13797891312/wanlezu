package com.hzkjkf.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.adapter.Trends_listview_adapter;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/** 玩赚动态 ****/
public class Trends extends Activity implements OnItemClickListener {
	private ListView trends_listview;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private ProgressDialog dialog;
	Trends_listview_adapter tla;
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(Trends.this, "加载失败，请检查网络连接", 1000).show();
				dialog.cancel();
				break;
			case 1:
				if (dialog != null) {
					dialog.cancel();
				}
				tla.notifyDataSetChanged();
				break;
			default:
				Toast.makeText(Trends.this, ErrorCode.getString(msg.what), 1000)
						.show();
				dialog.cancel();
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
		dialog = ProgressDialog.show(this, "", "正在加载");
		setContentView(R.layout.activity_trends);
		((TextView) findViewById(R.id.title)).setText("玩乐族动态");
		trends_listview = (ListView) findViewById(R.id.listview_trends);
		initData();
		tla = new Trends_listview_adapter(this, list);
		trends_listview.setAdapter(tla);
		trends_listview.setOnItemClickListener(this);
	}

	/*** 初始化数据 ***/
	private void initData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei" }, new String[] {
						"10031", MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei() });
				String json = HttpTool.httpGetJson1(Trends.this, url, hd);
				if (!json.isEmpty()) {
					LogUtils.e("flag", String.valueOf(HttpTool.getFlag(json)));
					if (!HttpTool.getFlag(json)) {
						hd.sendEmptyMessage(HttpTool.getErrorCode(json));
						return;
					}
					JSONArray result = HttpTool.getResult(json);
					for (int i = 0; i < result.length(); i++) {
						try {
							JSONObject object = result.getJSONObject(i);
							Map<String, String> map = new HashMap<String, String>();
							map.put("title", object.getString("newTitle"));
							map.put("url", object.getString("newContextUrl"));
							map.put("time", object.getString("data"));
							map.put("from", "玩乐族");
							list.add(map);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					hd.sendEmptyMessage(1);
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, TrendsInfo.class);
		intent.putExtra("url", list.get(position).get("url"));
		intent.putExtra("title", list.get(position).get("title"));
		intent.putExtra("time", list.get(position).get("time"));
		this.startActivity(intent);

	}

	// private void createPop(int position) {
	// // TODO Auto-generated method stub
	// View view=View.inflate(this, R.layout.pop_trends, null);
	// ((TextView)view.findViewById(R.id.textView_title)).setText(list.get(position).get("title"));
	// ((TextView)view.findViewById(R.id.textView_text)).setText(list.get(position).get("text"));
	// ((TextView)view.findViewById(R.id.textView_from)).setText("来自："+list.get(position).get("from"));
	// ((TextView)view.findViewById(R.id.textView_time)).setText("时间："+list.get(position).get("time"));
	// Dialog dialog=new
	// Dialog(this,android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
	// dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
	// dialog.setContentView(view);
	// dialog.show();
	// }

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
