package com.mac.runtu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mac.runtu.holder.BaseHolder;

import java.util.ArrayList;



/**
 * Description: 适配器的基类
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/9/21 0021 上午 10:39
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    private ArrayList<T> data;
    private Context context;

    public MyBaseAdapter(Context context ,ArrayList<T> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BaseHolder holder = null;
        if (convertView == null) {
            holder = getHolder(context);
        } else {
            holder = (BaseHolder) convertView.getTag();

        }
        holder.setData(getItem(position),position);

        return holder.convertView;


    }

    /**
     * 加载 holder
     * @return
     */
    public abstract BaseHolder getHolder(Context context);
}
