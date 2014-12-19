package com.hzkjkf.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

public class DownFileUtil {
	private Context con;
	private String url;
	private String title;
	private String packageName;
	private String name;
	private long tag_id;
	private DownloadManager m_downLoadManager;

	/** ����ID ***/
	public interface LoadOverLisener {
		public void loadOver();
	}

	public LoadOverLisener lisener;

	/**
	 * 
	 * @param con
	 *            ������
	 * @param url
	 *            ���ص�ַ
	 * @param name
	 *            ������ļ���
	 * @param title
	 *            ������ʾ�ı���
	 */
	public DownFileUtil(Context con, String url, String name, String title,
			String packageName) {
		// TODO Auto-generated constructor stub
		this.con = con;
		this.url = url;
		this.name = name;
		this.title = title;
		this.packageName = packageName;
		/*** ��ʼ�������� ****/
		m_downLoadManager = (DownloadManager) con
				.getSystemService(Context.DOWNLOAD_SERVICE);
		// ע��㲥������
		DownLoadReceiver receiver = new DownLoadReceiver();
		con.getApplicationContext().registerReceiver(receiver,
				new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	/*** ���� **/
	public void downFile() {
		Uri uri = Uri.parse(url);
		Request request = new Request(uri);
		request.setTitle(title);
		request.setDescription("��������");
		// ��������ͼƬ�����λ�ú��ļ���
		request.setDestinationInExternalPublicDir("wanzhuanapp", name);
		request.setAllowedNetworkTypes(Request.NETWORK_WIFI
				| Request.NETWORK_MOBILE);
		tag_id = m_downLoadManager.enqueue(request);
	}

	/***
	 * ����������ϼ���
	 * 
	 * @param lisener
	 **/
	public void setDownOverLisener(LoadOverLisener lisener) {
		this.lisener = lisener;
	}

	/*** ������Ϲ㲥���� ***/
	private class DownLoadReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			long id = intent
					.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

			if (tag_id == id) {
				if (lisener != null) {
					lisener.loadOver();
				}
			}
		}
	}

	/**
	 * 
	 * @param apkName
	 *            ��װ���İ�������ʽΪcom.xxxx.xxxx;
	 * @return
	 */
	public static boolean isInstall(String apkName, Context context) {
		final PackageManager packageManager = context.getPackageManager();// ��ȡpackagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// ��ȡ�����Ѱ�װ����İ���Ϣ
		List<String> pName = new ArrayList<String>();// ���ڴ洢�����Ѱ�װ����İ���
		// ��pinfo�н���������һȡ����ѹ��pName list��
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				pName.add(pn);
			}
		}
		return pName.contains(apkName);// �ж�pName���Ƿ���Ŀ�����İ�������TRUE��û��FALSE
	}

	/**
	 * 
	 * @param appPackageName
	 *            ����
	 * @param context
	 */
	public static void startAPP(String appPackageName, Context context) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(
				appPackageName);
		context.startActivity(intent);
	}

	/**
	 * 
	 * @param path
	 *            ����·����ʽ mnt/sdcard/***.apk
	 * @param context
	 */
	public static void insertAPK(String path, Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(path)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * ��ǰ�������е�APP����
	 * ***/
	public static String getCurrentPk(Context context) {
		// ��ǰ�������е�Ӧ�õİ���
		ActivityManager am = (ActivityManager) context
				.getSystemService("activity");
		String currentrunningpk = am.getRunningTasks(1).get(0).topActivity
				.getPackageName();
		return currentrunningpk;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTag_id() {
		return tag_id;
	}

	public void setTag_id(long tag_id) {
		this.tag_id = tag_id;
	}

}
