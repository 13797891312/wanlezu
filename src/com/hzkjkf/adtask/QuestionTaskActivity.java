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

/*** 问券调查 ***/
public class QuestionTaskActivity extends Activity {
	private Bitmap bitmap;
	private TextView tv_1, tv_2, tv_3, tv_4, tv_5;
	private Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(QuestionTaskActivity.this, "网络不给力，请检查网络连接",
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
				intent.putExtra("type", "1");// 爱调研
				startActivity(intent);
			} else if (id == R.id.TextView_2) {
				intent.setClass(QuestionTaskActivity.this,
						Question1Acitvity.class);
				intent.putExtra("type", "0");// 问卷吧
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
		((TextView) findViewById(R.id.title)).setText("问卷调查");
		((TextView) findViewById(R.id.textView3))
				.setText("*问卷调查玩币由第三方调查公司发放，请认真填写，以下情况是不会发放玩币的:\n\t1,被筛选：不符合答题条件，比如此问卷只针对20-30岁的，如果您选择的是40岁的选项就会被筛选掉\n\t2,份额满：北京、上海各需要100份，某个时候上海已经收集了100份，北京只收集50份，接下来第101个上海人进来答题就会被告知配额满，而北京人可以继续答题");
		findViewById(R.id.iv_1).setBackgroundResource(R.drawable.wjicon_03);
		findViewById(R.id.iv_2).setBackgroundResource(R.drawable.wjicon_03);
		findViewById(R.id.iv_3).setBackgroundResource(R.drawable.wjicon_03);
		findViewById(R.id.iv_4).setBackgroundResource(R.drawable.wjicon_03);
		// findViewById(R.id.rel_duomeng).setVisibility(View.GONE);
//		findViewById(R.id.rel_yinggao).setVisibility(View.GONE);
		findViewById(R.id.rel_dianle).setVisibility(View.GONE);
		((TextView) findViewById(R.id.textView1)).setText("*重复填写同一问卷只会记录一次有效");
		tv_1 = ((TextView) findViewById(R.id.TextView_1));
		tv_1.setText("问卷调查一区(推荐)");
		tv_1.setOnClickListener(listener);
		tv_2 = ((TextView) findViewById(R.id.TextView_2));
		tv_2.setText("问卷调查二区");
		tv_2.setOnClickListener(listener);
		tv_3 = ((TextView) findViewById(R.id.TextView_3));
		tv_3.setOnClickListener(listener);
		tv_3.setText("问卷调查三区");
		tv_4 = ((TextView) findViewById(R.id.TextView_4));
		tv_4.setText("问卷调查四区");
	}

	/** 返回按钮监听 ***/
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
