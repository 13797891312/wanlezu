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

/** �Զ���listViewͷ���� **/
public class Foot extends RelativeLayout {
	ProgressBar pb;
	TextView tv;
	RelativeLayout rl;
	/** �����ļ�����Ŀ�߶��� **/
	RelativeLayout.LayoutParams lp;
	/** ����ˢ�� */
	public final static int UPDATA = 0X03;
	/** ��ʼ״̬ */
	public final static int INIT = 0X00;
	public int current = 0X00;

	public Foot(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		// ͨ�����ּ����������ļ�
		rl = (RelativeLayout) View.inflate(context, R.layout.foot, null);
		tv = (TextView) rl.findViewById(R.id.textView1);
		pb = (ProgressBar) rl.findViewById(R.id.progressBar1);
		lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 0);
		this.addView(rl, lp);

	}

	/** ͷ������ͬ״̬�¶�Ӧ���߼� **/
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
