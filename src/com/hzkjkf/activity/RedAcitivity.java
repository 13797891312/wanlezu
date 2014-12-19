package com.hzkjkf.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.util.BitmapUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RedAcitivity extends Activity implements OnItemClickListener {
	private ListView red_listview;
	private ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private ProgressDialog dialog;
	private MyAdapter ma;
	public static Bitmap backBitmap;
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(RedAcitivity.this, "网络不给力，请检查网络",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				ma.notifyDataSetChanged();
				break;
			default:

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
		backBitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.redbanner);
		setContentView(R.layout.activity_red);
		((ImageView) findViewById(R.id.image_baner)).setImageBitmap(backBitmap);
		((TextView) findViewById(R.id.title)).setText("领红包");
		red_listview = (ListView) findViewById(R.id.listview_red);
		ma = new MyAdapter();
		red_listview.setAdapter(ma);
		red_listview.setOnItemClickListener(this);
		initData();
	}

	/** 初始化数据 **/
	private void initData() {
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", "正在加载，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei" }, new String[] {
						"10043", MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei() });
				String json = HttpTool.httpGetJson1(RedAcitivity.this, url, hd);
				if (!json.isEmpty()) {
					JSONArray result = HttpTool.getResult(json);
					for (int i = 0; i < result.length(); i++) {
						try {
							JSONObject object = result.getJSONObject(i);
							Map<String, String> map = new HashMap<String, String>();
							map.put("redPacketName",
									object.getString("redPacketName"));
							map.put("redPacketSize",
									object.getString("redPacketSize"));
							map.put("redPacketCode",
									object.getString("redPacketCode"));
							list.add(map);
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

	public void backClick(View v) {
		hd = null;
		BitmapUtil.recycle(backBitmap);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		hd = null;
		BitmapUtil.recycle(backBitmap);
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

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = View.inflate(RedAcitivity.this,
					R.layout.item_red_listview, null);
			((TextView) convertView.findViewById(R.id.textview_title))
					.setText(list.get(position).get("redPacketName"));
			((TextView) convertView.findViewById(R.id.textView2)).setText(list
					.get(position).get("redPacketSize"));
			((ImageView) convertView.findViewById(R.id.imageView2))
					.setImageResource(R.drawable.hb);
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, NewcomerRedAcitivity.class);
		intent.putExtra("type", list.get(position).get("redPacketCode"));
		intent.putExtra("title", list.get(position).get("redPacketName"));
		this.startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		hd = null;
	}
}
