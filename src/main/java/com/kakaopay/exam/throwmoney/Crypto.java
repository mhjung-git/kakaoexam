package com.kakaopay.exam.throwmoney;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

	public Crypto() {
		
	}
	
	// byte[] to hex
	public static String byteArrayToHex(byte[] ba) {
		if (ba == null || ba.length == 0) {
			return null;
	    }
	
	    StringBuffer sb = new StringBuffer(ba.length * 2);
	    String hexNumber;
	    for (int x = 0; x < ba.length; x++) {
	            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
	            sb.append(hexNumber.substring(hexNumber.length() - 2));
	    }
	    return sb.toString();
	}
	  
	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
	                return null;
	    }
	
	    byte[] ba = new byte[hex.length() / 2];
	    for (int i = 0; i < ba.length; i++) {
	            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
	    }
	    
	    return ba;
	}
	  
	public static String Encrypt(String InKey, String InValue) throws Exception {

	    if (InValue == null) return null;
	    if (InKey == null) return InValue;

	    String LS_Encrypt = null;

	    InKey = byteArrayToHex(InKey.getBytes());
	    byte[] keyString = hexToByteArray(EncInfo.PubCONST_KEY+InKey) ;
	    byte[] iv= hexToByteArray(EncInfo.PubCONST_IV);
	    IvParameterSpec ivSpec = new IvParameterSpec(iv);

	    SecretKeySpec key = new SecretKeySpec(keyString, "AES");

	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

	    cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
	    byte[] LB_EncryptValue = cipher.doFinal(InValue.getBytes("EUC-KR"));
	    LS_Encrypt = byteArrayToHex(LB_EncryptValue);
	    //System.out.println("encrypted: "+LS_Encrypt);

	    return LS_Encrypt;
	  }
	
	  public static String Decrypt(String InKey, String InValue) throws Exception {

		    if (InValue == null) return null;
		    if (InKey == null) return InValue;

		    String LS_Decrypt = null;

		    InKey = byteArrayToHex(InKey.getBytes());
		    byte[] keyString = hexToByteArray(EncInfo.PubCONST_KEY+InKey) ;
		    byte[] iv= hexToByteArray(EncInfo.PubCONST_IV);
		    IvParameterSpec ivSpec = new IvParameterSpec(iv);

		    SecretKeySpec key = new SecretKeySpec(keyString, "AES");

		    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		    cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		    byte[] LB_Value = hexToByteArray(InValue);
		    byte[] LB_DecryptValue = cipher.doFinal(LB_Value);
		    LS_Decrypt = new String(LB_DecryptValue, "euc-kr");
		    //System.out.println("decrypted : ["+byteArrayToHex(LB_DecryptValue)+"]");
		    //System.out.println("destination : ["+LS_Decrypt+"]");

		    return LS_Decrypt;
	  }
}
