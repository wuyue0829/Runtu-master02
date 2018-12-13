package com.mac.runtu.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {

	private static final String FILENAME = "config";

	private static SharedPreferences sp = null;

	public static SharedPreferences getSharedPreferences(Context context) {

		if (sp == null) {
			sp = context.getSharedPreferences(FILENAME, context.MODE_PRIVATE);
		}
		return sp;
	}


	// 存取String
	public static void setString(Context context, String key, String value) {

		getSharedPreferences(context).edit().putString(key, value).commit();
	}

	public static String getString(Context context, String key, String defValue) {

		return getSharedPreferences(context).getString(key, defValue);
	}

	// 存取Iint
	public static void setInt(Context context, String key, int value) {

		getSharedPreferences(context).edit().putInt(key, value).commit();
	}

	public static int getInt(Context context, String key, int defValue) {

		return getSharedPreferences(context).getInt(key, defValue);
	}

	// 存boolean
	public static void setBoolean(Context context, String key, boolean value) {

		getSharedPreferences(context).edit().putBoolean(key, value).commit();
	}

	public static boolean getBoolean(Context context, String key,
			boolean defValue) {

		return getSharedPreferences(context).getBoolean(key, defValue);
	}

	//通过建删除一个数据
	public static void remove(Context context, String key) {

		getSharedPreferences(context).edit().remove(key).commit();
	}


}
