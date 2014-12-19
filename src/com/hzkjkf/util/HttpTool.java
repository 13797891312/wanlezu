package com.hzkjkf.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.activity.LoginActivity;
import com.hzkjkf.activity.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/** 获取json文件 **/
public class HttpTool {

	private static SimpleDateFormat dateFormat = null;
	static {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
	}

	/*** 获取URL ***/
	public static synchronized String getUrl(String key[], String value[]) {
		String url = "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < value.length; i++) {
			sb.append(key[i] + "=");
			LogUtils.e("参数", key[i] + "=" + value[i]);
			try {
				sb.append(key[i].equals("pwd") ? EbotongSecurity.MD5(value[i])
						: value[i]);
				LogUtils.e("sdfasdf",
						key[i].equals("pwd") ? EbotongSecurity.MD5(value[i])
								: value[i]);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			sb.append("&");
		}
		sb.delete(sb.length() - 1, sb.length());
		// sb.append(EbotongSecurity.ebotongEncrypto(key[i].equals("pwd")?EbotongSecurity.MD5(value[i]):value[i]).replaceAll("\r|\n",
		// ""));
		try {
			url = MyApp.address
					+ (EbotongSecurity.ebotongEncrypto(URLEncoder.encode(
							sb.toString(), "utf-8"))).replaceAll("\r|\n", "");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtils.e("url1", url);
		return url;
	}

	public static synchronized boolean getFlag(String json) {
		boolean flag = false;
		JSONArray array;
		try {
			array = new JSONArray(json);
			JSONObject object = array.getJSONObject(0);
			flag = object.getBoolean("flag");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/** 获取单个字段值 **/
	public static synchronized String getString(String json, String key) {
		String value = "";
		JSONArray array;
		try {
			array = new JSONArray(json);
			JSONObject object = array.getJSONObject(0);
			value = object.getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	public static synchronized int getErrorCode(String json) {
		int errorCode = 0;
		JSONArray array;
		try {
			array = new JSONArray(json);
			JSONObject object = array.getJSONObject(0);
			errorCode = object.getInt("errorCode");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return errorCode;
	}

	public static synchronized String getMsg(String json) {
		String msg = "";
		JSONArray array;
		try {
			array = new JSONArray(json);
			JSONObject object = array.getJSONObject(0);
			msg = object.getString("msg");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}

	public static String httpGetJson1(final Context context, String url,
			Handler hd) {
		if (context == null) {
			return "";
		}
		String result = "";
		if (!isNetwork(context)) {
			HandleUtil.sendInt(hd, 0);
			return result;
		}
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url)
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(30000);
			connection.setDoOutput(true);                      
			connection.setDoInput(true); 
			InputStream is = connection.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line = "";
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			result = EbotongSecurity.ebotongDecrypto(sb.toString());
			if (result == null) {
				return "";
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		if (result != null && !result.isEmpty()) {
			if (!HttpTool.getFlag(result)) {
				if (HttpTool.getErrorCode(result) == 10008) {
					HandleUtil.post(hd, new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(context, "登陆已失效，请重新登录",
									Toast.LENGTH_LONG).show();
							Intent intent = new Intent(context,
									LoginActivity.class);
							MyApp.getInstence().setToken(null);
							context.startActivity(intent);
							((Activity) context).finish();
						}
					});
				}
			}
		}
		LogUtils.e("result", result);
		return result;
	}

	public static synchronized String httpGetJson2(final Context context,
			String url, Handler hd, byte[] data) {
		if (context == null) {
			return "";
		}
		String result = "";
		if (!isNetwork(context)) {
			HandleUtil.sendInt(hd, 0);
			return result;
		}
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url)
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			OutputStream os = connection.getOutputStream();
			os.write(data);
			os.flush();

			connection.setConnectTimeout(15000);
			connection.setReadTimeout(15000);

			InputStream is = connection.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line = "";
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			result = sb.toString();
			os.close();
			reader.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		if (result != null && !result.isEmpty()) {
			if (!HttpTool.getFlag(result)) {
				if (HttpTool.getErrorCode(result) == 10008) {
					HandleUtil.post(hd, new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(context, "登陆已失效，请重新登录",
									Toast.LENGTH_LONG).show();
							Intent intent = new Intent(context,
									LoginActivity.class);
							MyApp.getInstence().setToken(null);
							context.startActivity(intent);
							((Activity) context).finish();
						}
					});
				}
			}
		}
		LogUtils.e("result", result);
		return result;
	}

	public static synchronized Map<String, String> jsonToMap(String json) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			JSONArray array = new JSONArray(json);
			JSONObject object = array.getJSONObject(0);
			JSONArray ja1 = object.names();
			for (int j = 0; j < ja1.length(); j++) {
				map.put(ja1.get(j).toString(),
						(object.get(ja1.get(j).toString())).toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/*** 取出result数组 ***/
	public static synchronized JSONArray getResult(String json) {
		try {
			JSONArray ja = new JSONArray(json);
			JSONObject object = ja.getJSONObject(0);
			return object.getJSONArray("result");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*** 取出result ***/
	public static synchronized JSONObject getResultJson(String json) {
		try {
			JSONArray ja = new JSONArray(json);
			JSONObject object = ja.getJSONObject(0);
			return object.getJSONObject("result");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/***** 是否有网络 ****/
	public static boolean isNetwork(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {

			return false;
		}
		if (connectivityManager.getActiveNetworkInfo() == null) {
			return false;
		}
		return connectivityManager.getActiveNetworkInfo().isAvailable();
	}

	/**** 是否是有效电话 ****/
	public static boolean isPhone(String name) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(17[0-9])|(14[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(name);
		return m.matches();
	}

	/*** 是否是有效Email ***/
	public static boolean isEmail(String email) {
		boolean flag = true;
		if (email.indexOf("@") == -1 || email.indexOf(".") == -1) {

			flag = false;
		}
		if (flag) {
			if (email.indexOf("@") > email.indexOf("."))
				flag = false;
		}
		return flag;
	}

	/** 是否是有效密码 ***/
	public static boolean isGoodPWD(String pwd) {
		Pattern p = Pattern.compile("^[a-zA-Z0-9_]{6,16}$");
		Matcher m = p.matcher(pwd);
		return m.matches();
	}

	/** 是否是有效日期 ***/
	public static boolean isGoodDate(String s) {
		try {
			dateFormat.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}

	/*** 读取手机中的json文件，在没有测试服务器之前用来测试json解析 ***/
	public static String readJson(Context context, int id) {
		InputStream is = context.getResources().openRawResource(id);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String s = null;
		try {
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	public static String httpClientGetJson(String url){
		String result="";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);  
	      HttpConnectionParams.setSoTimeout(client.getParams(), 30000);
		HttpGet get = new HttpGet(url);//此处的URL为http://..../path?arg1=value&....argn=value
		HttpResponse response;
		try {
			response = client.execute(get);
			int code = response.getStatusLine().getStatusCode();//返回响应码
			InputStream is = response.getEntity().getContent();//服务器返回的数据
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line = "";
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			result = sb.toString();
			reader.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtils.e("result", result);
		return result;
	}
	
//	 public static String GetHttps(String https){  
//	        try{  
//	            SSLContext sc = SSLContext.getInstance("TLS");  
//	            sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());  
//	            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());  
//	            HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());  
//	            HttpsURLConnection conn = (HttpsURLConnection)new URL(https).openConnection();  
//	            conn.setDoOutput(true);  
//	            conn.setDoInput(true);  
//	            conn.connect();  
//	            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));   
//	            StringBuffer sb = new StringBuffer();   
//	            String line;   
//	            while ((line = br.readLine()) != null)   
//	                sb.append(line);  
//	            LogUtils.e("httpsJson", sb.toString());
//	            return sb.toString();  
//	           }catch(Exception e){  
//	        	   e.printStackTrace();
//	        	   return ""; 
//	           }        
//	     }  
//	  
//	  
//	  
//	      static class MyHostnameVerifier implements HostnameVerifier{  
//	            @Override  
//	            public boolean verify(String hostname, SSLSession session) {  
//	                    // TODO Auto-generated method stub  
//	                    return true;  
//	            }  
//	  
//	       }  
//	  
//	       static class MyTrustManager implements X509TrustManager{  
//	     
//	            @Override  
//	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
//	                    // TODO Auto-generated method stub  
//	                    return null;  
//	            }
//
//				@Override
//				public void checkClientTrusted(
//						java.security.cert.X509Certificate[] chain,
//						String authType) throws CertificateException {
//					// TODO Auto-generated method stub
//					
//				}
//
//				@Override
//				public void checkServerTrusted(
//						java.security.cert.X509Certificate[] chain,
//						String authType) throws CertificateException {
//					// TODO Auto-generated method stub
//					
//				}
//	  
//	      }     
}
