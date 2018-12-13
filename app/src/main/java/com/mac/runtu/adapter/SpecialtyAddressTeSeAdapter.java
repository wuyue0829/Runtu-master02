package com.mac.runtu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.holder.SpecialtyAddressTeSeHolder;
import com.mac.runtu.javabean.SpecialtyGoodDetailInfo;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/5 0005 下午 6:29
 */
public class SpecialtyAddressTeSeAdapter extends BaseAdapter {

    private ArrayList<SpecialtyGoodDetailInfo> data;
    private Context                            context;

    public SpecialtyAddressTeSeAdapter(Context context,
                                       ArrayList<SpecialtyGoodDetailInfo>
                                               data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        //只显示六条
        if (data.size() > 6) {
            return 6;
        } else {
            return data.size();
        }

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
            //加载holer
            holder = new SpecialtyAddressTeSeHolder(context);
        } else {
            holder = (BaseHolder) convertView.getTag();

        }
        holder.setData(getItem(position), position);

        return holder.convertView;

    }

}
