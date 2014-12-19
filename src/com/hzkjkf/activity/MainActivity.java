package com.hzkjkf.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hzkjkf.activity.baidu.BaiduMapActivity;
import com.hzkjkf.fragment.BaseFragment;
import com.hzkjkf.fragment.MyWanzhuanFragment;
import com.hzkjkf.fragment.HomeFragment;
import com.hzkjkf.fragment.MoreFragment;
import com.hzkjkf.fragment.ShopFragment;
import com.hzkjkf.location.MyLocation;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.util.MySqliteHelper;
import com.hzkjkf.util.SqlInfo;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.ALIAS_TYPE;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.proguard.I.e;

public class MainActivity extends FragmentActivity {
	public static int screenX, screenY;
	public static float scale;
	private GridView tabLayout;
	public TextView title_textview, city_tv;
	public static final String title[] = { "首页", "商城", "我的", "更多" };
	String text[] = { "首页", "玩币商城", "我的", "更多" };
	private List<Fragment> fragmentList = new ArrayList<Fragment>();
	private List<Map<String, Object>> tabList = new ArrayList<Map<String, Object>>();
	private FragmentManager manager;
	private int image[] = { R.drawable.shouye_press, R.drawable.tuijian,
			R.drawable.youxi, R.drawable.gengduo };
	private int selectImage[] = { R.drawable.shouye_press,
			R.drawable.tuijian_press, R.drawable.youxi_press,
			R.drawable.gengduo_press };
	private BaseFragment fragment;
	private ViewStub loading;
	/** 当前显示碎片设置标签，防止当前页面加载过程中切换到其他页面时初始化出问题 ***/
	private String tag;
	private SimpleAdapter tabAdapter;
	private MySqliteHelper sql;
	private TextView news_tv;
	private int news_count;
	private RelativeLayout city_rel, back_rel;
	private MyLocation location;
	/**友盟推送**/
	PushAgent mPushAgent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		//测试开关
		mPushAgent.setDebugMode(false);
		//友盟推送添加ID;
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mPushAgent.addAlias(MyApp.getInstence().getPhone(), ALIAS_TYPE.QQ);
				} catch (e e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		manager = this.getSupportFragmentManager();
		sql = new MySqliteHelper(this);
		scale = this.getResources().getDisplayMetrics().density;
		// 获取屏幕宽
		screenX = this.getResources().getDisplayMetrics().widthPixels;
		// 获取屏幕高
		screenY = this.getResources().getDisplayMetrics().heightPixels;
		tabLayout = (GridView) findViewById(R.id.tab_gridview);
		news_tv = (TextView) findViewById(R.id.textView_news);
		title_textview = (TextView) findViewById(R.id.title_textview);
		city_tv = (TextView) findViewById(R.id.city_textView);
		city_rel = (RelativeLayout) findViewById(R.id.city_layout);
		back_rel = (RelativeLayout) findViewById(R.id.back_layout);
		loading = (ViewStub) findViewById(R.id.loading);
		loading.setVisibility(View.GONE);
		location = new MyLocation(this.getApplicationContext(), hd);
		location.startLocation();
		if (savedInstanceState != null) {
			LogUtils.e("回收后再次进入", "回收后再次进入");
			initSave(savedInstanceState);
			RelativeLayout rel = (RelativeLayout) findViewById(R.id.fragmentLayout);
			rel.removeAllViews();
			fragmentList = manager.getFragments();
			FragmentTransaction ft = manager.beginTransaction();
			ft.hide(fragmentList.get(1));
			ft.hide(fragmentList.get(2));
			ft.hide(fragmentList.get(3));
			ft.commit();
			loading.setVisibility(View.GONE);
		} else {
			initFragment();// 初始化碎片
		}
		new Thread(new LoadingTask(0)).start();// 加载首页碎片数据
		initTab();// 初始化底部导航标签
	}

	/*** 初始化碎片 **/
	private void initFragment() {
		// TODO Auto-generated method stub
		FragmentTransaction ft = manager.beginTransaction();
		fragmentList.add(new HomeFragment());
		fragmentList.add(new ShopFragment());
		fragmentList.add(new MyWanzhuanFragment());
		fragmentList.add(new MoreFragment());
		for (int i = 0; i < fragmentList.size(); i++) {
			ft.add(R.id.fragmentLayout, fragmentList.get(i), title[i]);
			if (i == 0) {
				fragment = (BaseFragment) fragmentList.get(i);
				ft.show(fragment);
				tag = fragment.getTag();
			} else {
				ft.hide(fragmentList.get(i));
			}
		}
		ft.commit();
	}

	/** 初始化底部导航标签 **/
	private void initTab() {
		// TODO Auto-generated method stub
		((ImageButton) findViewById(R.id.news_btn))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MainActivity.this,
								NewsActivity.class);
						MainActivity.this.startActivityForResult(intent, 1);
					}
				});

		for (int i = 0; i < image.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", title[i]);
			map.put("image", image[i]);
			map.put("text", text[i]);
			tabList.add(map);
		}
		tabAdapter = new SimpleAdapter(this, tabList,
				R.layout.item_main_tabhost, new String[] { "title", "image" },
				new int[] { R.id.textView1, R.id.imageView1 });
		tabLayout.setAdapter(tabAdapter);
		tabLayout.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				image[0] = R.drawable.shouye;
				FragmentTransaction ft = manager.beginTransaction();
				for (int i = 0; i < title.length; i++) {
					if (position == i) {
						fragment = (BaseFragment) fragmentList.get(i);
						// ???????????
						((ImageView) view.findViewById(R.id.imageView1))
								.setImageResource(selectImage[i]);
						if (position == 0) {
							city_rel.setVisibility(View.VISIBLE);
							back_rel.setVisibility(View.GONE);
						} else {
							city_rel.setVisibility(View.GONE);
							back_rel.setVisibility(View.VISIBLE);
						}
						title_textview.setText(text[i]);
					} else {
						ft.hide(fragmentList.get(i));
						((ImageView) parent.getChildAt(i).findViewById(
								R.id.imageView1)).setImageResource(image[i]);
					}
				}
				tag = fragment.getTag();
				ft.show(fragment);
				ft.commitAllowingStateLoss();
				fragment.initView();
				if (!fragment.isIsloading() && !fragment.isIsload()) {
					fragment.setIsIsload(true);
					new Thread(new LoadingTask(position)).start();
					loading.setVisibility(View.GONE);
				} else {
					/*** ??????????????С??? ***/
					HandleUtil.postDelayed(hd, new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							loading.setVisibility(View.GONE);
						}
					}, 50);
				}
			}
		});
	}

	public Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0 && msg.arg1 != 1) {
				Toast.makeText(MainActivity.this, "网络故障 请检查网络",
						Toast.LENGTH_SHORT).show();
			} else if (msg.arg1 == 1) {// 加载数据成功
				((BaseFragment) msg.obj).initView();// 初始化碎片控件
				((BaseFragment) msg.obj).setIsloading(true);
				if (((BaseFragment) msg.obj).getTag().equals(tag)) {// 判断当前显示碎片是否是加载数据的碎片
					loading.setVisibility(View.GONE);
				}
			} else if (msg.what == 2) {

			} else if (msg.what == 5) {// 没有更多推荐记录
				Toast.makeText(MainActivity.this, "没有更多记录", Toast.LENGTH_SHORT)
						.show();
			} else if (msg.what == 6) {// 没有更多推荐记录
				if (FormatStringUtil.isEmpty(MyApp.getInstence().city)) {
					city_tv.setText("定位失败");
				} else {
					city_tv.setText(MyApp.getInstence().city);
				}
			} else {
				Toast.makeText(MainActivity.this,
						ErrorCode.getString(msg.what), Toast.LENGTH_SHORT)
						.show();
			}
		}

	};

	/*** 更具type加载不同碎片的信数据 ***/
	class LoadingTask implements Runnable {
		private int type = 0;

		public LoadingTask(int type) {
			// TODO Auto-generated constructor stub
			this.type = type;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String url;
			String json = "";
			switch (type) {
			case 0:// 首页数据
				url = HttpTool.getUrl(new String[] { "classId", "phoneNumber",
						"requestId", "imei" }, new String[] { "10039",
						MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei() });
				json = HttpTool.httpGetJson1(MainActivity.this, url, hd);
				if (json.isEmpty()) {
					HandleUtil.sendInt(hd, 0);
					return;
				} else {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					readHomeJson(json);
				}
				break;
			case 1:// 商城页碎片
				url = HttpTool.getUrl(new String[] { "classId", "phoneNumber",
						"requestId", "imei" }, new String[] { "10048",
						MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei() });
				json = HttpTool.httpGetJson1(MainActivity.this, url, hd);
				if (json.isEmpty()) {
					HandleUtil.sendInt(hd, 0);
					return;
				} else {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					readShopJson(json);
				}
				break;
			}
			// ((BaseFragment) fragmentList.get(type)).setIsloading(false);//
			// 标记碎片加载完毕
		}
	}

	/**** 解析首页碎片JSON ****/
	private void readHomeJson(String json) {
		try {
			JSONArray result = new JSONArray(json);
			JSONArray object = result.getJSONObject(0).getJSONArray("result");
			for (int j = 0; j < object.length(); j++) {
				JSONObject jsonObject = object.getJSONObject(j);
				// ((HomeFragment)
				// fragmentList.get(0)).getHomeMap().put(keys[j],
				// jsonObject.getString("count"));
				if (j == 0) {
					MyApp.getInstence().setHomeData(0,
							jsonObject.getString("count"));
				} else if (j == 1) {
					MyApp.getInstence().setHomeData(1,
							jsonObject.getString("count"));
				} else if (j == 2) {
					MyApp.getInstence().setHomeData(2, "奖励");
					MyApp.getInstence().setHomeData(3, "任务");
					MyApp.getInstence().setHomeData(4, "玩币");
				} else if (j == 3) {
					MyApp.getInstence().setHomeData(j + 2,
							jsonObject.getString("count"));
				} else if (j == 6) {
					news_count = jsonObject.getInt("count");
				} else if (j == 4) {
					MyApp.getInstence().setHomeData(j + 2,
							jsonObject.getString("count"));
				} else if (j == 5) {
					MyApp.getInstence().setHomeData(j + 2,
							jsonObject.getString("count"));
				}
				
			}
			HandleUtil.post(hd, new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					initNewsData();
				}
			});
			/*** 更新百度地图位置 ****/
			HandleUtil.postDelayed(hd, new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					// creatPoi();
					// hasPoi();
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							upDataPoi();
						}
					}).start();
				}
			}, 10000);
			if (hd != null) {
				Message msg = hd.obtainMessage();
				msg.obj = fragmentList.get(0);
				msg.arg1 = 1;
				hd.sendMessage(msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**** 解析商城碎片JSON ****/
	private void readShopJson(String json) {
		try {
			JSONArray result = new JSONArray(json);
			JSONArray object = result.getJSONObject(0).getJSONArray("result");
			for (int j = 0; j < object.length(); j++) {
				JSONObject jsonObject = object.getJSONObject(j);
				JSONArray keys = jsonObject.names();
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < keys.length(); i++) {
					map.put(keys.getString(i),
							jsonObject.getString(keys.getString(i)));
				}
				if (map.get("goodType").equals("TX")) {
					((ShopFragment) fragmentList.get(1)).list1.add(map);
				} else if (map.get("goodType").equals("AUCTION")) {
					((ShopFragment) fragmentList.get(1)).list2.add(map);
				} else if (map.get("goodType").equals("ACTIVITY")) {
					((ShopFragment) fragmentList.get(1)).list3.add(map);
				} else if (map.get("goodType").equals("LLBCP")) {
					((ShopFragment) fragmentList.get(1)).list4.add(map);
				}
			}
			((ShopFragment) fragmentList.get(1)).list_sum
					.addAll(((ShopFragment) fragmentList.get(1)).list1);
			((ShopFragment) fragmentList.get(1)).list_sum
					.addAll(((ShopFragment) fragmentList.get(1)).list4);
			((ShopFragment) fragmentList.get(1)).list_sum
					.addAll(((ShopFragment) fragmentList.get(1)).list2);
			((ShopFragment) fragmentList.get(1)).list_sum
					.addAll(((ShopFragment) fragmentList.get(1)).list3);
			if (hd != null) {
				Message msg = hd.obtainMessage();
				msg.obj = fragmentList.get(1);
				msg.arg1 = 1;
				hd.sendMessage(msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/*** 初始化消息有多少条未读 ***/
	private void initNewsData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Cursor cursor = sql.getInfoCursor(SqlInfo.ISREADED + "=? and "
						+ SqlInfo.PHONE + "=?", new String[] { "false",
						MyApp.getInstence().getPhone() });
				final int count = cursor.getCount() + news_count;
				HandleUtil.post(hd, new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (count > 0) {
							news_tv.setVisibility(View.VISIBLE);
							news_tv.setText(String.valueOf(count));
						} else {
							news_tv.setVisibility(View.GONE);
						}
					}
				});
				cursor.close();
			}
		}).start();

	};

	/*** 询问是否退出程序 ***/
	@Override
	public void onBackPressed() {
		createExitPop();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 1) {
			Cursor cursor = sql.getInfoCursor(SqlInfo.ISREADED + "=? and "
					+ SqlInfo.PHONE + "=?", new String[] { "false",
					MyApp.getInstence().getPhone() });
			int count = cursor.getCount();
			if (count > 0) {
				news_tv.setVisibility(View.VISIBLE);
				news_tv.setText(String.valueOf(count));
			} else {
				news_tv.setVisibility(View.GONE);
			}
			cursor.close();
		}
		System.gc();
	}

	private void createExitPop() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(this, R.style.MyDialogStyle);
		dialog.setCanceledOnTouchOutside(true);
		View view = View.inflate(this, R.layout.pop_exit, null);
		Button dismiss_btn = (Button) view.findViewById(R.id.button_cancel);
		Button exit_btn = (Button) view.findViewById(R.id.button_exit);
		dismiss_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		exit_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (MainActivity.this != null) {
					MainActivity.this.finish();
				}
				System.exit(0);
			}
		});
		dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		dialog.setContentView(view);
		dialog.show();
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		hd = null;
	}

	@Override
	protected void onRestoreInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(outState);
		LogUtils.e("程序被回收", "程序被回收");
		outState.putFloat("surplus", MyApp.getInstence().getSurplusFloat());
		outState.putInt("remainAd", MyApp.getInstence().remainAd);
		outState.putInt("remainAdcurrent", MyApp.getInstence().remainAdcurrent);
		outState.putInt("collectCount", MyApp.getInstence().collectCount);
		outState.putInt("lv", MyApp.getInstence().lv);
		outState.putString("token", MyApp.getInstence().getToken());
		outState.putString("phone", MyApp.getInstence().getPhone());
		outState.putString("imei", MyApp.getInstence().getImei());
		outState.putString("InviteCode", MyApp.getInstence().InviteCode);
		outState.putString("edu", MyApp.getInstence().edu);
		outState.putString("aliCount", MyApp.getInstence().aliCount);
		outState.putString("QQ", MyApp.getInstence().QQ);
		outState.putString("birthday", MyApp.getInstence().birthday);
		outState.putString("expName", MyApp.getInstence().expName);
		outState.putString("expPosition", MyApp.getInstence().expPosition);
		outState.putString("sex", MyApp.getInstence().sex);
		outState.putString("profession", MyApp.getInstence().profession);
		outState.putString("email", MyApp.getInstence().email);
	}

	private void initSave(Bundle savedInstanceState) {
		MyApp.getInstence().setSurplus(savedInstanceState.getFloat("surplus"));
		MyApp.getInstence().remainAd = savedInstanceState.getInt("remainAd");
		MyApp.getInstence().remainAdcurrent = savedInstanceState
				.getInt("remainAdcurrent");
		MyApp.getInstence().collectCount = savedInstanceState
				.getInt("collectCount");
		MyApp.getInstence().lv = savedInstanceState.getInt("lv");
		MyApp.getInstence().setToken(savedInstanceState.getString("token"));
		MyApp.getInstence().setPhone(savedInstanceState.getString("phone"));
		MyApp.getInstence().setImei(savedInstanceState.getString("imei"));
		MyApp.getInstence().InviteCode = savedInstanceState
				.getString("InviteCode");
		MyApp.getInstence().edu = savedInstanceState.getString("edu");
		MyApp.getInstence().aliCount = savedInstanceState.getString("aliCount");
		MyApp.getInstence().QQ = savedInstanceState.getString("QQ");
		MyApp.getInstence().birthday = savedInstanceState.getString("birthday");
		MyApp.getInstence().expName = savedInstanceState.getString("expName");
		MyApp.getInstence().expPosition = savedInstanceState
				.getString("expPosition");
		MyApp.getInstence().sex = savedInstanceState.getString("sex");
		MyApp.getInstence().profession = savedInstanceState
				.getString("profession");
		MyApp.getInstence().email = savedInstanceState.getString("email");
	}

	public void mapClick(View v) {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, BaiduMapActivity.class));
	}

	public void backClick(View v) {
		// TODO Auto-generated method stub
		FragmentTransaction ft = manager.beginTransaction();
		for (int i = 0; i < fragmentList.size(); i++) {
			if (i == 0) {
				ft.show(fragmentList.get(0));
				((ImageView) tabLayout.getChildAt(0).findViewById(
						R.id.imageView1)).setImageResource(selectImage[0]);
			} else {
				ft.hide(fragmentList.get(i));
				((ImageView) tabLayout.getChildAt(i).findViewById(
						R.id.imageView1)).setImageResource(image[i]);
			}
		}
		ft.commit();
		fragment = (BaseFragment) fragmentList.get(0);
		// TODO Auto-generated method stub
		city_rel.setVisibility(View.VISIBLE);
		back_rel.setVisibility(View.GONE);
		title_textview.setText(text[0]);
		tag = fragment.getTag();
		fragment.initView();
		if (!fragment.isIsloading() && !fragment.isIsload()) {
			fragment.setIsIsload(true);
			new Thread(new LoadingTask(0)).start();
			loading.setVisibility(View.GONE);
		} else {
			/*** ??????????????С??? ***/
			HandleUtil.postDelayed(hd, new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					loading.setVisibility(View.GONE);
				}
			}, 50);
		}

	}

	/*** 修改百度数据 **/
	private void upDataPoi() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		String key[] = { "phone", "latitude", "longitude", "coord_type",
				"geotable_id", "address", "ak", "name", "money", "lv" };
		String value[] = {
				MyApp.getInstence().getPhone(),
				String.valueOf(MyApp.getInstence().latitude),
				String.valueOf(MyApp.getInstence().longitude),
				"3",
				"77838",
				MyApp.getInstence().addrStr,
				"AO6OFZrtV9jWhafBxIpKZCRI",
				MyApp.getInstence().name,
				FormatStringUtil
						.getDesplay(String.valueOf(MyApp.getInstence().maxMoney)),
				String.valueOf(MyApp.getInstence().lv) };
		for (int i = 0; i < key.length; i++) {
			sb.append(key[i] + "=");
			sb.append(value[i] + "&");
		}
		// catch block
		sb.delete(sb.length() - 1, sb.length());
		byte data[] = sb.toString().getBytes();
		LogUtils.e("上传百度LBS请求", sb.toString());
		try {
			JSONObject json = new JSONObject(HttpTool.httpGetJson2(
					MainActivity.this, MyApp.getInstence().baiduUpdate, hd,
					data));
			if (json.getInt("status") == 3003) {
				creatPoi();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*** 创建新的百度数据 ***/
	private void creatPoi() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				StringBuffer sb = new StringBuffer();
				String key[] = { "title", "latitude", "longitude",
						"coord_type", "geotable_id", "address", "ak", "name",
						"money", "phone", "lv" };
				String value[] = {
						MyApp.getInstence().getPhone(),
						String.valueOf(MyApp.getInstence().latitude),
						String.valueOf(MyApp.getInstence().longitude),
						"3",
						"77838",
						MyApp.getInstence().addrStr,
						"AO6OFZrtV9jWhafBxIpKZCRI",
						MyApp.getInstence().name,
						FormatStringUtil.getDesplay(String.valueOf(MyApp
								.getInstence().maxMoney)),
						MyApp.getInstence().getPhone(),
						String.valueOf(MyApp.getInstence().lv) };
				for (int i = 0; i < key.length; i++) {
					sb.append(key[i] + "=");
					sb.append(value[i] + "&");
				}
				// catch block
				sb.delete(sb.length() - 1, sb.length());
				byte data[] = sb.toString().getBytes();
				LogUtils.e("上传百度LBS请求", sb.toString());
				HttpTool.httpGetJson2(MainActivity.this,
						MyApp.getInstence().bauduAddress, hd, data);
			}
		}).start();
	}
}
