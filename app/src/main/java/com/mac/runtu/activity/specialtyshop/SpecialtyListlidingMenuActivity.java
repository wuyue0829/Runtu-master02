package com.mac.runtu.activity.specialtyshop;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.mac.runtu.R;
import com.mac.runtu.fragment.SpecialtyLeftFilterFragment;
import com.mac.runtu.fragment.SpecialtyListFragment;

/**
 * 列表页
 */
public class SpecialtyListlidingMenuActivity extends SlidingFragmentActivity {

    //fragment标记
    private static final String TAG_LEFT_MENU    = "TAG_LEFT_MENU";
    private static final String TAG_LIST_CONTENT = "TAG_SPECIALTY_LIST_CONTENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialty_listliding_menu);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //屏幕高度
        int heightPixels = metrics.heightPixels;
        //屏幕宽度
        int widthPixels = metrics.widthPixels;


        //设置侧边栏布局
        setBehindContentView(R.layout.left_menu);

        SlidingMenu slidingMenu = getSlidingMenu();
        //表示右都有侧边栏
        slidingMenu.setMode(SlidingMenu.RIGHT);

        // slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 全屏触摸
        slidingMenu.setBehindOffset(widthPixels * 1 / 5);//屏幕剩余宽度
        initFragment();

    }

    //初始化fragment
    private void initFragment() {
        //获取fragmentManager对象
        FragmentManager fm = getSupportFragmentManager();
        //开始事务
        FragmentTransaction transaction = fm.beginTransaction();

        transaction
                .replace(R.id.fl_specialty_list_content, new
                                SpecialtyListFragment(),
                        TAG_LIST_CONTENT);
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
    public SpecialtyListFragment getContentFragment() {
        //获取fragmentManager对象
        FragmentManager fm = getSupportFragmentManager();
        SpecialtyListFragment fragment = (SpecialtyListFragment) fm
                .findFragmentByTag(TAG_LIST_CONTENT);
        return fragment;
    }
}

