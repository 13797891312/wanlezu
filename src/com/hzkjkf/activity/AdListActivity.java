package com.hzkjkf.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.adapter.Collect_gridview_Adapter;
import com.hzkjkf.imageloader.Imageloader;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class AdListActivity extends Activity implements OnItemClickListener {
	private GridView Collect_gridview;
	private List<Map<String, String>> data_list = new ArrayList<Map<String, String>>();
	private Collect_gridview_Adapter adapter;
	private ProgressDialog dialog;
	private String type;
	private int currentPositon = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adlist);
		((TextView) findViewById(R.id.title)).setText(this.getIntent()
				.getStringExtra("title"));
		type = getIntent().getStringExtra("type");
		Collect_gridview = (GridView) findViewById(R.id.collect_gridview);
		initDate();
		adapter = new Collect_gridview_Adapter(this, data_list, hd);
		Collect_gridview.setAdapter(adapter);
		Collect_gridview.setOnItemClickListener(this);
	}

	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(AdListActivity.this, "网络故障，请检查网络连接",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				adapter.notifyDataSetChanged();
				break;
			case 2:

				break;

			}
		};
	};

	/** 初始化数据 **/
	private void initDate() {
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", "正在加载，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "requestId",
								"imei", "typeUids" },
						new String[] { "10061", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(),
								MyApp.getInstence().getImei(), type });
				String json = HttpTool.httpGetJson1(AdListActivity.this, url,
						hd);
				if (!json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					JSONArray result = HttpTool.getResult(json);
					for (int i = 0; i < result.length(); i++) {
						try {
							JSONObject object = result.getJSONObject(i);
							Map<String, String> map = new HashMap<String, String>();
							map.put("thumbnailUrl",
									object.getString("smallImage"));
							map.put("advertisId", object.getString("adUids"));
							map.put("advertisName", object.getString("adName"));
							map.put("bigImage", object.getString("bigImage"));
							map.put("isAnser", "false");
							map.put("intro", object.getString("intro"));
							map.put("couponAble",
									object.getString("couponAble"));
							map.put("shareAble", object.getString("shareAble"));
							data_list.add(map);
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
		hd = null;
		Imageloader.getInstence().clearCache();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, StartEarnActivity.class);
		// intent.putExtra("advertiseId", data_list.get(position)
		// .get("advertisId"));
		intent.putExtra("type", 0);
		intent.putExtra("couponAble",
				Integer.valueOf(data_list.get(position).get("couponAble")));
		intent.putExtra("shareAble",
				Integer.valueOf(data_list.get(position).get("shareAble")));
		intent.putExtra("adUids", data_list.get(position).get("advertisId"));
		intent.putExtra("bigImage", data_list.get(position).get("bigImage"));
		intent.putExtra("smallImage",
				data_list.get(position).get("thumbnailUrl"));
		intent.putExtra("intro", data_list.get(position).get("intro"));
		this.startActivityForResult(intent, 1);
		this.currentPositon = position;
	}

	public void createDialog(final int position) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		builder.setIcon(R.drawable.error);
		builder.setTitle("");
		builder.setMessage("是否删除此收藏？");
		builder.setPositiveButton("取消", null);
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				initDelData(data_list.get(position).get("advertisId"));
				data_list.remove(position);
				MyApp.getInstence().collectCount -= 1;
				adapter.notifyDataSetChanged();
			}
		});
		builder.create().show();
	}

	/** 请求删除URL **/
	private void initDelData(final String advertiseId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "requestId",
								"imei", "advertiseId" },
						new String[] { "10028", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(),
								MyApp.getInstence().getImei(), advertiseId });
				HttpTool.httpGetJson1(AdListActivity.this, url, hd);
			}
		}).start();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		hd = null;
		Imageloader.getInstence().clearCache();
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
		hd = null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null && data.getBooleanExtra("isAnser", false)) {
			data_list.remove(currentPositon);
			adapter.notifyDataSetChanged();
		}

	}
}
