package com.hzkjkf.fragment;

import net.miidiwall.SDK.AdWall;
import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsManager;
import cn.dm.android.DMOfferWall;

import com.advertwall.sdk.util.OffWowContants;
import com.bb.dd.BeiduoPlatform;
import com.dlnetwork.Dianle;
import com.hzkjkf.activity.MoreTaskActivity;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MoreTaskFragment extends BaseFragment implements OnClickListener {
	private String PublisherID = "96ZJ36BwzeH3jwTBwc";
	private TextView domob_tv, youmi_tv, dianle_tv, yinggao_tv;
	private Dialog dialog;
	private ImageView iv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_moretask, container,
				false);
		domob_tv = (TextView) view.findViewById(R.id.TextView_2);
		dianle_tv = (TextView) view.findViewById(R.id.TextView_4);
		yinggao_tv = (TextView) view.findViewById(R.id.TextView_3);
		dianle_tv.setOnClickListener(this);
		yinggao_tv.setOnClickListener(this);
		domob_tv.setOnClickListener(this);
		youmi_tv = (TextView) view.findViewById(R.id.TextView_1);
		youmi_tv.setOnClickListener(this);
		// BeiduoPlatform. setAppId(this.getActivity(),
		// "13425","149c5c9dd041113");
		DMOfferWall.getInstance(this.getActivity()).init(this.getActivity(),
				PublisherID, MyApp.getInstence().getPhone());
		// mDomobOfferWallManager = new OManager(this, PublisherID,
		// MyApp.getInstence().phone);
		Dianle.initGoogleContext(this.getActivity(),
				"e694c2eb116df67373d11b5128560220");
		Dianle.setCurrentUserID(this.getActivity(), MyApp.getInstence()
				.getPhone());

		// TODO Auto-generated method stub
		initYouMi();// ��ʼ������

		AdWall.init(this.getActivity().getApplicationContext(), "19098",
				"ib5i0df3j2f5oo1n");// ��ʼ���׵�
		AdWall.setUserParam(MyApp.getInstence().getPhone());
		return view;
	}

	/***** ��ʼ������ ****/

	private void initYouMi() {
		/**** ע������ ***/
		AdManager.getInstance(this.getActivity().getApplicationContext()).init(
				"176af3213f2f54fb", "1f637fefa7b0ae27", false);
		OffersManager.getInstance(this.getActivity()).setCustomUserId(
				MyApp.getInstence().getPhone());
		OffersManager.getInstance(this.getActivity()).onAppLaunch();
		// // �����û�����ͳ�Ʒ���,Ĭ�ϲ����������� false ֵҲ��������ֻ�д��� true �Ż����
		// AdManager.getInstance(this.getApplicationContext()).setUserDataCollect(
		// true);
	}

	@Override
	public boolean isIsloading() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isIsload() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setIsIsload(boolean isLoad) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setIsloading(boolean isloading) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.TextView_1) {
			dialog = ProgressDialog.show(this.getActivity(), "", "���ڼ���");
			dialog.setCancelable(true);
			getDomobList();
		} else if (id == R.id.TextView_2) {
			Dianle.showOffers(this.getActivity());
		} else if (id == R.id.TextView_3) {
			AdWall.showAppOffers(null);
		} else if (id == R.id.TextView_4) {
//			OffersManager.getInstance(this.getActivity()).showOffersWall();//����
//			Intent intent = new Intent(this.getActivity(), TaskWallActivity.class);
//			intent.putExtra(OffWowContants.KEY_WEB_SITE_ID,"1950");   //�����봫��,��ʽλ�ַ�����
//			intent.putExtra(OffWowContants.KEY_USERID, MyApp.getInstence().getPhone());       // [��ѡ����]
//			startActivity(intent);
		}

	}

	/*** ��ȡ�����б� ***/
	private void getDomobList() {
		DMOfferWall.getInstance(this.getActivity()).showOfferWall(
				this.getActivity());
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		OffersManager.getInstance(this.getActivity()).onAppExit();// ����������Դ
		System.gc();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (dialog != null) {
			dialog.cancel();
		}
	}
}
