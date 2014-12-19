package com.hzkjkf.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.activity.MainActivity;
import com.hzkjkf.activity.ShopInfoActivity;
import com.hzkjkf.adapter.Home_viewpager_Adapter;
import com.hzkjkf.adapter.Shop_listview;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.view.Head;
import com.hzkjkf.view.HeadListoner;
import com.hzkjkf.view.MyListView;
import com.hzkjkf.view.Myview;
import com.hzkjkf.wanzhuan.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShopFragment extends BaseFragment implements Runnable,
		OnPageChangeListener, OnItemClickListener {	
	private boolean isHasBannerData = false;
	private MyListView shop_listview;
	public List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();
	public List<Map<String, String>> list2 = new ArrayList<Map<String, String>>();
	public List<Map<String, String>> list3 = new ArrayList<Map<String, String>>();
	public List<Map<String, String>> list4 = new ArrayList<Map<String, String>>();
	public List<Map<String, String>> list_sum = new ArrayList<Map<String, String>>();
	/** 当前viewpager显示第几页 ***/
	private int currentItem = 0;
	/*** 放小圆点view的集合 ****/
	private List<View> list_image = new ArrayList<View>();
	/** 放小圆点标签的布局 **/
	private LinearLayout layout;
	private Handler hd;
	private ViewPager home_viewpager;
	private List<Bitmap> list_vp = new ArrayList<Bitmap>();
	private boolean isLoading = false;
	private Home_viewpager_Adapter mpa;
	public List<Map<String, String>> bannerList = new ArrayList<Map<String, String>>();
	private MainActivity mAcitivity;
	private Shop_listview sla;

	public ShopFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mAcitivity = (MainActivity) this.getActivity();
		hd = ((MainActivity) this.getActivity()).hd;
		View view = inflater.inflate(R.layout.fragment_shop, container, false);
		home_viewpager = (ViewPager) view.findViewById(R.id.viewpager_shop);
		home_viewpager.setOnPageChangeListener(this);
		layout = (LinearLayout) view.findViewById(R.id.linearlayout);// 添加小圆点标签
		shop_listview = (MyListView) view.findViewById(R.id.listView_shop);
		shop_listview.SetOnHeadRefreshListener(new HeadListoner() {
			@Override
			public void headRefresh() {
				// TODO Auto-generated method stub
				initData();
			}
		});
		sla = new Shop_listview(hd, this.getActivity(), list1, list2, list3,list4,
				list_sum);
		shop_listview.setAdapter(sla);
		shop_listview.setOnItemClickListener(this);
		return view;
	}

	/** 更新视图 ***/
	@Override
	public void initView() {
		sla.notifyDataSetChanged();
		if (!isHasBannerData) {
			getBannerJson();
			isHasBannerData = true;
		}

	}

	/*** 初始化小圆点标签 **/
	private void initLayout() {
		LayoutParams params = new LayoutParams(16, 16);
		for (int i = 0; i < list_vp.size(); i++) {
			Myview view1 = new Myview(this.getActivity(), Color.WHITE, 2);
			if (i == 0) {
				view1.setColor(0xff18A614);
			}
			view1.setLayoutParams(params);
			list_image.add(view1);
			layout.addView(view1);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int count = home_viewpager.getCurrentItem();
		((Myview) list_image.get(currentItem)).setColor(Color.WHITE);
		home_viewpager.setCurrentItem(++count);
		currentItem = home_viewpager.getCurrentItem() % list_vp.size();
		((Myview) list_image.get(currentItem)).setColor(0xff18A614);
		HandleUtil.postDelayed(hd, this, 5000);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		HandleUtil.removeCallbacks(hd, this);
		for (int i = 0; i < list_vp.size(); i++) {
			if (list_vp.get(i) != null) {
				list_vp.get(i).recycle();
			}
		}
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
	public boolean isIsloading() {
		return isLoading;
	}

	@Override
	public void setIsloading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public boolean isIsload() {
		// TODO Auto-generated method stub
		return isLoad;
	}

	@Override
	public void setIsIsload(boolean isLoad) {
		// TODO Auto-generated method stub
		this.isLoad = isLoad;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mpa != null) {
			mpa.imageloader.clearCache();
		}
	}

	/*** 获取banner信息 **/
	private void getBannerJson() {
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
								MyApp.getInstence().getImei(), "2" });
				String json = HttpTool.httpGetJson1(mAcitivity, url, hd);
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
							mpa = new Home_viewpager_Adapter(mAcitivity,
									bannerList, hd);
							home_viewpager.setAdapter(mpa);
							if (bannerList.size() > 1) {
								initLayout();
								HandleUtil.postDelayed(hd, ShopFragment.this,
										5000);
							}
						}
					});

				}
			}
		}).start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		position -= 1;
		// TODO Auto-generated method stub
		Intent intent = new Intent(this.getActivity(), ShopInfoActivity.class);
		intent.putExtra("picUrl", list_sum.get(position).get("picUrl"));
		intent.putExtra("kcNum", list_sum.get(position).get("kcNum"));
		intent.putExtra("costs", list_sum.get(position).get("costs"));
		intent.putExtra("goodName", list_sum.get(position).get("goodName"));
		intent.putExtra("goodPrice", list_sum.get(position).get("goodPrice"));
		intent.putExtra("goodType", list_sum.get(position).get("goodType"));
		intent.putExtra("memo", list_sum.get(position).get("memo"));
		intent.putExtra("uids", list_sum.get(position).get("uids"));
		intent.putExtra("flowsign", list_sum.get(position).get("flowsign"));
		intent.putExtra("exchangeState",
				list_sum.get(position).get("exchangeState"));
		this.getActivity().startActivity(intent);
	}

	/*** 下拉刷新数据 ****/
	private void initData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei" }, new String[] {
						"10048", MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei() });
				String json = HttpTool.httpGetJson1(
						ShopFragment.this.getActivity(), url, hd);
				if (json.isEmpty()) {
					HandleUtil.sendInt(hd, 0);
					HandleUtil.post(hd, new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							shop_listview.head.logic(Head.NO);
						}
					});
					return;
				} else {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					readShopJson(json);
				}
			}
		}).start();
	}

	/**** 解析商城碎片JSON ****/
	private void readShopJson(String json) {
		list1.clear();
		list2.clear();
		list3.clear();
		list4.clear();
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
					list1.add(map);
				} else if (map.get("goodType").equals("AUCTION")) {
					list2.add(map);
				} else if (map.get("goodType").equals("ACTIVITY")) {
					list3.add(map);
				} else if (map.get("goodType").equals("LLBCP")) {
					list4.add(map);
				}
			}
			HandleUtil.post(hd, new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					list_sum.clear();
					list_sum.addAll(list1);
					list_sum.addAll(list4);
					list_sum.addAll(list2);
					list_sum.addAll(list3);
					sla.notifyDataSetChanged();
					shop_listview.head.logic(Head.OK);
				}
			});
			return;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		HandleUtil.post(hd, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				shop_listview.head.logic(Head.NO);
			}
		});
	}
}
