package com.hzkjkf.fragment;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.advertwall.sdk.activity.TaskWallActivity;
import com.advertwall.sdk.util.OffWowContants;
import com.hzkjkf.activity.AdvertActivity;
import com.hzkjkf.activity.CollectActivity;
import com.hzkjkf.activity.DownLoadTaskAcitivity;
import com.hzkjkf.activity.GameAcitivity;
import com.hzkjkf.activity.MainActivity;
import com.hzkjkf.activity.MoreTaskActivity;
import com.hzkjkf.activity.RedAcitivity;
import com.hzkjkf.activity.ShareAcitivity;
import com.hzkjkf.activity.SignAcitivity;
import com.hzkjkf.activity.StartEarnActivity;
import com.hzkjkf.activity.UserInfoAcitivity;
import com.hzkjkf.adapter.Home_gridview_adapter;
import com.hzkjkf.adapter.Home_viewpager_Adapter;
import com.hzkjkf.adtask.QuestionTaskActivity;
import com.hzkjkf.update.DownLoadTask;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.util.MyApp.HomeChengedListener;
import com.hzkjkf.view.Myview;
import com.hzkjkf.wanzhuan.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HomeFragment extends BaseFragment implements Runnable,
		OnPageChangeListener, OnItemClickListener {
	/** 当前viewpager显示第几页 ***/
	private int currentItem = 0;
	/*** 放小圆点view的集合 ****/
	private List<View> list_image = new ArrayList<View>();
	/** 放小圆点标签的布局 **/
	private LinearLayout layout;
	private Handler hd;
	/**** 放gridview的item项的集合 *****/
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private ViewPager home_viewpager;
	private GridView home_gridview;
	private List<Bitmap> list_vp = new ArrayList<Bitmap>();
	private boolean isLoading = false;
	private Map<String, String> dataMap = new HashMap<String, String>();
	private Home_gridview_adapter adapter;
	private Home_viewpager_Adapter mpa;
	public List<Map<String, String>> bannerList = new ArrayList<Map<String, String>>();
	private boolean isInit;

	public HomeFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		hd = ((MainActivity) this.getActivity()).hd;
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		home_viewpager = (ViewPager) view.findViewById(R.id.viewpager_home);
		home_viewpager.setOnPageChangeListener(this);
		layout = (LinearLayout) view.findViewById(R.id.linearlayout);// 添加小圆点标签
		home_gridview = (GridView) view.findViewById(R.id.gridview_home);
		home_gridview.setOnItemClickListener(this);
		adapter = new Home_gridview_adapter(this.getActivity());
		home_gridview.setAdapter(adapter);
		MyApp.getInstence().setOnHomeCheanged(new HomeChengedListener() {
			@Override
			public void homeChenged() {
				// TODO Auto-generated method stub
				HandleUtil.post(hd, new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter.notifyDataSetChanged();
					}
				});
			}
		});
		getBannerJson();
		return view;
	}

	/** 更新视图 ***/
	@Override
	public void initView() {

		// String str2[] = { "剩余" + MyApp.getInstence().getSurplus()+"玩币",
		// "赚取" + FormatStringUtil.getDesplay(dataMap.get("makeMoney"))+"玩币",
		// "共提现￥" + dataMap.get("withdrawAmount"),
		// "可看图" + MyApp.getInstence().remainAd + "张",
		// "已收藏" + MyApp.getInstence().collectCount + "张", "" };
		// for (int i = 0; i < list.size(); i++) {
		// list.get(i).put("text2", str2[i]);
		// }

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
		HandleUtil.removeCallbacks(hd, this);
		HandleUtil.postDelayed(hd, this, 5000);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (position) {
		case 0:
			if (FormatStringUtil.isEmpty(MyApp.getInstence().birthday)
					|| FormatStringUtil.isEmpty(MyApp.getInstence().sex)) {
				Toast.makeText(this.getActivity(), "请先完善您的资料",
						Toast.LENGTH_LONG).show();
				intent.setClass(getActivity(), UserInfoAcitivity.class);
			} else {
				intent.setClass(getActivity(), AdvertActivity.class);
			}
			startActivity(intent);
			break;
		case 1:
//			intent.setClass(getActivity(), DownLoadTaskAcitivity.class);
			 intent.setClass(getActivity(), MoreTaskActivity.class);
			startActivity(intent);
			break;
		case 2:
			if (FormatStringUtil.isEmpty(MyApp.getInstence().email)) {
				Toast.makeText(this.getActivity(), "Email资料填写不全，无法参加问卷调查",
						Toast.LENGTH_LONG).show();
				intent.setClass(getActivity(), UserInfoAcitivity.class);
			} else {
				intent.setClass(getActivity(), QuestionTaskActivity.class);
			}
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(this.getActivity(), TaskWallActivity.class);
			intent.putExtra(OffWowContants.KEY_WEB_SITE_ID,"1950");   //【必须传入,格式位字符串】
			intent.putExtra(OffWowContants.KEY_USERID, MyApp.getInstence().getPhone());       // [可选参数]
			startActivity(intent);
			break;
		case 4:
			intent.setClass(getActivity(), GameAcitivity.class);
			startActivity(intent);
			break;
		case 5:
			intent.setClass(getActivity(), ShareAcitivity.class);
			startActivity(intent);
			break;
		case 6:
			intent.setClass(getActivity(), SignAcitivity.class);
			startActivity(intent);
			break;
		case 7:
			intent.setClass(getActivity(), RedAcitivity.class);
			startActivity(intent);
			break;
		}

	}

	@Override
	public boolean isIsloading() {
		return isLoading;
	}

	@Override
	public void setIsloading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	public Map<String, String> getHomeMap() {
		return dataMap;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		initView();

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
								MyApp.getInstence().getImei(), "1" });
				String json = HttpTool.httpGetJson1(
						HomeFragment.this.getActivity(), url, hd);
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
							mpa = new Home_viewpager_Adapter(HomeFragment.this
									.getActivity(), bannerList, hd);
							LogUtils.e("bannerList", bannerList.toString());
							home_viewpager.setAdapter(mpa);
							if (bannerList.size() > 1) {
								initLayout();
								HandleUtil.postDelayed(hd, HomeFragment.this,
										5000);
							}
						}
					});

				}
			}
		}).start();
	}
}
