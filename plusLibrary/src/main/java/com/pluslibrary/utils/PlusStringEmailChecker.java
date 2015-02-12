package com.pluslibrary.utils;

/**
 * 정상적인 이메일 인지 검증
 */
import java.util.regex.Pattern;

public class PlusStringEmailChecker {

	public static boolean doIt(String email) {
		if (email == null || email.equals(""))
			return false;
		boolean b = Pattern.matches(
				"[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", email.trim());
		return b;
	}
}
