package com.hzkjkf.fragment;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {
	protected boolean isLoad;

	/** 是否加载完成 ***/
	public abstract boolean isIsloading();

	/*** 是否正在加载 ****/
	public abstract boolean isIsload();

	/*** 设置是否正在加载 ****/
	public abstract void setIsIsload(boolean isLoad);

	/*** 设置是否加载完成 **/
	public abstract void setIsloading(boolean isloading);

	public abstract void initView();
}
