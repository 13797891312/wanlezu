package com.hzkjkf.fragment;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.activity.TaskInfoActivity;
import com.hzkjkf.adapter.MyTask_listview_adapter;
import com.hzkjkf.adtask.Question1Acitvity;
import com.hzkjkf.javabean.QuestionBean;
import com.hzkjkf.javabean.TaskData;
import com.hzkjkf.util.DownFileUtil;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.MyApp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**** 推广任务列表 ****/
public class TaskListFragment extends BaseFragment {
	private ListView taskListView;
	private MyTask_listview_adapter mla;
	private List<TaskData> list = new ArrayList<TaskData>();
	private ProgressDialog dialog;
	private int type;
	private OnItemClickListener listener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(TaskListFragment.this.getActivity(),
					TaskInfoActivity.class);
			intent.putExtra("data", list.get(position));
			TaskListFragment.this.startActivity(intent);
		}
	};

	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(TaskListFragment.this.getActivity(),
						"网络不给力，请检查网络", Toast.LENGTH_LONG).show();
				break;
			case 1:
				mla.notifyDataSetChanged();
				break;
			default:
				Toast.makeText(TaskListFragment.this.getActivity(),
						ErrorCode.getString(msg.what), Toast.LENGTH_LONG)
						.show();
				break;
			}

		};
	};

	public TaskListFragment(int type) {
		// TODO Auto-generated constructor stub
		this.type = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		taskListView = new ListView(this.getActivity());
		taskListView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		taskListView.setDividerHeight(0);
		taskListView.setOnItemClickListener(listener);
		mla = new MyTask_listview_adapter(this.getActivity(), list);
		taskListView.setAdapter(mla);
		initData();
		return taskListView;
	}

	/*** 加载数据 ***/
	private void initData() {
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this.getActivity(), "", "正在加载，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool
						.getUrl(new String[] { "classId", "phoneNumber",
								"requestId", "imei", "type" },
								new String[] { "10053",
										MyApp.getInstence().getPhone(),
										MyApp.getInstence().getToken(),
										MyApp.getInstence().getImei(),
										String.valueOf(type) });
				String json = HttpTool.httpGetJson1(
						TaskListFragment.this.getActivity(), url, hd);
				if (!json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					JSONArray result = HttpTool.getResult(json);
					try {
						for (int i = 0; i < result.length(); i++) {
							JSONObject object = result.getJSONObject(i);
							TaskData data = new TaskData();
							data.setAPKname(object
									.getString("apply_package_name"));
							data.setInfo(object.getString("sim_describe"));
							data.setMoney((int) (object
									.getDouble("total_money") * 100));
							data.setName(object.getString("apply_name"));
							data.setSize(object.getString("apply_size"));
							data.setSmallImageUrl(object.getString("logo_url"));
							data.setVersion(object.getString("edition"));
							data.setTask1_info(object.getString("desc_1_info"));
							data.setTask2_info(object.getString("desc_2_info"));
							data.setTask3_info(object.getString("desc_3_info"));
							data.setImageUrl(object.getString("effect_pic_url"));
							data.setDownUrl(object
									.getString("apply_download_url"));
							data.setTask1_money((int) (object
									.getDouble("desc_1_money") * 100));
							data.setTask2_money((int) (object
									.getDouble("desc_2_money") * 100));
							data.setTask3_money((int) (object
									.getDouble("desc_3_money") * 100));
							data.setTryTime1(object.getInt("desc_1_trytime"));
							data.setTryTime2(object.getInt("desc_2_trytime"));
							data.setTryTime3(object.getInt("desc_3_trytime"));
							data.setUids(object.getString("uids"));
							data.setAppInfo(object.getString("det_describe"));
							if (type == 0) {
								data.setTask1_state(0);
								data.setTask2_state(2);
								data.setTask3_state(2);
							} else {
								data.setTask1_state(object
										.getInt("desc_1_state"));
								data.setTask2_state(object
										.getInt("desc_2_state"));
								data.setTask3_state(object
										.getInt("desc_3_state"));
								data.setLogUids(object.getString("logUids"));
							}
							data.setType(type);
							// 如果手机中安装了则不显示
							if (type == 0) {
								if (!DownFileUtil.isInstall(data.getAPKname(),
										TaskListFragment.this.getActivity())) {
									list.add(data);
								}
							} else {
								list.add(data);
							}
						}
						HandleUtil.sendInt(hd, 1);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						HandleUtil.sendInt(hd, 0);
					}
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();
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
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		list.clear();
	}

	public void reFresh() {
		list.clear();
		initData();
	}
}
