package com.hzkjkf.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.adapter.Home_viewpager_Adapter;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.view.Myview;
import com.hzkjkf.wanzhuan.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AdvertActivity extends Activity implements OnPageChangeListener,
		OnItemClickListener, Runnable {
	/** 当前viewpager显示第几页 ***/
	private int currentItem = 0;
	/*** 放小圆点view的集合 ****/
	private List<View> list_image = new ArrayList<View>();
	/** 放小圆点标签的布局 **/
	private LinearLayout layout;
	/**** 放gridview的item项的集合 *****/
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private ViewPager advert_viewpager;
	private GridView advert_gridview;
	private List<Bitmap> list_vp = new ArrayList<Bitmap>();
	public List<Map<String, String>> bannerList = new ArrayList<Map<String, String>>();
	private myAdapter adapter;
	private Home_viewpager_Adapter mpa;
	private Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(AdvertActivity.this, "网络故障，请检查网络",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		};
	};
	final String str[] = { "地产园林", "服装饰品", "家居建材", "教育科技", "运动健身", "旅游酒店",
			"美容护肤", "汽车机械", "餐饮娱乐", "日用百货", "生活服务", "食品保健", "数码家电", "医疗卫生",
			"金融保险", "公益广告" };
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_advert);
		((TextView) findViewById(R.id.title)).setText("看广告");
		advert_viewpager = (ViewPager) findViewById(R.id.viewpager_advert);
		advert_viewpager.setOnPageChangeListener(this);
		layout = (LinearLayout) findViewById(R.id.linearlayout);// 添加小圆点标签
		advert_gridview = (GridView) findViewById(R.id.gridview_advert);
		advert_gridview.setOnItemClickListener(this);
		adapter = new myAdapter();
		advert_gridview.setAdapter(adapter);
		getBannerJson();
		initData();
	}

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
						"10060", MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei() });
				String json = HttpTool.httpGetJson1(AdvertActivity.this, url,
						hd);
				if (!json.isEmpty()) {
					JSONArray result = HttpTool.getResult(json);
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					for (int i = 0; i < result.length(); i++) {
						try {
							JSONObject object = result.getJSONObject(i);
							Map<String, String> map = new HashMap<String, String>();
							map.put("UIDS", object.getString("UIDS"));
							map.put("PROP_NAME", object.getString("PROP_NAME"));
							map.put("PROP_CODE", object.getString("PROP_CODE"));
							map.put("NUM", object.getString("NUM"));
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
		this.finish();
	}

	/*** 初始化小圆点标签 **/
	private void initLayout() {
		LayoutParams params = new LayoutParams(16, 16);
		for (int i = 0; i < list_vp.size(); i++) {
			Myview view1 = new Myview(this, Color.WHITE, 2);
			if (i == 0) {
				view1.setColor(0xff18A614);
			}
			view1.setLayoutParams(params);
			list_image.add(view1);
			layout.addView(view1);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int count = advert_viewpager.getCurrentItem();
		((Myview) list_image.get(currentItem)).setColor(Color.WHITE);
		advert_viewpager.setCurrentItem(++count);
		currentItem = advert_viewpager.getCurrentItem() % list_vp.size();
		((Myview) list_image.get(currentItem)).setColor(0xff18A614);
		HandleUtil.postDelayed(hd, this, 5000);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		HandleUtil.removeCallbacks(hd, this);
		if (mpa != null) {
			mpa.imageloader.clearCache();
		}
		for (int i = 0; i < list_vp.size(); i++) {
			if (list_vp.get(i) != null) {
				list_vp.get(i).recycle();
			}
		}
		hd = null;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(final int arg0) {
		// TODO Auto-generated method stub
		if (list_image.isEmpty()) {
			return;
		}
		((Myview) list_image.get(currentItem)).setColor(Color.WHITE);
		currentItem = arg0 % list_vp.size();
		((Myview) list_image.get(currentItem)).setColor(0xff18A614);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		this.startActivity(new Intent(this, AdListActivity.class).putExtra(
				"type", list.get(position).get("UIDS")).putExtra("title",
				list.get(position).get("PROP_NAME")));
	}

	class myAdapter extends BaseAdapter {
		final int id[] = { R.drawable._dcyl, R.drawable._fzsp,
				R.drawable._jjjc, R.drawable._jykj, R.drawable._ydjs,
				R.drawable._nyjd, R.drawable._mrhf, R.drawable._qcjx,
				R.drawable._cyyl, R.drawable._rybh, R.drawable._shfw,
				R.drawable._spbj, R.drawable._smjd, R.drawable._ylws,
				R.drawable._jrbx, R.drawable._gygg };

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
			if (convertView == null) {
				convertView = View.inflate(AdvertActivity.this,
						R.layout.item_advert_gridview, null);
			}
			TextView tv_num = (TextView) convertView
					.findViewById(R.id.textView_num);
			if (list.get(position).get("NUM").equals("0")) {
				tv_num.setVisibility(View.GONE);
			} else {
				tv_num.setText(list.get(position).get("NUM"));
			}
			((TextView) convertView.findViewById(R.id.textView1)).setText(list
					.get(position).get("PROP_NAME"));
			if (position <= id.length - 1) {
				((ImageView) convertView.findViewById(R.id.imageView1))
						.setImageResource(id[position]);
			} else {
				((ImageView) convertView.findViewById(R.id.imageView1))
						.setVisibility(View.GONE);
			}
			return convertView;
		}
	}

	/*** 获取banner信息 **/
	public void getBannerJson() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "requestId",
								"imei", "imageType" },
						new String[] { "10033", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(),
								MyApp.getInstence().getImei(), "3" });
				String json = HttpTool.httpGetJson1(AdvertActivity.this, url,
						hd);
				if (json.isEmpty()) {
					HandleUtil.sendInt(hd, 0);
					return;
				} else {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					JSONArray array = HttpTool.getResult(json);
					for (int i = 0; i < array.length(); i++) {
						list_vp.add(null);
						Map<String, String> map = new HashMap<String, String>();
						try {
							map.put("title",
									array.getJSONObject(i).getString("title"));
							map.put("imageUrl", array.getJSONObject(i)
									.getString("imageUrl"));
							map.put("url",
									array.getJSONObject(i).getString(
											"redictUrl"));
							bannerList.add(map);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					HandleUtil.post(hd, new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mpa = new Home_viewpager_Adapter(
									AdvertActivity.this, bannerList, hd);
							advert_viewpager.setAdapter(mpa);
							if (bannerList.size() > 1) {
								initLayout();
								HandleUtil.postDelayed(hd, AdvertActivity.this,
										5000);
							}
						}
					});
				}
			}
		}).start();
	}
}
