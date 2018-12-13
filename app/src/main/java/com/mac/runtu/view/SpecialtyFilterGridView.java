package com.mac.runtu.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/5 0005 下午 3:30
 */
public class SpecialtyFilterGridView extends GridView {

    private int startX;
    private int startY;

    public SpecialtyFilterGridView(Context context) {
        super(context);
    }

    public SpecialtyFilterGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpecialtyFilterGridView(Context context, AttributeSet attrs, int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpecialtyFilterGridView(Context context, AttributeSet attrs, int
            defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求父控件及祖宗控件不拦截事件
        //解决bug: 头条新闻触摸事件被页签的ViewPager拦截了, 无法内部滑动
        //一开始,先拿到事件, 然后判断需不需要拦截
        getParent().requestDisallowInterceptTouchEvent(true);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();

                int dx = moveX - startX;
                int dy = moveY - startY;


                if (Math.abs(dx) > Math.abs(dy)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {//上下滑动,需要拦截
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;

            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
