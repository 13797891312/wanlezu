package com.hzkjkf.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class MyListView extends ListView {
	/** 是否在顶部 **/
	boolean isTop;
	/** 是否在底部 **/
	boolean isBottom;
	HeadListoner listoner;
	FootListoner listoner2;
	int lastY;
	int lastY1;
	/** 头部对象 **/
	public Head head;
	public Foot foot;
	// 阻尼系数
	final int RADIO = 2;

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		// 实例化头部兵添加到当前自定义视图对象中
		head = new Head(context);
		foot = new Foot(context);
		this.addFooterView(foot);
		this.addHeaderView(head);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (foot.current == foot.UPDATA) {
			return super.onTouchEvent(ev);
		}
		/** ListView滚动状态监听 ***/
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			this.setOnScrollListener(new OnScrollListener() {
				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					if (firstVisibleItem == 0) {
						isTop = true;
					} else
						isTop = false;
					if (firstVisibleItem + visibleItemCount == totalItemCount) {
						isBottom = true;
					} else {
						isBottom = false;
					}
				}
			});
		}
		if (isBottom && foot.current != foot.UPDATA) {
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				lastY1 = (int) ev.getY();
			} else if (ev.getAction() == MotionEvent.ACTION_UP) {
				int y = (int) ev.getY();
				if (lastY1 - y > 50 && listoner2 != null
						&& head.current == Head.GONE) {
					listoner2.footRefresh();
					this.setSelection(this.getBottom());
				}
			}

		}
		if (head.current != Head.UPDATA && head.current != Head.OK && isTop) {
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
		return super.onTouchEvent(ev);
	}

	/** 添加头部注册监听函数 ***/
	public void SetOnHeadRefreshListener(HeadListoner listoner) {
		this.listoner = listoner;
	}

	/** 添加尾部注册监听函数 ***/
	public void SetOnFootRefreshListener(FootListoner listoner) {
		this.listoner2 = listoner;
	}
}
