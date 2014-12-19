package com.hzkjkf.adapter;

import com.hzkjkf.fragment.RankFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

public class Rank_viewpager_Adapter extends FragmentStatePagerAdapter {
	private RankFragment[] ranks;

	public Rank_viewpager_Adapter(FragmentManager fm, RankFragment[] ranks) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.ranks = ranks;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ranks.length;
	}

	@Override
	public android.support.v4.app.Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return ranks[arg0];
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return PagerAdapter.POSITION_NONE;
	}
}
