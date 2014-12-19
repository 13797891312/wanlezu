package com.hzkjkf.imageloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import com.hzkjkf.util.FileUtil;
import com.hzkjkf.util.HandleUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Imageloader_homePager {
	private ImageCache imageCache = new ImageCache(8 * 1024 * 1024);

	public void displayImage(final String url, ImageView imageView, Handler hd,
			ProgressBar bar) {
		Bitmap bitmap = imageCache.get(url);
		if (bitmap != null && !bitmap.isRecycled()) {
			imageView.setImageBitmap(bitmap);
			if (bar != null) {
				bar.setVisibility(View.GONE);
			}
			return;
		}
		bitmap = FileUtil.getBitmapSD(url);
		if (bitmap != null && !bitmap.isRecycled()) {
			imageView.setImageBitmap(bitmap);
			imageCache.put("url", bitmap);
			if (bar != null) {
				bar.setVisibility(View.GONE);
			}
			return;
		}
		urlToBitmap(url, imageView, hd, bar);
	}

	public void urlToBitmap(final String url, final ImageView imageView,
			final Handler hd, final ProgressBar bar) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpURLConnection connection = (HttpURLConnection) new URL(
							url).openConnection();
					connection.setConnectTimeout(10000);
					connection.setReadTimeout(10000);
					InputStream is = connection.getInputStream();
					// ��ȡ����Bitmap��ѡ�����
					Options options = new Options();
					// ����ͼƬ��СΪԭͼƬ��1/2
					options.inJustDecodeBounds = false;
					if (Build.MODEL.equals("HUAWEI U9510E")) {
						options.inPreferredConfig = Bitmap.Config.ARGB_8888;
					} else {
						options.inPreferredConfig = Bitmap.Config.RGB_565;
					}
					options.inPurgeable = true;
					options.inInputShareable = true;
					Bitmap bitmap = BitmapFactory.decodeStream(is, null,
							options);
					if (bitmap != null) {
						imageCache.put(url, bitmap);
						HandleUtil.post(hd, new DisplayImageTask(imageView,
								bitmap, bar));
						FileUtil.savaBitmap(bitmap, url);
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	/*** ��ջ��� ****/
	public void clearCache() {
		Map<String, Bitmap> m = imageCache.snapshot();
		Iterator it = m.keySet().iterator();
		while (it.hasNext()) {
			String key;
			key = (String) it.next();
			m.get(key).recycle();
			imageCache.remove(key);
		}
		imageCache.evictAll();
	}
}
