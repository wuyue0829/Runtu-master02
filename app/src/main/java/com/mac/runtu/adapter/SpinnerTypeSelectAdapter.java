package com.mac.runtu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mac.runtu.javabean.SelectTypeInfo;

import java.util.List;

public class SpinnerTypeSelectAdapter extends android.widget.BaseAdapter {

    private List<SelectTypeInfo.SelecttypeDetailInfo> list;
    private Context                                   context;

    public SpinnerTypeSelectAdapter(Context context, List<SelectTypeInfo
            .SelecttypeDetailInfo> list) {

        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {

        return list.size();

    }

    @Override
    public SelectTypeInfo.SelecttypeDetailInfo getItem(int position) {

        return list.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView tv = null;

        if (convertView == null) {
            //MainActivity.this MainActivity对象
            tv = new TextView(context);
            tv.setSingleLine();

            tv.setTextSize(15);
        } else {

            tv = (TextView) convertView;

        }

        tv.setText(getItem(position).kindName);
        return tv;
    }

}