package com.hzkjkf.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hzkjkf.adapter.Taskinfo_listview_adapter;
import com.hzkjkf.fragment.MyTaskFragment.TaskOkReceiver;
import com.hzkjkf.imageloader.Imageloader;
import com.hzkjkf.imageloader.Imageloader_myTask;
import com.hzkjkf.javabean.TaskData;
import com.hzkjkf.javabean.TaskInfoData;
import com.hzkjkf.service_receiver.AppService;
import com.hzkjkf.util.DownFileUtil;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.DownFileUtil.LoadOverLisener;
import com.hzkjkf.wanzhuan.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskInfoActivity extends Activity {
	/*** 存放下载任务的集合 ***/
	public static Map<String, DownFileUtil> downMap = new HashMap<String, DownFileUtil>();
	private Taskinfo_listview_adapter tla;
	private ListView taskInfo_lv;
	private ImageView iv_info, iv_small;
	/** 详细信息 **/
	private TextView appInfo_tv, version_tv, size_tv, name_tv;
	/*** 下载按钮 **/
	private Button down_btn;
	private List<TaskInfoData> list = new ArrayList<TaskInfoData>();
	public static TaskData data;
	private TaskOkReceiver receiver;
	public Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.activity_taskinfo, null);
		setContentView(view);
		((TextView) findViewById(R.id.title)).setText("任务详细");
		data = (TaskData) this.getIntent().getSerializableExtra("data");
		taskInfo_lv = (ListView) findViewById(R.id.taskinfo_listview);
		iv_info = (ImageView) findViewById(R.id.imageView_info);
		iv_small = (ImageView) findViewById(R.id.imageView1);
		appInfo_tv = (TextView) findViewById(R.id.textView_info);
		name_tv = (TextView) findViewById(R.id.textView_name);
		size_tv = (TextView) findViewById(R.id.textView_size);
		version_tv = (TextView) findViewById(R.id.textView_version);
		name_tv.setText(data.getName());
		size_tv.setText("大小 : " + data.getSize() + "M");
		version_tv.setText("版本 : " + data.getVersion());
		appInfo_tv.setText(data.getAppInfo());
		Imageloader_myTask.getInstence().displayImage(data.getSmallImageUrl(),
				iv_small, null, null);
		Imageloader.getInstence().urlToBitmap1(data.getImageUrl(), iv_info, hd,
				null);
		down_btn = (Button) findViewById(R.id.button_down);
		if (DownFileUtil.isInstall(data.getAPKname(), this)) {
			down_btn.setText("已下载,点击试用");
		} else {
			down_btn.setText("立即下载试用");
		}
		down_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				if (((Button) v).getText().toString().equals("立即下载试用")) {
					downAPK(v);
					Toast.makeText(TaskInfoActivity.this, "开始下载",
							Toast.LENGTH_LONG).show();
				} else {
					try {
						DownFileUtil.startAPP(data.getAPKname(),
								TaskInfoActivity.this);
						Intent intent2 = new Intent(TaskInfoActivity.this,
								AppService.class);
						intent2.putExtra("data", data);
						TaskInfoActivity.this.startService(intent2);
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(TaskInfoActivity.this, "开始下载",
								Toast.LENGTH_LONG).show();
						downAPK(v);
					}
				}
			}
		});
		tla = new Taskinfo_listview_adapter(this, list);
		taskInfo_lv.setAdapter(tla);
		initData();

		hd.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (view != null && view.getWidth() > 0 && view.getHeight() > 0) {
					// 此处代表改activity已经初始化完毕
					// 停止检测
					getTotalHeightofListView(taskInfo_lv);
					hd.removeCallbacks(this);
				} else {
					hd.postDelayed(this, 20);
				}
			}
		}, 10);

		// 注册广播接收器
		IntentFilter intentFilter = new IntentFilter();
		receiver = new TaskOkReceiver();
		intentFilter.addAction("com.hzkjkf.action.TaskOk");
		this.registerReceiver(receiver, intentFilter);
	}

	/*** 下载APK ***/
	private void downAPK(final View v) {
		// 下载
		DownFileUtil downfile = new DownFileUtil(TaskInfoActivity.this,
				data.getDownUrl(), data.getName() + ".apk", data.getName(),
				data.getAPKname());
		downfile.setDownOverLisener(new LoadOverLisener() {
			@Override
			public void loadOver() {
				// TODO Auto-generated method stub
				Toast.makeText(TaskInfoActivity.this, "下载完毕", Toast.LENGTH_LONG)
						.show();
				DownFileUtil.insertAPK(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/wanzhuanapp/" + data.getName() + ".apk",
						TaskInfoActivity.this);
				((Button) v).setText("点击打开");
				v.setEnabled(true);
			}
		});
		downfile.downFile();
		downMap.put(downfile.getPackageName(), downfile);
		((Button) v).setText("正在下载");
		v.setEnabled(false);

	}

	/*** 请求网络获取数据 ***/
	private void initData() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 3; i++) {
			TaskInfoData InfoData = new TaskInfoData();
			if (i == 0) {
				InfoData.setMoney(data.getTask1_money());
				InfoData.setState(data.getTask1_state());
				InfoData.setTitle(data.getTask1_info());
			} else if (i == 1) {
				InfoData.setMoney(data.getTask2_money());
				InfoData.setState(data.getTask2_state());
				InfoData.setTitle(data.getTask2_info());
			} else if (i == 2) {
				InfoData.setMoney(data.getTask3_money());
				InfoData.setState(data.getTask3_state());
				InfoData.setTitle(data.getTask3_info());
			}
			list.add(InfoData);
		}
		tla.notifyDataSetChanged();
	}

	/*** 返回按钮监听 **/
	public void backClick(View v) {
		this.finish();
		hd = null;
		Imageloader.getInstence().recBitmap();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		hd = null;
	}

	public static void getTotalHeightofListView(ListView listView) {
		ListAdapter mAdapter = listView.getAdapter();
		if (mAdapter == null) {
			return;
		}
		int totalHeight = 0;
		View view = listView.getChildAt(0);
		totalHeight = view.getMeasuredHeight() * mAdapter.getCount();
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (mAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Imageloader.getInstence().recBitmap();
		this.unregisterReceiver(receiver);
	}

	/*** 任务完成广播接收 ***/
	public class TaskOkReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("com.hzkjkf.action.TaskOk")) {
				TaskInfoActivity.this.finish();
				Imageloader.getInstence().recBitmap();
			}
		}
	}
}
