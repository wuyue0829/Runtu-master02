package com.mac.runtu.utils;

import android.content.Context;

public class DensityUtils {

	//dp转px
	public static int dp2px(Context ctx, float dp) {
		float density = ctx.getResources().getDisplayMetrics().density;
		//1.9->1
		int px = (int) (dp * density + 0.5f);//加0.5四舍五入

		return px;
	}

	//px转dp
	public static float px2dp(Context ctx, int px) {
		float density = ctx.getResources().getDisplayMetrics().density;
		float dp = px / density;

		return dp;
	}

}
