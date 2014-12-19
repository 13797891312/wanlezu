package com.hzkjkf.imageloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.hzkjkf.util.HandleUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Imageloader_shop {
	private static Imageloader_shop imageloader;
	private ImageCache imageCache = new ImageCache(5 * 1024 * 1024);
	Bitmap result;

	private Imageloader_shop() {
	}

	public static Imageloader_shop getInstence() {
		if (imageloader == null) {
			imageloader = new Imageloader_shop();
		}
		return imageloader;
	}

	public void displayImage(final String url, ImageView imageView, Handler hd,
			ProgressBar bar) {
		Bitmap bitmap = imageCache.get(url);
		if (bitmap != null && !bitmap.isRecycled()) {
			imageView.setImageBitmap(bitmap);
			if (bar != null) {
				bar.setVisibility(View.GONE);
			}
			HandleUtil.sendInt(hd, 2);
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
					Bitmap bitmap = BitmapFactory.decodeStream(is);
					if (bitmap != null) {
						imageCache.put(url, bitmap);
						if (imageView.getTag() != null
								&& imageView.getTag().equals(url)) {
							HandleUtil.post(hd, new DisplayImageTask(imageView,
									bitmap, bar));
							HandleUtil.sendInt(hd, 2);
						}
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

	/** 加载但不缓存 ***/
	public void urlToBitmap1(final String url, final ImageView imageView,
			final Handler hd, final ProgressBar bar) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpURLConnection connection = (HttpURLConnection) new URL(
							url).openConnection();
					connection.setConnectTimeout(10000);
					connection.setReadTimeout(20000);
					InputStream is = connection.getInputStream();
					Bitmap bitmap = BitmapFactory.decodeStream(is);
					if (bitmap != null) {
						recBitmap();
						result = bitmap;
						HandleUtil.post(hd, new DisplayImageTask(imageView,
								bitmap, bar));
						HandleUtil.sendInt(hd, 2);
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					result = null;
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result = null;
				}
			}
		}).start();
	}

	/*** 销毁单张图片 ****/
	public void recBitmap() {
		if (result != null) {
			result.recycle();
			result = null;
			System.gc();
		}
	}

	/*** 清空缓存 ****/
	public void clearCache() {
		Map<String, Bitmap> m = imageCache.snapshot();
		Iterator it = m.keySet().iterator();
		while (it.hasNext()) {
			String key;
			key = (String) it.next();
			Log.e("  dafadf", "clearCache" + m.get(key));
			m.get(key).recycle();
			imageCache.remove(key);
		}
		imageCache.evictAll();
	}
}
