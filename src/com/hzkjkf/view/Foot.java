package com.hzkjkf.view;

import com.hzkjkf.wanzhuan.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/** 自定义listView头部类 **/
public class Foot extends RelativeLayout {
	ProgressBar pb;
	TextView tv;
	RelativeLayout rl;
	/** 布局文件对象的宽高对象 **/
	RelativeLayout.LayoutParams lp;
	/** 正在刷新 */
	public final static int UPDATA = 0X03;
	/** 初始状态 */
	public final static int INIT = 0X00;
	public int current = 0X00;

	public Foot(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		// 通过布局加载器加载文件
		rl = (RelativeLayout) View.inflate(context, R.layout.foot, null);
		tv = (TextView) rl.findViewById(R.id.textView1);
		pb = (ProgressBar) rl.findViewById(R.id.progressBar1);
		lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 0);
		this.addView(rl, lp);

	}

	/** 头部对象不同状态下对应的逻辑 **/
	public void logic(int stataus) {
		current = stataus;
		switch (current) {
		case INIT:
			lp.height = 0;
			rl.setLayoutParams(lp);
			break;
		case UPDATA:
			lp.height = 100;
			rl.setLayoutParams(lp);
			pb.setVisibility(View.VISIBLE);
			break;
		}
	}
}
