package com.pluslibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 시간을 오전/오후 00:00 형식으로 변환
 * 
 */

public class PlusTimeFormatter {

	public static String doIt() {
		SimpleDateFormat sdf = new SimpleDateFormat("a hh:mm");
		String currentDate = sdf.format(new Date());

		return currentDate;
	}

	public static String doItLong() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 E요일");
		String currentDate = sdf.format(new Date());

		return currentDate;
	}

	public static String getFormattedTime(String time) {

		Calendar calendar = new GregorianCalendar();
		Date date = null;

		try {
			date = new SimpleDateFormat("yyyyMMddhhmmss").parse(time);

		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		calendar.setTime(date);

		return calendar.get(Calendar.YEAR) + "년" + calendar.get(Calendar.MONTH)
				+ "월" + calendar.get(Calendar.DAY_OF_MONTH) + "일"
				+ calendar.get(Calendar.HOUR) + "시"
				+ calendar.get(Calendar.MINUTE) + "분";
	}

	public static long getMilliSec(String time) {

		String[] tokens = time.split(":");
		int milliSecHour = (isNumeric(tokens[0]) ? Integer.parseInt(tokens[0])
				: 0) * 60 * 60 * 1000;
		int millSecMin = (isNumeric(tokens[1]) ? Integer.parseInt(tokens[1])
				: 0) * 60 * 1000;
		int milliSecSec = (isNumeric(tokens[2]) ? Integer.parseInt(tokens[2])
				: 0) * 1000;
		return milliSecHour + millSecMin + milliSecSec;

//		 Calendar calendar = new GregorianCalendar();
//		 Date date = null;
//		
//		 try {
//		 date = new SimpleDateFormat("'T'H:mm:ss").parse(time);
//		
//		 } catch (java.text.ParseException e) {
//		 // TODO Auto-generated catch block
//		 e.printStackTrace();
//		 }
//		
//		 calendar.setTime(date);
//		
//		 return calendar.getTimeInMillis();
	}

	private static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static String getFormattedTime(int year, int month, int day) {

		StringBuilder timeForServer = new StringBuilder().append(year)
				.append((month + 1 >= 10) ? month + 1 : "0" + (month + 1))
				.append(day >= 10 ? day : "0" + day);

		return timeForServer.toString();
	}
}
