package com.hzkjkf.activity;

import net.miidiwall.SDK.AdWall;
import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsManager;
import cn.dm.android.DMOfferWall;

import com.advertwall.sdk.util.OffWowContants;
import com.dlnetwork.Dianle;
import com.hzkjkf.util.BitmapUtil;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MoreTaskActivity extends Activity implements OnClickListener,
		net.youmi.android.offers.PointsChangeNotify {
	private String PublisherID = "96ZJ36BwzeH3jwTBwc";
	private TextView domob_tv, youmi_tv, dianle_tv, yinggao_tv,tv_5;
	private Dialog dialog;
	private ImageView iv;
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moretask);
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.taskbanner);
		((ImageView) findViewById(R.id.imageView1)).setImageBitmap(bitmap);
		iv = (ImageView) findViewById(R.id.imageView1);
		((TextView) findViewById(R.id.title)).setText("��Ӧ��");
		findViewById(R.id.rel_5).setVisibility(View.VISIBLE);
		domob_tv = (TextView) findViewById(R.id.TextView_2);
		dianle_tv = (TextView) findViewById(R.id.TextView_4);
		yinggao_tv = (TextView) findViewById(R.id.TextView_3);
		dianle_tv.setOnClickListener(this);
		yinggao_tv.setOnClickListener(this);
		domob_tv.setOnClickListener(this);
		youmi_tv = (TextView) findViewById(R.id.TextView_1);
		youmi_tv.setOnClickListener(this);
		tv_5 = (TextView) findViewById(R.id.TextView_5);
		tv_5.setOnClickListener(this);
		DMOfferWall.getInstance(this).init(this, PublisherID,
				MyApp.getInstence().getPhone());
		// mDomobOfferWallManager = new OManager(this, PublisherID,
		// MyApp.getInstence().phone);
		Dianle.initGoogleContext(this, "e694c2eb116df67373d11b5128560220");
		Dianle.setCurrentUserID(this, MyApp.getInstence().getPhone());

		// TODO Auto-generated method stub
		initYouMi();// ��ʼ������

		AdWall.init(this.getApplicationContext(), "19098", "ib5i0df3j2f5oo1n");// ��ʼ���׵�
		AdWall.setUserParam(MyApp.getInstence().getPhone());

	}

	/***** ��ʼ������ ****/

	private void initYouMi() {
		/**** ע������ ***/
		AdManager.getInstance(this.getApplicationContext()).init(
				"176af3213f2f54fb", "1f637fefa7b0ae27", false);
		OffersManager.getInstance(this).setCustomUserId(
				MyApp.getInstence().getPhone());
		OffersManager.getInstance(this).onAppLaunch();
		// // �����û�����ͳ�Ʒ���,Ĭ�ϲ����������� false ֵҲ��������ֻ�д��� true �Ż����
		// AdManager.getInstance(this.getApplicationContext()).setUserDataCollect(
		// true);
		PointsManager.getInstance(this).registerNotify(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		OffersManager.getInstance(this).onAppExit();// ����������Դ
		System.gc();
		BitmapUtil.recycle(bitmap);
	}

	@Override
	public void onClick(View v) {
		
		
		int id = v.getId();
		if (id == R.id.TextView_1) {
			dialog = ProgressDialog.show(this, "", "���ڼ���");
			dialog.setCancelable(true);
			getDomobList();
		} else if (id == R.id.TextView_2) {
			Dianle.showOffers(this);
		} else if (id == R.id.TextView_3 ) {
			AdWall.showAppOffers(null);
		} else if (id == R.id.TextView_4) {
			OffersManager.getInstance(this).showOffersWall();//����
		} else if(id == R.id.TextView_5){
			com.winad.android.offers.AdManager.setUserID(this, MyApp.getInstence().getPhone());
			com.winad.android.offers.AdManager.showAdOffers(this);
		};

	}

	/*** ��ȡ�����б� ***/
	private void getDomobList() {
		DMOfferWall.getInstance(this).showOfferWall(this);
	}

	/** ���ذ�ť���� ***/
	public void backClick(View v) {
		this.setResult(10);
		this.finish();
		// this.flush();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (dialog != null) {
			dialog.cancel();
		}
	}

	/*** �������䶯�Զ�ˢ��UI�ӿ� ***/
	@Override
	public void onPointBalanceChange(int arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "���׻��ֱ䶯" + arg0, Toast.LENGTH_SHORT).show();
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

	// private void flush(){
	// dialog = ProgressDialog.show(this, "", "����ˢ����ң����Ժ󡣡���");
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	// String url = HttpTool.getUrl(new String[] { "classId", "phoneNumber",
	// "requestId", "imei" },
	// new String[] { "10006", MyApp.getInstence().phone,
	// MyApp.getInstence().token,
	// MyApp.getInstence().imei });
	// String json = HttpTool.httpGetJson1(MoreTaskActivity.this, url, hd);
	// if (json.isEmpty()) {
	// hd.sendEmptyMessage(0);
	// return;
	// } else {
	// try {
	// JSONArray result = new JSONArray(json);
	// JSONObject object=result.getJSONObject(0).getJSONObject("result");
	// if (!result.getJSONObject(0).getBoolean("flag")) {
	// hd.sendEmptyMessage(0);
	// return;
	// }
	// final float money=Float.parseFloat(object.getString("accountBalance"));
	// final float thisMoney=money-MyApp.getInstence().surplus;
	// hd.post(new Runnable() {
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// Toast.makeText(MoreTaskActivity.this,
	// "�������������"+FormatStringUtil.getDesplay((String.valueOf(thisMoney>0?thisMoney:0)))+"���",
	// Toast.LENGTH_LONG).show();
	// MyApp.getInstence().surplus=money;
	// }
	// });
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	// hd.sendEmptyMessage(1);
	// }
	// }).start();
	// }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.setResult(10);
		this.finish();
		// flush();
	}

}
