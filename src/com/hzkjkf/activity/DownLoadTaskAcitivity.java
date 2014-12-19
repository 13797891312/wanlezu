package com.hzkjkf.activity;

import com.hzkjkf.fragment.MoreTaskFragment;
import com.hzkjkf.fragment.MyTaskFragment;
import com.hzkjkf.wanzhuan.R;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class DownLoadTaskAcitivity extends FragmentActivity {
	private Button myTask_btn, moreTask_btn;
	private FrameLayout frame;
	private ClickLisener lisener;
	private FragmentManager manager;
	private MyTaskFragment myTaskFragment;
	private MoreTaskFragment moreTaskFragment;
	public Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiayingyong);
		((TextView) findViewById(R.id.title)).setText("下应用");
		manager = this.getSupportFragmentManager();
		lisener = new ClickLisener();
		myTask_btn = (Button) findViewById(R.id.button_myTask);
		moreTask_btn = (Button) findViewById(R.id.button_moreTask);
		myTask_btn.setOnClickListener(lisener);
		moreTask_btn.setOnClickListener(lisener);
		frame = (FrameLayout) findViewById(R.id.frame);
		myTaskFragment = new MyTaskFragment(this);
		moreTaskFragment = new MoreTaskFragment();
		manager.beginTransaction().add(R.id.frame, myTaskFragment, "myTask")
				.commit();
	}

	/** 返回按钮监听 ***/
	public void backClick(View v) {
		this.finish();
	}

	class ClickLisener implements OnClickListener {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.button_myTask) {
				manager.beginTransaction().replace(R.id.frame, myTaskFragment)
						.commit();
				myTask_btn.setBackgroundResource(R.color.white);
				myTask_btn.setTextColor(Color.RED);
				moreTask_btn.setBackgroundResource(R.color.red);
				moreTask_btn.setTextColor(Color.WHITE);
			} else if (id == R.id.button_moreTask) {
				myTask_btn.setBackgroundResource(R.color.red);
				myTask_btn.setTextColor(Color.WHITE);
				moreTask_btn.setTextColor(Color.RED);
				moreTask_btn.setBackgroundResource(R.color.bg);
				manager.beginTransaction()
						.replace(R.id.frame, moreTaskFragment).commit();
			}

		}
	}
}
