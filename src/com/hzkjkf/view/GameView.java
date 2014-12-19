package com.hzkjkf.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hzkjkf.activity.MainActivity;
import com.hzkjkf.util.BitmapUtil;
import com.hzkjkf.wanzhuan.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {
	private Paint paint = new Paint();
	private Paint paint1 = new Paint();
	private Bitmap bitmap;
	private Bitmap bitmap1;
	private Context context;
	private List<Data> list = new ArrayList<GameView.Data>();
	private int hight;

	public List<Data> getList() {
		return list;
	}

	private int images[] = { R.drawable.wzicon_03, R.drawable.wzicon_04,
			R.drawable.wzicon_05, R.drawable.wzicon_06, R.drawable.wzicon_07,
			R.drawable.wzicon_03, R.drawable.wzicon_13, R.drawable.wzicon_03,
			R.drawable.wzicon_17, R.drawable.wzicon_16, R.drawable.yxicon_19,
			R.drawable.wzicon_14, R.drawable.wzicon_12, R.drawable.wzicon_07 };
	private float infos[] = { 0.05f, 2.0f, 0.2f, 1.0f, 0, 0.05f, 0.5f, 0.05f,
			0.02f, 5.0f, 0.1f, 10.0f, 0.1f, 0 };

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		paint.setAntiAlias(true);
		paint1.setAntiAlias(true);
		hight = (int) (MainActivity.screenY - 130 * MainActivity.scale);
		bitmap1 = BitmapUtil.getBitmapFromRes(R.drawable.cjbj_07, context);
		initData();
	}

	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		paint.setAntiAlias(true);
		paint1.setAntiAlias(true);
		initData();
	}

	public void initData() {
		for (int i = 0; i < 14; i++) {
			bitmap = BitmapUtil.getBitmapFromRes(images[i], context);
			Data data;
			if (i <= 4) {
				data = new Data(bitmap, bitmap.getWidth() * i, 0, infos[i], i);
			} else if (i > 4 && i <= 7) {
				data = new Data(bitmap, bitmap.getWidth() * 4,
						bitmap.getHeight() * (i - 4), infos[i], i);
			} else if (i > 7 && i <= 11) {
				data = new Data(bitmap, bitmap.getWidth() * 4
						- bitmap.getWidth() * (i - 7), bitmap.getHeight() * 3,
						infos[i], i);
			} else {
				data = new Data(bitmap, 0, bitmap.getHeight() * 3
						- bitmap.getHeight() * (i - 11), infos[i], i);
			}
			list.add(data);
		}
		this.invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		paint.setColor(0xffE7DC73);
		// canvas.drawRect(getBitmapWidth()+(MainActivity.screenX-getBitmapWidth()*5)/2,
		// getBitmapHeight()+(hight-getBitmapHeight()*4)/2,
		// 4*getBitmapWidth()+(MainActivity.screenX-getBitmapWidth()*5)/2,
		// 3*getBitmapHeight()+(hight-getBitmapHeight()*4)/2, paint);
		canvas.drawBitmap(bitmap1, getBitmapWidth()
				+ (MainActivity.screenX - getBitmapWidth() * 5) / 2,
				getBitmapHeight() + (hight - getBitmapHeight() * 4) / 2, paint);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).paint(paint, canvas);
		}
		paint.setStyle(Style.STROKE);
		canvas.drawRect((MainActivity.screenX - getBitmapWidth() * 5) / 2,
				(hight - getBitmapHeight() * 4) / 2,
				(MainActivity.screenX - getBitmapWidth() * 5) / 2
						+ getBitmapWidth() * 5, (hight - getBitmapHeight() * 4)
						/ 2 + getBitmapHeight() * 4, paint);

	}

	public class Data {
		private Bitmap bitmap;
		private int posX;
		private int posY;
		private float info;
		private boolean isChecked = false;
		private int count;

		public Data(Bitmap bitmap, int posX, int posY, Float info, int count) {
			// TODO Auto-generated constructor stub
			this.info = info;
			this.bitmap = bitmap;
			this.posX = posX + (MainActivity.screenX - getBitmapWidth() * 5)
					/ 2;
			this.posY = posY + (hight - getBitmapHeight() * 4) / 2;
			this.count = count;
		}

		public void paint(Paint paint, Canvas canvas) {
			if (isChecked) {
				paint1.setColor(0xffF2CACA);
				canvas.drawRect(posX, posY, posX + bitmap.getWidth(), posY
						+ bitmap.getHeight(), paint1);
			} else if (count % 2 == 0) {
				paint1.setColor(0xffF8EEAA);
				canvas.drawRect(posX, posY, posX + bitmap.getWidth(), posY
						+ bitmap.getHeight(), paint1);
			} else if (count % 2 == 1) {
				paint1.setColor(0xffFDF5CA);
				canvas.drawRect(posX, posY, posX + bitmap.getWidth(), posY
						+ bitmap.getHeight(), paint1);
			}
			canvas.drawBitmap(bitmap, posX, posY, paint);
		}

		public float getInfo() {
			return info;
		}

		public int getCount() {
			return count;
		}

		public boolean isChecked() {
			return isChecked;
		}

		public void setChecked(boolean isChecked) {
			this.isChecked = isChecked;
		}
	}

	public int getBitmapWidth() {
		if (bitmap != null) {
			return bitmap.getWidth();
		} else {
			return 0;
		}
	}

	public int getBitmapHeight() {
		if (bitmap != null) {
			return bitmap.getHeight();
		} else {
			return 0;
		}
	}
}
