/** */
package com.example.springboottest.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @功能:
 * @项目名:myspringboot
 * @作者:wangjz
 * @日期:2018年6月22日下午2:23:54
 */
public class NoteUtil {
	/*
	 * MD5加密:摘要算法
	 * 特点:任意长度字节处理成等长结果;不可逆
	 * Base64:a-z A-Z 0-9 = +
	 */
	public static String md5(String src){
		try {
			MessageDigest md=MessageDigest.getInstance("MD5");
			byte[] output=md.digest(src.getBytes());
			//return new String(output);
			String ret=Base64.encodeBase64String(output);
			return ret;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return src;
	}
	//UUID生成ID
	public static String createId(){
		String id=UUID.randomUUID().toString();
		return id;
	}
	public static void main(String[] args) {
		System.out.println(createId());
		System.out.println(md5("123"));
		System.out.println(md5("123456"));
		System.out.println(md5("123").length());
		System.out.println(md5("123456").length());
		
	}
}
