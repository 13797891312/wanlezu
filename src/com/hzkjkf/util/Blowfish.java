package com.hzkjkf.util;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.hzkjkf.adtask.QuestionGMOAcitvity;
import com.hzkjkf.javabean.QuestionBean_GMO;
/**
 * 
 * GMO参数加密
 *
 */
public class Blowfish {
    /**
     * 
     * @param phone 手机号的后8位
     * @return 加密后的参数
     * @throws Exception
     */
    public static String getcrytValue(String phone) throws Exception{
    	String bString  = QuestionGMOAcitvity.PANEL;
    	String cString  = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) ;
        return encrypt("27035656"+":"+bString+":"+cString);
    }


    /** ALGORITHM */
    private static final String ALGORITHM = "Blowfish";
    /** KEY */
    private static final String KEY = "N67SAxUb8fNRjYTd";//fs5uhnjcnpxcpg9g

    /**
     * encrypt
     *
     * @param input
     * @return cipher text
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws NoSuchPaddingException
     */
    public static String encrypt(final String input) throws
                                        IllegalBlockSizeException,
                                        InvalidKeyException,
                                        NoSuchAlgorithmException,
                                        BadPaddingException,
                                        NoSuchPaddingException {

        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, KEY);
        return new String(Hex.encodeHex(cipher.doFinal(input.getBytes())));
    }

    /**
     * decrypt
     *
     * @param input
     * @return plain text
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws NoSuchPaddingException
     * @throws DecoderException
     */
    public static String decrypt(final String input) throws
                                        IllegalBlockSizeException,
                                        InvalidKeyException,
                                        NoSuchAlgorithmException,
                                        BadPaddingException,
                                        NoSuchPaddingException,
                                        DecoderException {

        byte[] encrypted = Hex.decodeHex(input.toCharArray());
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, KEY);
        return new String(cipher.doFinal(encrypted));
    }

    /**
     * create Cipher object
     *
     * @param mode
     * @param key
     * @return Cipher object
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     */
    private static Cipher getCipher(final int mode, final String key) throws
                                        NoSuchAlgorithmException,
                                        NoSuchPaddingException,
                                        InvalidKeyException {

        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, skeySpec);
        return cipher;
    }

    /**
     * create Md5
     *
     * @param input
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return String.valueOf(Hex.encodeHex(md5.digest(input.getBytes())));
    }

}
