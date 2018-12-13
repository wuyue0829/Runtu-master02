package com.mac.runtu.holder;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/9/21 0021 上午 11:03
 */


import android.content.Context;
import android.view.View;



public abstract class BaseHolder<T> {

    public View convertView;

    public T data;

    public Context context;

    public BaseHolder( Context context) {
        this.context = context;
        //加载布局
        convertView = initView();
        convertView.setTag(this);
    }

    public abstract View initView();

    public void setData(T data, int position) {
        this.data = data;
        //刷新数据
        refreshView(position);

    }

    public abstract void refreshView(int position);
}
