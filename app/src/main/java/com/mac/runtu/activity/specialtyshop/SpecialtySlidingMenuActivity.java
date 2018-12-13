package com.mac.runtu.activity.specialtyshop;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.mac.runtu.R;
import com.mac.runtu.fragment.SpecialtyContentFragment;
import com.mac.runtu.fragment.SpecialtyLeftFilterFragment;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;

/**
 * 带侧边栏的特产电商   主
 */
public class SpecialtySlidingMenuActivity extends SlidingFragmentActivity {
    //fragment标记
    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private static final String TAG_CONTENT   = "TAG_CONTENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_slidingmenu_count);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //屏幕高度
        int heightPixels = metrics.heightPixels;
        //屏幕宽度
        int widthPixels = metrics.widthPixels;
        //设置侧边栏布局
        setBehindContentView(R.layout.left_menu);

        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setFadeDegree(0.35f);
        //slidingMenu.setFadeEnabled(true);
        //slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏触摸
        slidingMenu.setBehindOffset(widthPixels * 1 / 5);//屏幕剩余宽度
        initFragment();

        //侧边栏划出 开始就初始化
        MarkerConstants.value_mode_filter_4one = GlobalConstants.value_mode_filter_specialty;
    }

    //初始化fragment
    private void initFragment() {
        //获取fragmentManager对象
        FragmentManager fm = getSupportFragmentManager();
        //开始事务
        FragmentTransaction transaction = fm.beginTransaction();

        transaction
                .replace(R.id.fl_content, new SpecialtyContentFragment(),
                        TAG_CONTENT);
        transaction.replace(R.id.fl_left_menu, new
                        SpecialtyLeftFilterFragment(),
                TAG_LEFT_MENU);

        transaction.commit();

    }

    //获取侧边栏fragment
    public SpecialtyLeftFilterFragment getLeftMenuFragment() {
        //获取fragmentManager对象
        FragmentManager fm = getSupportFragmentManager();
        SpecialtyLeftFilterFragment fragment = (SpecialtyLeftFilterFragment) fm
                .findFragmentByTag(TAG_LEFT_MENU);
        return fragment;
    }

    //获取主页面fragment
    public SpecialtyContentFragment getContentFragment() {
        //获取fragmentManager对象
        FragmentManager fm = getSupportFragmentManager();
        SpecialtyContentFragment fragment = (SpecialtyContentFragment) fm
                .findFragmentByTag(TAG_CONTENT);
        return fragment;
    }

}
