package com.cwang.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Convert {
	
	/**
	 * 对输入字符串进行MD5转换
	 * @param text	输入字符串
	 * @param mod	16/32
	 * @return	MD5转换后的字符串
	 */
	public static String ConvertMD5(String plainText, int mod){
		try {  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            md.update(plainText.getBytes());  
            byte b[] = md.digest();  
  
            int i;  
  
            StringBuffer buf = new StringBuffer("");  
            for (int offset = 0; offset < b.length; offset++) {  
                i = b[offset];  
                if (i < 0)  
                    i += 256;  
                if (i < 16)  
                    buf.append("0");  
                buf.append(Integer.toHexString(i));  
            }  
            if(16 == mod){
            	return buf.toString().toUpperCase().substring(8, 24);  
            }else{
            	return buf.toString().toUpperCase();  
            }
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
            return null;  
        }  
	}
	
	
	public static void main(String[] args){
		String text = ConvertMD5("123456",32);
		System.out.println(text);
	}
}
