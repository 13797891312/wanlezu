package com.hzkjkf.activity.baidu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchInfo;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.LatLngBounds.Builder;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.utils.DistanceUtil;
import com.hzkjkf.activity.NewcomerRedAcitivity;
import com.hzkjkf.javabean.RedData;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BaiduMapActivity extends Activity implements OnClickListener,
		CloudListener, OnMarkerClickListener {
	private MapView mMapView = null;
	private BaiduMap mBaiduMap;
	private LatLng ll_me;
	private RelativeLayout info_rel;
	private Button send_bt;
	private TextView title_tv, lv_tv, money_tv, space_tv;
	private String sendUser;
	private EditText msg_edt;
	private ProgressDialog dialog;
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(BaiduMapActivity.this, "网络不给力，请检查网络",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(BaiduMapActivity.this, "发送成功",
						Toast.LENGTH_SHORT).show();
				msg_edt.setText("");
				break;
			default:
				Toast.makeText(BaiduMapActivity.this,
						ErrorCode.getString(msg.what), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_map);
		((TextView) findViewById(R.id.title)).setText("附近玩家");
		CloudManager.getInstance().init(this);
		mMapView = (MapView) findViewById(R.id.bmapView);
		info_rel = (RelativeLayout) findViewById(R.id.info_rel);
		send_bt = (Button) findViewById(R.id.button1);
		msg_edt = (EditText) findViewById(R.id.editText1);
		send_bt.setOnClickListener(this);
		title_tv = (TextView) findViewById(R.id.textView1);
		lv_tv = (TextView) findViewById(R.id.textView2);
		money_tv = (TextView) findViewById(R.id.textView3);
		space_tv = (TextView) findViewById(R.id.textView4);

		mBaiduMap = mMapView.getMap();
		mBaiduMap.setOnMarkerClickListener(this);
		nearBy(MyApp.getInstence().latitude, MyApp.getInstence().longitude);
		ll_me = new LatLng(MyApp.getInstence().latitude,
				MyApp.getInstence().longitude);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	@Override
	public void onGetDetailSearchResult(DetailSearchResult result, int arg1) {
		// TODO Auto-generated method stub
	}

	// 显示检索到的坐标
	@Override
	public void onGetSearchResult(CloudSearchResult result, int arg1) {
		// TODO Auto-generated method stub
		if (result != null && result.poiList != null
				&& result.poiList.size() > 0) {
			// mBaiduMap.clear();
			BitmapDescriptor bd = BitmapDescriptorFactory
					.fromResource(R.drawable.icon_gcoding);
			LatLng ll;
			LatLngBounds.Builder builder = new Builder();
			for (CloudPoiInfo info : result.poiList) {
				if (info.title.equals(MyApp.getInstence().getPhone())) {

					continue;
				}
				ll = new LatLng(info.latitude, info.longitude);
				int space = (int) DistanceUtil.getDistance(ll_me, ll);
				Bundle bundle = new Bundle();
				bundle.putString("lv", info.extras.get("lv") + "");
				bundle.putString("phone", info.extras.get("phone") + "");
				bundle.putString("money", info.extras.get("money") + "");
				bundle.putString("name", info.extras.get("name") + "");
				bundle.putString("space", String.valueOf(space));
				LogUtils.e("sdfasd", bundle + "");
				OverlayOptions oo = new MarkerOptions().icon(bd).position(ll)
						.title(info.title).extraInfo(bundle);
				mBaiduMap.addOverlay(oo);
				builder.include(ll);
			}

			LatLngBounds bounds = builder.build();
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
			try {
				mBaiduMap.animateMapStatus(u);
			} catch (Exception e) {
				// TODO: handle exception
			}
			mBaiduMap.setMapStatus(MapStatusUpdateFactory
					.newMapStatus(new MapStatus.Builder().zoom(15).build()));
		}
		setPoiAtMap(MyApp.getInstence().latitude, MyApp.getInstence().longitude);
		ondrowCircle();

	}

	/*** 设置中心点 ***/
	public void setPoiAtMap(double latitude, double longitude) {
		LatLng cenpt = new LatLng(latitude, longitude);
		// BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(null);
		LatLngBounds.Builder builder = new Builder();
		builder.include(cenpt);
		LatLngBounds bounds = builder.build();
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
		mBaiduMap.animateMapStatus(u);
	}

	/*** 周边检索 ***/
	public void nearBy(double longitude, double latitude) {
		NearbySearchInfo info = new NearbySearchInfo();
		info.ak = "AO6OFZrtV9jWhafBxIpKZCRI";
		info.geoTableId = 77838;
		info.location = latitude + "," + longitude;
		info.radius = 2000;
		info.q = "";
		CloudManager.getInstance().nearbySearch(info);
	}

	Marker nastMarker;

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		// 获取Marker的坐标
		BitmapDescriptor bd = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding_press);
		BitmapDescriptor bd1 = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);
		arg0.setIcon(bd);
		if (nastMarker != null && nastMarker != arg0) {
			nastMarker.setIcon(bd1);
		}
		nastMarker = arg0;
		info_rel.setVisibility(View.VISIBLE);
		title_tv.setText(FormatStringUtil.isEmpty(arg0.getExtraInfo()
				.getString("name", "匿名用户")) ? "匿名用户" : arg0.getExtraInfo()
				.getString("name", "匿名"));
		lv_tv.setText("等级:lv" + arg0.getExtraInfo().getString("lv", "0"));
		money_tv.setText("已赚:" + arg0.getExtraInfo().getString("money", "0")
				+ "玩币");
		space_tv.setText("距离我:" + arg0.getExtraInfo().getString("space", "0")
				+ "米");
		sendUser = arg0.getExtraInfo().getString("phone", "");
		return true;
	}

	/*** 画圆 ***/
	public void ondrowCircle() {
		LatLng llCircle = new LatLng(MyApp.getInstence().latitude,
				MyApp.getInstence().longitude);
		OverlayOptions ooCircle = new CircleOptions().fillColor(0x50ff0000)
				.center(llCircle).stroke(new Stroke(3, 0x00000000))
				.radius(2000);
		mBaiduMap.addOverlay(ooCircle);
	}

	public void backClick(View v) {
		this.finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (msg_edt.getText().toString().isEmpty()) {
			Toast.makeText(this, "请输入消息类容", Toast.LENGTH_SHORT).show();
			return;
		}
		dialog = ProgressDialog.show(this, "", "正在加载，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei", "sendUser",
						"userMsg" }, new String[] { "10037",
						MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei(), sendUser,
						msg_edt.getText().toString() });
				String json = HttpTool.httpGetJson1(BaiduMapActivity.this, url,
						hd);
				if (!json.isEmpty()) {
					if (HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, 1);
					} else {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
					}
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();
	}
}
