package com.hzkjkf.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Myview extends View {
	private Paint paint = new Paint();
	private int color;
	private int padding;

	public Myview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		color = attrs.getAttributeResourceValue(1, Color.BLACK);
		paint.setAntiAlias(true);

	}

	public Myview(Context context, int color, int padding) {
		super(context);
		// TODO Auto-generated constructor stub
		this.color = color;
		paint.setAntiAlias(true);
		this.padding = padding;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		paint.setColor(color);
		canvas.drawCircle(this.getWidth() / 2, this.getHeight() / 2,
				this.getWidth() / 2 - padding, paint);
	}

	public void setColor(int color) {
		this.color = color;
		this.invalidate();
	}
}
