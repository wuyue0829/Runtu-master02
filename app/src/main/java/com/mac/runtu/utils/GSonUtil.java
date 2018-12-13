package com.mac.runtu.utils;

import com.google.gson.Gson;

public class GSonUtil {

	public static Gson gSon;

	public static <T> T parseJson(String json, Class<T> clazz) {

		if (gSon == null) {
			gSon = new Gson();
		}

		return gSon.fromJson(json, clazz);
	}

	public static String obj2json(Object obj){
		if (gSon == null) {
			gSon = new Gson();
		}
		return gSon.toJson(obj);

	}
}
