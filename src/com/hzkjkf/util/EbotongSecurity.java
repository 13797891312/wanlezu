package com.hzkjkf.util;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.rt.BASE64Decoder;
import com.rt.BASE64Encoder;

public class EbotongSecurity {
	// The length of Encryptionstring should be 8 bytes and not be
	// a weak key
	private final static BASE64Encoder base64encoder = new BASE64Encoder();
	private final static BASE64Decoder base64decoder = new BASE64Decoder();
	private final static String encoding = "UTF-8";

	/**
	 * 加密
	 */
	public static synchronized String ebotongEncrypto(String str) {
		String result = str;
		if (str != null && str.length() > 0) {
			try {
				byte[] encodeByte = symmetricEncrypto(str.getBytes(encoding));
				result = base64encoder.encode(encodeByte);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 解密
	 */
	public static synchronized String ebotongDecrypto(String str) {
		String result = str;
		if (str != null && str.length() > 0) {
			try {
				byte[] encodeByte = base64decoder.decodeBuffer(str);
				byte[] decoder = EbotongSecurity.symmetricDecrypto(encodeByte);
				if (decoder != null) {
					result = new String(decoder, encoding);
				} else {
					result = "";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 瀵圭О鍔犲瘑鏂规硶
	 * 
	 * @param byteSource
	 *            闇�鍔犲瘑鐨勬暟鎹�?
	 * @return 缁忚繃鍔犲瘑鐨勬暟鎹�?
	 * @throws Exception
	 */
	public static byte[] symmetricEncrypto(byte[] byteSource) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			int mode = Cipher.ENCRYPT_MODE;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			byte[] keyData = { 1, 9, 8, 2, 0, 8, 2, 1 };
			DESKeySpec keySpec = new DESKeySpec(keyData);
			Key key = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(mode, key);
			byte[] result = cipher.doFinal(byteSource);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			baos.close();
		}
	}

	/**
	 * 瀵圭О瑙ｅ瘑鏂规硶
	 * 
	 * @param byteSource
	 *            闇�瑙ｅ瘑鐨勬暟鎹�?
	 * @return 缁忚繃瑙ｅ瘑鐨勬暟鎹�?
	 * @throws Exception
	 */
	public static byte[] symmetricDecrypto(byte[] byteSource) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			int mode = Cipher.DECRYPT_MODE;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			byte[] keyData = { 1, 9, 8, 2, 0, 8, 2, 1 };
			DESKeySpec keySpec = new DESKeySpec(keyData);
			Key key = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(mode, key);
			byte[] result = cipher.doFinal(byteSource);
			return result;
		} catch (Exception e) {
			// throw e;
			return null;
		} finally {
			baos.close();
		}
	}

	/**
	 * 鏁ｅ垪绠楁硶
	 * 
	 * @param byteSource
	 *            闇�鏁ｅ垪璁＄畻鐨勬暟鎹�
	 * @return 缁忚繃鏁ｅ垪璁＄畻鐨勬暟鎹�
	 * @throws Exception
	 */
	public static byte[] hashMethod(byte[] byteSource) throws Exception {
		try {
			MessageDigest currentAlgorithm = MessageDigest.getInstance("SHA-1");
			currentAlgorithm.reset();
			currentAlgorithm.update(byteSource);
			return currentAlgorithm.digest();
		} catch (Exception e) {
			throw e;
		}
	}

	/**** md5加密 ***/
	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
}
