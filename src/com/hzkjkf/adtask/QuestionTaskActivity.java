package com.hzkjkf.adtask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.adapter.DrawRecord_listview_adapter2;
import com.hzkjkf.adapter.SuperRecord_listview_adapter2;
import com.hzkjkf.util.BitmapUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.view.Foot;
import com.hzkjkf.view.FootListoner;
import com.hzkjkf.view.Head;
import com.hzkjkf.view.HeadListoner;
import com.hzkjkf.view.MyListView;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/*** ��ȯ���� ***/
public class QuestionTaskActivity extends Activity {
	private Bitmap bitmap;
	private TextView tv_1, tv_2, tv_3, tv_4, tv_5;
	private Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(QuestionTaskActivity.this, "���粻������������������",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				break;
			}
		};
	};

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			int id = v.getId();
			if (id == R.id.TextView_1) {
				intent.setClass(QuestionTaskActivity.this,
						Question1Acitvity.class);
				intent.putExtra("type", "1");// ������
				startActivity(intent);
			} else if (id == R.id.TextView_2) {
				intent.setClass(QuestionTaskActivity.this,
						Question1Acitvity.class);
				intent.putExtra("type", "0");// �ʾ��
				startActivity(intent);
			} else if (id == R.id.TextView_3) {
				intent.setClass(QuestionTaskActivity.this,
						QuestionGMOAcitvity.class);
				intent.putExtra("type", "2");// GMO
				startActivity(intent);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moretask);
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.question_banner);
		((ImageView) findViewById(R.id.imageView1)).setImageBitmap(bitmap);
		((TextView) findViewById(R.id.title)).setText("�ʾ����");
		((TextView) findViewById(R.id.textView3))
				.setText("*�ʾ��������ɵ��������鹫˾���ţ���������д����������ǲ��ᷢ����ҵ�:\n\t1,��ɸѡ�������ϴ���������������ʾ�ֻ���20-30��ģ������ѡ�����40���ѡ��ͻᱻɸѡ��\n\t2,�ݶ������������Ϻ�����Ҫ100�ݣ�ĳ��ʱ���Ϻ��Ѿ��ռ���100�ݣ�����ֻ�ռ�50�ݣ���������101���Ϻ��˽�������ͻᱻ��֪��������������˿��Լ�������");
		findViewById(R.id.iv_1).setBackgroundResource(R.drawable.wjicon_03);
		findViewById(R.id.iv_2).setBackgroundResource(R.drawable.wjicon_03);
		findViewById(R.id.iv_3).setBackgroundResource(R.drawable.wjicon_03);
		findViewById(R.id.iv_4).setBackgroundResource(R.drawable.wjicon_03);
		// findViewById(R.id.rel_duomeng).setVisibility(View.GONE);
//		findViewById(R.id.rel_yinggao).setVisibility(View.GONE);
		findViewById(R.id.rel_dianle).setVisibility(View.GONE);
		((TextView) findViewById(R.id.textView1)).setText("*�ظ���дͬһ�ʾ�ֻ���¼һ����Ч");
		tv_1 = ((TextView) findViewById(R.id.TextView_1));
		tv_1.setText("�ʾ����һ��(�Ƽ�)");
		tv_1.setOnClickListener(listener);
		tv_2 = ((TextView) findViewById(R.id.TextView_2));
		tv_2.setText("�ʾ�������");
		tv_2.setOnClickListener(listener);
		tv_3 = ((TextView) findViewById(R.id.TextView_3));
		tv_3.setOnClickListener(listener);
		tv_3.setText("�ʾ��������");
		tv_4 = ((TextView) findViewById(R.id.TextView_4));
		tv_4.setText("�ʾ��������");
	}

	/** ���ذ�ť���� ***/
	public void backClick(View v) {
		this.finish();
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
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		hd = null;
		BitmapUtil.recycle(bitmap);
	}

}
