package com.mac.runtu.activity.experiencefarm;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.mac.runtu.R;
import com.mac.runtu.fragment.ExperienceFarmContentFragment;
import com.mac.runtu.fragment.SpecialtyLeftFilterFragment;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;

/**
 * 体验农场
 */
public class ExperienceFarmSlidingMenuActivity extends SlidingFragmentActivity {

    private static final String TAG = "ExperienceFarmSlidingMenuActivity";

    //fragment标记
    private static final String TAG_LEFT_MENU          = "TAG_LEFT_MENU";
    private static final String TAG_EXPER_FARM_CONTENT =
            "TAG_EXPER_FARM_CONTENT";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_slidingmenu_count);

        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //屏幕高度
        int heightPixels = metrics.heightPixels;
        //屏幕宽度
        int widthPixels = metrics.widthPixels;
        //设置侧边栏布局
        setBehindContentView(R.layout.left_menu);

        SlidingMenu slidingMenu = getSlidingMenu();


        //slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏触摸
        slidingMenu.setBehindOffset(widthPixels * 1 / 5);//屏幕剩余宽度
        initFragment();

        //侧边栏划出 开始就初始化
        MarkerConstants.value_mode_filter_4one = GlobalConstants
                .value_mode_filter_farm;


    }

    //初始化fragment
    private void initFragment() {
        //获取fragmentManager对象
        FragmentManager fm = getSupportFragmentManager();
        //开始事务
        FragmentTransaction transaction = fm.beginTransaction();

        transaction
                .replace(R.id.fl_content, new ExperienceFarmContentFragment(),
                        TAG_EXPER_FARM_CONTENT);
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
    public ExperienceFarmContentFragment getContentFragment() {
        //获取fragmentManager对象
        FragmentManager fm = getSupportFragmentManager();
        ExperienceFarmContentFragment fragment =
                (ExperienceFarmContentFragment) fm
                .findFragmentByTag(TAG_EXPER_FARM_CONTENT);
        return fragment;
    }

}
