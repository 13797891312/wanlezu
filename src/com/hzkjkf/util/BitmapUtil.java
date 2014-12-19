package com.hzkjkf.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Build;

public class BitmapUtil {
	public static Bitmap getBitmap(int resId, Context con) {
		// 获取创建Bitmap的选项对象
		Options options = new Options();
		// 设置图片大小为原图片的1/2
		options.inSampleSize = 2;
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;
		Bitmap bitmap = BitmapFactory.decodeResource(con.getResources(), resId,
				options);
		return bitmap;
	}

	public static Bitmap getBitmapFromRes(int resId, Context con) {
		// 获取创建Bitmap的选项对象
		Options options = new Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeResource(con.getResources(), resId,
				options);
		return bitmap;

	}

	public static Bitmap getBitmap1(int resId, Context con) {
		// 获取创建Bitmap的选项对象
		Options options = new Options();
		options.inJustDecodeBounds = false;
		if (Build.MODEL.equals("HUAWEI U9510E")) {
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		} else {
			options.inPreferredConfig = Bitmap.Config.RGB_565;
		}
		options.inPurgeable = true;
		options.inInputShareable = true;
		Bitmap bitmap = BitmapFactory.decodeResource(con.getResources(), resId,
				options);
		return bitmap;
	}

	public static void recycle(Bitmap bitmap) {
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
	}

}
