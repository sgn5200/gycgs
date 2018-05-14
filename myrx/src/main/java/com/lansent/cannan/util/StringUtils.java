/**
 *      
 */
package com.lansent.cannan.util;

import android.annotation.SuppressLint;
import android.text.Html;
import android.text.Spanned;

/**
 * 字符串工具
 */
@SuppressLint("SimpleDateFormat")
public class StringUtils {

	/**
	 * 判断字符串是否为null或全为空格
	 *
	 * @param s 待校验字符串
	 * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
	 */
	public static boolean isEmpty(final String s) {
		return (s == null || s.trim().length() == 0);
	}

	/**
	 * 判断两字符串忽略大小写是否相等
	 *
	 * @param a 待校验字符串a
	 * @param b 待校验字符串b
	 * @return {@code true}: 相等<br>{@code false}: 不相等
	 */
	public static boolean equalsIgnoreCase(final String a, final String b) {
		return a == null ? b == null : a.equalsIgnoreCase(b);
	}

	/**
	 * 返回字符串长度
	 *
	 * @param s 字符串
	 * @return null返回0，其他返回自身长度
	 */
	public static int length(final CharSequence s) {
		return s == null ? 0 : s.length();
	}

	/**
	 * 转换网页内容
	 * 
	 * @return
	 */
	public static String transfWebContent(String content) {
		if (content == null) {
			return "";
		}
		Spanned contentTemp = Html.fromHtml(content);
		String contentStr = contentTemp.toString();
		return contentStr;
	}

	/**
	 * 隐藏手机号中间四位数
	 * @param mobile
	 *            11位手机号码
	 * @return 中间四位使用"****"表示的手机号
	 */
	public static String getHidenMobile(String mobile) {
		if (mobile.length() < 8) {
			throw new RuntimeException("传入的手机号长度不正确");
		}
		return mobile.replace(mobile.subSequence(3, 7), "****");
	}

	/**
	 * 返回出生日期数组
	 * @param identity
	 * @return String[]
	 **/
	public static String[] getIdentityDate(String identity) {
		String[] mStrings = new String[3];
		mStrings[0] = identity.substring(6, 10);
		mStrings[1] = identity.substring(10, 12);
		mStrings[2] = identity.substring(12, 14);
		return mStrings;
	}
}
