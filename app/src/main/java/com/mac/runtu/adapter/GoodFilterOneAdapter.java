package com.mac.runtu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.javabean.GoodFilterInfo;

import java.util.ArrayList;

/**
 * Description: 筛选一级
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/12 0012 上午 11:35
 */
public class GoodFilterOneAdapter extends BaseAdapter {

    private Context                   context;
    private ArrayList<GoodFilterInfo> list;

    public GoodFilterOneAdapter(Context context,
                                ArrayList<GoodFilterInfo>
                                        list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GoodFilterInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout
                    .item_filter_list_view_one, null);

            holder.tvName = (TextView) convertView.findViewById(R.id
                    .tv_shuiguo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(getItem(position).typeName);
        holder.tvName.setSelected(getItem(position).isSelected);

        return convertView;
    }

    static class ViewHolder {
        TextView tvName;
    }
}
