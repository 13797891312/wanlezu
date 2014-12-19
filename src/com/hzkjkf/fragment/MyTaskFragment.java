package com.hzkjkf.fragment;

import com.hzkjkf.util.LogUtils;
import com.hzkjkf.wanzhuan.R;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyTaskFragment extends BaseFragment {
	private TextView task_tv, signTask_tv;
	private FragmentManager manager;
	private TaskListFragment taskFragment;
	private TaskListFragment taskFragment_sign;
	/**** 当前显示的Fragment ****/
	private TaskListFragment currentFragment;
	private OnClickListener lisener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.textView_task) {
				currentFragment = taskFragment;
				manager.beginTransaction()
						.replace(R.id.frame1, taskFragment, "task1").commit();
				task_tv.setBackgroundResource(R.drawable.shape_mytask_tasklist_press);
				signTask_tv.setBackgroundResource(R.drawable.shape_sgintask);
			} else if (id == R.id.textView_signTask) {
				currentFragment = taskFragment_sign;
				task_tv.setBackgroundResource(R.drawable.shape_mytask_tasklist);
				signTask_tv
						.setBackgroundResource(R.drawable.shape_sgintask_press);
				manager.beginTransaction()
						.replace(R.id.frame1, taskFragment_sign, "task2")
						.commit();
			}
		}
	};

	public MyTaskFragment(Context context) {
		// TODO Auto-generated constructor stub
		/** 动态注册任务完成广播监听 **/
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.hzkjkf.action.TaskOk");
		context.registerReceiver(new TaskOkReceiver(), intentFilter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_mytask, container, false);
		manager = this.getActivity().getSupportFragmentManager();
		taskFragment_sign = new TaskListFragment(1);
		taskFragment = new TaskListFragment(0);
		task_tv = (TextView) view.findViewById(R.id.textView_task);
		signTask_tv = (TextView) view.findViewById(R.id.textView_signTask);
		task_tv.setOnClickListener(lisener);
		signTask_tv.setOnClickListener(lisener);
		currentFragment = taskFragment;
		manager.beginTransaction().add(R.id.frame1, taskFragment, "task1")
				.commit();
		return view;
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

	/*** 任务完成广播接收 ***/
	public class TaskOkReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("com.hzkjkf.action.TaskOk")) {
				currentFragment.reFresh();// 刷新列表
			}
		}
	}
}
