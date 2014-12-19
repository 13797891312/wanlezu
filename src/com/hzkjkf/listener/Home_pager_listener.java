package com.hzkjkf.listener;

import java.util.List;
import java.util.Map;

import com.hzkjkf.activity.HomeViewPageActivity;
import com.hzkjkf.activity.MoreTaskActivity;
import com.hzkjkf.adapter.Home_viewpager_Adapter;
import com.hzkjkf.fragment.HomeFragment;
import com.hzkjkf.util.LogUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Home_pager_listener implements OnClickListener {
	private int position;
	private Context context;
	List<Map<String, String>> list;

	// public static Home_pager_listener getInstence(int position,Context
	// context){
	// if (myClickListener==null) {
	// myClickListener=new Home_pager_listener(context);
	// }
	// Home_pager_listener.position=position;
	// return myClickListener;
	// }
	public Home_pager_listener(Context context, int position,
			List<Map<String, String>> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.position = position;
		this.list = list;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, HomeViewPageActivity.class);
		intent.putExtra("url", list.get(position).get("url"));
		context.startActivity(intent);
		/** …Ë÷√«–ªª∂Øª≠ **/
		// ((Activity)
		// context).overridePendingTransition(android.R.anim.fade_in,
		// android.R.anim.fade_out);
	}
}
