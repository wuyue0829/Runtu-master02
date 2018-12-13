package com.mac.runtu.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.javabean.LaborServiceDetailInfo;
import com.mac.runtu.utils.TimeUtils;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/3 0003 上午 10:02
 */
public class LaborServiceMapHolder extends BaseHolder<LaborServiceDetailInfo> {

    private TextView tvTitle;
    private TextView tvAddress;
    private TextView tvTime;

    public LaborServiceMapHolder(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.map_marker_pop_layout, null);


        tvTitle = (TextView) view.findViewById(R.id.title_Tv);
        tvAddress = (TextView) view.findViewById(R.id
                .address_Tv);
        tvTime = (TextView) view.findViewById(R.id.tv_time);

        return view;
    }

    @Override
    public void refreshView(int position) {
        tvTime.setText(TimeUtils.setCreatTime(data.labourTime));
        tvTitle.setText(data.title);
        tvAddress.setText(data.city + "-" + data.county + "-" + data.town +
                "-" + data.address);
    }
}
