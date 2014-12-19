package com.hzkjkf.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.hzkjkf.imageloader.Imageloader_homePager;
import com.hzkjkf.listener.Home_pager_listener;
import com.hzkjkf.util.BitmapUtil;

/*** viewpager  ≈‰∆˜ ***/
public class Home_viewpager_Adapter extends PagerAdapter {
	private List<View> viewList = new ArrayList<View>();
	private Handler hd;
	public Imageloader_homePager imageloader = new Imageloader_homePager();
	private List<Map<String, String>> list;

	public Home_viewpager_Adapter(Context context,
			List<Map<String, String>> list, Handler hd) {
		// TODO Auto-generated constructor stub
		this.hd = hd;
		this.list = list;
		for (int i = 0; i < list.size(); i++) {
			// list_vp.add(BitmapUtil.getBitmap1(images[i], context));
			ImageView iv = new ImageView(context);
			iv.setScaleType(ScaleType.FIT_XY);
			// iv.setImageBitmap(list_vp.get(i));
			iv.setOnClickListener(new Home_pager_listener(context, i, list));
			viewList.add(iv);
		}
		if (list.size() > 3) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			ImageView iv = new ImageView(context);
			iv.setScaleType(ScaleType.FIT_XY);
			// iv.setImageBitmap(list_vp.get(i));
			iv.setOnClickListener(new Home_pager_listener(context, i, list));
			viewList.add(iv);
		}
	}

	// @Override
	// public Object instantiateItem(ViewGroup container, int position) {
	// // TODO Auto-generated method stub
	// position=position%images.length;
	// ImageView iv = new ImageView(context);
	// iv.setScaleType(ScaleType.FIT_XY);
	// Imageloader_homePager.getInstence().displayImage(pagerData.get(position).get("iamgeURL"),
	// iv, hd, bar);
	// iv.setOnClickListener(Home_pager_listener
	// .getInstence(position, context));
	// ((ViewPager) container).addView(iv);
	// return iv;
	// }
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		position = position % (list.size() > 3 ? list.size() : list.size() * 2);
		imageloader.displayImage(
				list.get(position % (list.size())).get("imageUrl"),
				(ImageView) viewList.get(position), hd, null);
		((ViewPager) container).addView(viewList.get(position));
		return viewList.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size() > 1 ? Integer.MAX_VALUE : list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
		object = null;
	}

	public List<View> getViewList() {
		return viewList;
	}
}