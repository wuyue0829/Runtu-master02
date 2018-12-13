package com.mac.runtu.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;

import java.util.List;

/**
 * 判断服务是否运行
 */
public class ServiceStatusUtils {

	public static boolean isServiceRunning(Context ctx,
			Class<? extends Service> clazz) {
		//活动管理器
		ActivityManager am = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		//获取正在运行的所有服务
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);//返回服务的最大个数

		for (RunningServiceInfo runningServiceInfo : runningServices) {
			//System.out.println(runningServiceInfo.service.getClassName());
			if (runningServiceInfo.service.getClassName().equals(
					clazz.getName())) {//判断正在运行的服务类名是否和传进来的类名一致,如果一致,说明该服务正在运行
				return true;
			}

		}

		return false;
	}
}
