package com.jbm.game.tool.util;

import java.util.regex.Pattern;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年8月4日 下午2:36:16
 */
public class StringUtil {
	// 判断整数（int）

	public static boolean isInteger(String str) {
		if (null == str || "".equals(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	// 判断浮点数（double和float ）
	public static boolean isDouble(String str) {
		if (null == str || "".equals(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 首字母大写
	 * 
	 * @param Str
	 * @return
	 */
	public static String upFirstChar(String Str) {
		char[] cs = Str.toCharArray();
		if (Character.isLowerCase(cs[0])) {
			cs[0] -= 32;
			return String.valueOf(cs);
		}
		return Str;
	}

	/**
	 * 首字母转小写
	 */
	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0))) {
			return s;
		}

		return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}
}
