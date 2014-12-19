package com.hzkjkf.more;

import com.hzkjkf.adapter.Rank_viewpager_Adapter;
import com.hzkjkf.fragment.RankFragment;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Rank extends FragmentActivity implements OnPageChangeListener {
	private RankFragment[] ranks = new RankFragment[4];
	private Button[] buttons = new Button[4];
	private FragmentManager manager;
	private ViewPager rank_vp;
	private int position;
	private String[] types = { "day", "week", "month", "all" };
	public static Rank rank;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_rank);
		rank = this;
		((TextView) findViewById(R.id.title)).setText("ÍæÀÖ×å°ñµ¥");
		buttons[0] = (Button) findViewById(R.id.button_day);
		buttons[1] = (Button) findViewById(R.id.button_week);
		buttons[2] = (Button) findViewById(R.id.button_month);
		buttons[3] = (Button) findViewById(R.id.button_rank);
		rank_vp = (ViewPager) findViewById(R.id.viewpager_rank);
		for (int i = 0; i < ranks.length; i++) {
			ranks[i] = new RankFragment(types[i]);
			buttons[i].setOnClickListener(new Rank_pager_listener(i));
		}
		Rank_viewpager_Adapter rva = new Rank_viewpager_Adapter(
				this.getSupportFragmentManager(), ranks);
		rank_vp.setAdapter(rva);
		rank_vp.setOnPageChangeListener(this);
		initButtons(0);
	}

	public void backClick(View v) {
		for (int i = 0; i < ranks.length; i++) {
			ranks[i].hd = null;
		}
		this.finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		for (int i = 0; i < ranks.length; i++) {
			ranks[i].hd = null;
		}
	}

	public class Rank_pager_listener implements OnClickListener {
		private int position;

		private Rank_pager_listener(int position) {
			// TODO Auto-generated constructor stub
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			rank_vp.setCurrentItem(position, false);
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
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		initButtons(arg0);
	}

	private void initButtons(int arg0) {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setEnabled(true);
			buttons[i].setTextColor(Color.WHITE);
		}
		buttons[arg0].setEnabled(false);
		buttons[arg0].setTextColor(Color.RED);
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		rank = null;
	}
}
