package com.hzkjkf.location;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.util.SaveDate;

public class MyLocationListener implements BDLocationListener {
	Context context;
	SaveDate sd;
	Handler hd;

	public MyLocationListener(Context context, Handler hd) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.hd = hd;
	}

	@Override
	public void onReceiveLocation(final BDLocation location) {
		if (location == null)
			return;
		// StringBuffer sb = new StringBuffer(256);
		// sb.append("time : ");
		// sb.append(location.getTime());
		// sb.append("\nerror code : ");
		// sb.append(location.getLocType());
		// sb.append("\nlatitude : ");
		// sb.append(location.getLatitude());
		// sb.append("\nlontitude : ");
		// sb.append(location.getLongitude());
		// sb.append("\nradius : ");
		// sb.append(location.getRadius());
		// if (location.getLocType() == BDLocation.TypeGpsLocation) {
		// sb.append("\nspeed : ");
		// sb.append(location.getSpeed());
		// sb.append("\nsatellite : ");
		// sb.append(location.getSatelliteNumber());
		// } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
		// sb.append("\naddr : ");
		// sb.append(location.getAddrStr());
		// }
		// Log.e("½á¹û", sb.toString());
		sd = SaveDate.getInstence(context);
		if (location.getCity() != null && location.getProvince() != null) {
			MyApp.getInstence().longitude = location.getLongitude();
			MyApp.getInstence().latitude = location.getLatitude();
			MyApp.getInstence().addrStr = location.getAddrStr();
			MyApp.getInstence().city = location.getCity();
			HandleUtil.sendInt(hd, 6);
		}

	}
}