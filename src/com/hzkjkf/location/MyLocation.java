package com.hzkjkf.location;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class MyLocation {
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener;
	public Context context;

	public MyLocation(Context context, Handler hd) {
		// TODO Auto-generated constructor stub
		this.context = context;
		myListener = new MyLocationListener(context, hd);
	}

	public void startLocation() {
		// TODO Auto-generated method stub
		mLocationClient = new LocationClient(context); // 婢圭増妲慙ocationClient缁拷
		mLocationClient.registerLocationListener(myListener); // 濞夈劌鍞介惄鎴濇儔閸戣姤鏆�
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Battery_Saving);// 鐠佸墽鐤嗙�姘秴濡�绱�
		option.setCoorType("bd09ll");// 鏉╂柨娲栭惃鍕暰娴ｅ秶绮ㄩ弸婊勬Ц閻ф儳瀹崇紒蹇曞惈鎼达讣绱濇妯款吇閸婄扯cj02
		option.setIsNeedAddress(true);// 鏉╂柨娲栭惃鍕暰娴ｅ秶绮ㄩ弸婊冨瘶閸氼偄婀撮崸锟戒繆閹拷
		option.setNeedDeviceDirect(true);// 鏉╂柨娲栭惃鍕暰娴ｅ秶绮ㄩ弸婊冨瘶閸氼偅澧滈張鐑樻簚婢跺娈戦弬鐟版倻
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		else
			Log.d("LocSDK3", "locClient is null or not started");

	}
}
