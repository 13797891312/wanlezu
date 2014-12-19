package com.hzkjkf.view;

import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.wanzhuan.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/** �Զ���ͷ���� **/
public class MyFreshfHead extends RelativeLayout {

	/** ��¼�ϴ�״̬ **/
	int last;
	/** ת״̬��ת���� **/
	Animation am;
	ProgressBar pb;
	TextView tv;
	ImageView iv;
	RelativeLayout rl;
	/** �����ļ�����Ŀ�߶��� **/
	RelativeLayout.LayoutParams lp;
	/** ����״̬ */
	public final static int GONE = 0X04;
	/** ����ˢ�� */
	public final static int DOWN = 0X01;
	/** �ɿ�ˢ�� */
	public final static int UP = 0X02;
	/** ����ˢ�� */
	public final static int UPDATA = 0X03;
	/** ˢ����� */
	public final static int OK = 0X05;
	/** ˢ��ʧ�� */
	public final static int NO = 0X06;
	public int current = GONE;
	private Handler hd = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				rl.setLayoutParams(lp);
				break;
			case 1:
				current = GONE;
				logic(current);
				break;
			}
		};
	};

	public MyFreshfHead(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		rl = (RelativeLayout) View.inflate(context, R.layout.head, null);
		tv = (TextView) rl.findViewById(R.id.textView1);
		iv = (ImageView) rl.findViewById(R.id.imageView1);
		pb = (ProgressBar) rl.findViewById(R.id.progressBar1);
		rl.setBackgroundResource(R.drawable.shape_my_gridview);
		lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 0);
		this.addView(rl, lp);
	}

	public MyFreshfHead(Context context, View view) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/** �ı��ͷ������ĸߵķ��� **/
	public void setHeadHeight(int add) {
		lp.height += add;
		if (lp.height < 0) {
			lp.height = 0;
		}
		rl.setLayoutParams(lp);
		if (lp.height <= 150 && lp.height > 0) {
			this.logic(DOWN);
		} else if (lp.height > 150) {
			this.logic(UP);
		}
	}

	/** ͷ������ͬ״̬�¶�Ӧ���߼� **/
	public void logic(int stataus) {
		current = stataus;
		switch (current) {
		case DOWN:
			if (last == UP) {
				am = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF,
						0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				iv.startAnimation(am);
				am.setDuration(300);
				am.setFillAfter(true);
			}
			tv.setText("����ˢ��");
			pb.setVisibility(View.GONE);
			iv.setVisibility(View.VISIBLE);
			break;
		case UP:
			if (last == DOWN) {
				am = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF,
						0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				iv.startAnimation(am);
				am.setDuration(300);
				am.setFillAfter(true);
			}
			tv.setText("�ͷ�����ˢ��");
			pb.setVisibility(View.GONE);
			iv.setVisibility(View.VISIBLE);
			break;
		case UPDATA:
			lp.height = 130;
			rl.setLayoutParams(lp);
			tv.setText("����ˢ��");
			pb.setVisibility(View.VISIBLE);
			if (am != null) {
				am.setFillAfter(false);
			}
			iv.setVisibility(View.GONE);
			break;
		case OK:
			pb.setVisibility(View.GONE);
			iv.setVisibility(View.GONE);
			tv.setText("ˢ�����");
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					while (lp.height > 2) {
						lp.height -= 2;
						HandleUtil.sendInt(hd, 0);
						try {
							Thread.sleep(2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					HandleUtil.sendInt(hd, 1);
				}
			}).start();
			break;
		case NO:
			pb.setVisibility(View.GONE);
			iv.setVisibility(View.GONE);
			tv.setText("ˢ��ʧ��");
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					while (lp.height > 2) {
						lp.height -= 2;
						HandleUtil.sendInt(hd, 0);
						try {
							Thread.sleep(2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					HandleUtil.sendInt(hd, 1);
				}
			}).start();
			break;
		case GONE:
			lp.height = 0;
			rl.setLayoutParams(lp);
			break;
		}
		last = current;
	}
}
