package com.hzkjkf.view;

import com.hzkjkf.util.LogUtils;
import com.hzkjkf.wanzhuan.R;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;

public class MyRefreshLayout extends LinearLayout {
	public MyFreshfHead head;
	/** �Ƿ��ڶ��� **/
	boolean isTop;
	/** �Ƿ��ڵײ� **/
	boolean isBottom;
	HeadListoner listoner;
	int lastY;
	int lastY1;
	/** ͷ������ **/
	public Foot foot;
	// ����ϵ��
	final int RADIO = 2;

	public MyRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return true;
	}

	/** ���ͷ��ע��������� ***/
	public void SetOnHeadRefreshListener(HeadListoner listoner) {
		this.listoner = listoner;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (head == null) {
			head = (MyFreshfHead) this.findViewById(R.id.layout_head);
		}
		/** ListView����״̬���� ***/
		if (head.current != Head.UPDATA && head.current != Head.OK) {
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				lastY = (int) ev.getY();
			} else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
				int add = (int) ((ev.getY() - lastY) / RADIO);
				head.setHeadHeight(add);
				lastY = (int) ev.getY();
				if (head.current != Head.GONE) {
					return true;
				}
			} else if (ev.getAction() == MotionEvent.ACTION_UP
					&& head.current != Head.GONE) {
				if (head.current == Head.UP) {
					head.logic(Head.UPDATA);
					if (listoner != null) {
						listoner.headRefresh();
					}
				} else if (head.current == Head.DOWN) {
					head.logic(Head.GONE);
				}
				return true;
			}
		}
		return super.dispatchTouchEvent(ev);
	}
}
