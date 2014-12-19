package com.hzkjkf.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Build;
import android.util.Log;

public class FileUtil {
	public static void getFiles(String string, ArrayList<String> al) {
		// TODO Auto-generated method stub
		File file = new File(string);
		if (!file.exists()) {
			file.mkdirs();
			return;
		}
		File[] files = file.listFiles();
		for (int j = 0; j < files.length; j++) {
			String name = files[j].getName();
			al.add(name);
		}
	}

	public static Bitmap getBitmapSD(String url) {
		String path = "/sdcard/wanzhuanapp/" + url.replaceAll("/", "");
		Bitmap bitmap = null;
		File file = new File(path);
		if (file.exists()) {
			FileInputStream fos;
			try {
				fos = new FileInputStream(file);
				// 获取创建Bitmap的选项对象
				Options options = new Options();
				// 设置图片大小为原图片的1/2
				options.inJustDecodeBounds = false;
				if (Build.MODEL.equals("HUAWEI U9510E")) {
					options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				} else {
					options.inPreferredConfig = Bitmap.Config.RGB_565;
				}
				options.inPurgeable = true;
				options.inInputShareable = true;
				bitmap = BitmapFactory.decodeStream(fos, null, options);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	public static void savaBitmap(Bitmap bitmap, String url) {
		File files = new File("/sdcard/wanzhuanapp");
		if (!files.exists()) {
			files.mkdirs();
		}
		File file = new File("/sdcard/wanzhuanapp/" + url.replaceAll("/", ""));
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
