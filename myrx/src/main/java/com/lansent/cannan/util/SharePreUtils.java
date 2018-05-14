/**
 * 
 */
package com.lansent.cannan.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * 操作{@link android.content.SharedPreferences}配置信息工具类
 *
 * @author YinJ 2015-1-23
 */
public class SharePreUtils {

	private static String FILE_NAME = "cannan_file";

	/**
	 *
	 * 保存String类型的配置信息
	 *
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveStrConfig(Context context, String key, String value) {
		SharedPreferences preferences = context.getSharedPreferences(
				FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		if (value == null) {
			editor.putString(key, "");
		} else {
			editor.putString(key, value.toString());
		}
		editor.commit();
	}

	/**
	 * 获取自定义的String类型的配置信息，自定义默认返回值
	 *
	 * @param key
	 * @param defValue
	 *            默认返回值
	 * @return
	 */
	public static String getStrConfig(Context context, String key,
									  String defValue) {
		SharedPreferences preferences = context.getSharedPreferences(
				FILE_NAME, Context.MODE_PRIVATE);
		return preferences.getString(key, defValue);
	}

	/**
	 * 获取自定义的String类型的配置信息,使用默认返回值""
	 *
	 * @param context
	 * @param key
	 * @return 如果没有此配置信息，默认返回空字符串
	 */
	public static String getStrConfig(Context context, String key) {
		return getStrConfig(context, key, "");
	}

	/**
	 * 保存Int类型的配置信息
	 *
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveIntConfig(Context context, String key, Integer value) {
		SharedPreferences preferences = context.getSharedPreferences(
				FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		if (value == null) {
			return;
		}
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 *
	 * 获取Int类型的配置信息,自定义默认的返回值
	 *
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getIntConfig(Context context, String key, int defValue) {
		SharedPreferences preferences = context.getSharedPreferences(
				FILE_NAME, Context.MODE_PRIVATE);
		return preferences.getInt(key, defValue);
	}

	/**
	 *
	 * 获取Int类型的配置信息,使用默认的返回值0
	 *
	 * @param context
	 * @param key
	 * @param
	 * @return 如果没有此配置信息，默认返回 0
	 */
	public static int getIntConfig(Context context, String key) {
		return getIntConfig(context, key, 0);
	}

	/**
	 * 保存布尔类型的配置信息
	 *
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveBoolConfig(Context context, String key, Boolean value) {
		SharedPreferences preferences = context.getSharedPreferences(
				FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		if (value == null) {
			return;
		}
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 读取布尔类型的配置信息，自定义默认返回值
	 *
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolConfig(Context context, String key,
										boolean defValue) {
		SharedPreferences preferences = context.getSharedPreferences(
				FILE_NAME, Context.MODE_PRIVATE);
		return preferences.getBoolean(key, defValue);
	}

	/**
	 * 读取布尔类型的配置信息,使用默认返回值
	 *
	 * @param context
	 * @param key
	 * @param
	 * @return 如果没有此配置信息，默认返回 false
	 */
	public static boolean getBoolConfig(Context context, String key) {
		return getBoolConfig(context, key, false);
	}

	/**
	 * 移除某个key值已经对应的值
	 *
	 * @param context
	 * @param key
	 */
	public static void removeConfig(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key).commit();
	}

	/**
	 * 清除所有配置信息
	 *
	 * @param context
	 */
	public static void clearAllConfig(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear().commit();
	}

	/**
	 * 查询某个key配置信息是否已经存在
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean contains(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.contains(key);
	}
}
