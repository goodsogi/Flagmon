package com.pluslibrary.utils;

import java.io.UnsupportedEncodingException;

public class PlusEncodingFinder {
	public static void doIt(String s) {
		String word = s;
		try {
			PlusLogger.doIt("utf-8(1) : " + new String(word.getBytes("utf-8"), "euc-kr"));
			PlusLogger.doIt("utf-8(2) : " + new String(word.getBytes("utf-8"), "ksc5601"));
			PlusLogger.doIt("utf-8(3) : " + new String(word.getBytes("utf-8"), "x-windows-949"));
			PlusLogger.doIt("utf-8(4) : " + new String(word.getBytes("utf-8"), "iso-8859-1"));
			 
			PlusLogger.doIt("iso-8859-1(1) : " + new String(word.getBytes("iso-8859-1"), "euc-kr"));
			PlusLogger.doIt("iso-8859-1(2) : " + new String(word.getBytes("iso-8859-1"), "ksc5601"));
			PlusLogger.doIt("iso-8859-1(3) : " + new String(word.getBytes("iso-8859-1"), "x-windows-949"));
			PlusLogger.doIt("iso-8859-1(4) : " + new String(word.getBytes("iso-8859-1"), "utf-8"));
			 
			PlusLogger.doIt("euc-kr(1) : " + new String(word.getBytes("euc-kr"), "ksc5601"));
			PlusLogger.doIt("euc-kr(2) : " + new String(word.getBytes("euc-kr"), "utf-8"));
			PlusLogger.doIt("euc-kr(3) : " + new String(word.getBytes("euc-kr"), "x-windows-949"));
			PlusLogger.doIt("euc-kr(4) : " + new String(word.getBytes("euc-kr"), "iso-8859-1"));
			 
			PlusLogger.doIt("ksc5601(1) : " + new String(word.getBytes("ksc5601"), "euc-kr"));
			PlusLogger.doIt("ksc5601(2) : " + new String(word.getBytes("ksc5601"), "utf-8"));
			PlusLogger.doIt("ksc5601(3) : " + new String(word.getBytes("ksc5601"), "x-windows-949"));
			PlusLogger.doIt("ksc5601(4) : " + new String(word.getBytes("ksc5601"), "iso-8859-1"));
			 
			PlusLogger.doIt("x-windows-949(1) : " + new String(word.getBytes("x-windows-949"), "euc-kr"));
			PlusLogger.doIt("x-windows-949(2) : " + new String(word.getBytes("x-windows-949"), "utf-8"));
			PlusLogger.doIt("x-windows-949(3) : " + new String(word.getBytes("x-windows-949"), "ksc5601"));
			PlusLogger.doIt("x-windows-949(4) : " + new String(word.getBytes("x-windows-949"), "iso-8859-1"));
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
