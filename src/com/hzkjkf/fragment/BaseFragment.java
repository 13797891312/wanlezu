package com.hzkjkf.fragment;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {
	protected boolean isLoad;

	/** �Ƿ������� ***/
	public abstract boolean isIsloading();

	/*** �Ƿ����ڼ��� ****/
	public abstract boolean isIsload();

	/*** �����Ƿ����ڼ��� ****/
	public abstract void setIsIsload(boolean isLoad);

	/*** �����Ƿ������� **/
	public abstract void setIsloading(boolean isloading);

	public abstract void initView();
}
