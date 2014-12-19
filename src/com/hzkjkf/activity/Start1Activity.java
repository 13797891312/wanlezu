package com.hzkjkf.activity;

import java.util.ArrayList;
import java.util.List;

import com.hzkjkf.util.BitmapUtil;
import com.hzkjkf.wanzhuan.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Start1Activity extends Activity {
	private ViewPager vp;
	private List<View> list = new ArrayList<View>();
	private int images[] = { R.drawable.png1, R.drawable.png2, R.drawable.png3,
			R.drawable.png4, };
	private Bitmap bit[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// 全屏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		vp = new Myviewpage(this);
		bit = new Bitmap[4];
		for (int i = 0; i < 3; i++) {
			ImageView iv = new ImageView(this);
			bit[i] = BitmapUtil.getBitmap1(images[i], this);
			Drawable dr = new BitmapDrawable(bit[i]);
			iv.setBackgroundDrawable(dr);
			list.add(iv);
		}
		bit[3] = BitmapUtil.getBitmap1(images[3], this);
		View view = View.inflate(this, R.layout.activity_start_1, null);
		((RelativeLayout) view.findViewById(R.id.rel))
				.setBackgroundDrawable(new BitmapDrawable(bit[3]));
		((Button) view.findViewById(R.id.button1))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Start1Activity.this.startActivity(new Intent(
								Start1Activity.this, LoginActivity.class));
						Start1Activity.this.finish();
					}
				});
		list.add(view);
		vp.setAdapter(new MyPagerAdapter());
		setContentView(vp);

	}

	// 自定义VIEWPAGER,屏蔽向右滑动
	class Myviewpage extends ViewPager {
		private int lastX;

		public Myviewpage(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean onTouchEvent(MotionEvent arg0) {
			// TODO Auto-generated method stub
			// if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
			// lastX = (int) arg0.getX();
			// }
			// if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
			// if (arg0.getX() > lastX) {
			// return false;
			// }
			// }

			return super.onTouchEvent(arg0);
		}
	}

	/*** viewpager适配器 ***/
	public class MyPagerAdapter extends PagerAdapter {
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			((ViewPager) container).addView(list.get(position));
			return list.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
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
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		for (int i = 0; i < bit.length; i++) {
			if (bit[i] != null) {
				bit[i].recycle();
				bit[i] = null;
			}
		}
	}

}
