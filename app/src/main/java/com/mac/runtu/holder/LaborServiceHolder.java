package com.mac.runtu.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.javabean.LaborServiceDetailInfo;
import com.mac.runtu.utils.TimeUtils;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/27 1:56
 */
public class LaborServiceHolder extends BaseHolder<LaborServiceDetailInfo> {

    private TextView tvConent;
    private TextView tvTime;

    public LaborServiceHolder(Context context) {
        super(context);

    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout
                .labor_service_cooperation_list_item_layout, null);


        tvConent = (TextView) view.findViewById(R.id.content_Tv);
        tvTime = (TextView) view.findViewById(R.id.time_Tv);
        return view;
    }

    @Override
    public void refreshView(int position) {
        if (!TextUtils.isEmpty(data.title)) {
            tvConent.setText(data.title);
            tvTime.setText(TimeUtils.setCreatTime(data.createTime));
        }
    }
}
