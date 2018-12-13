package com.mac.runtu.adapter;

import android.content.Context;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.holder.LogisticsHolder;
import com.mac.runtu.javabean.LogisticsDetailInfo;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/28 0028 下午 3:44
 */
public class LogisticsAdapter extends MyBaseAdapter<LogisticsDetailInfo> {
    public LogisticsAdapter(Context context, ArrayList<LogisticsDetailInfo>
            data) {
        super(context, data);
    }

    @Override
    public BaseHolder getHolder(Context context) {

        return new LogisticsHolder(context);
    }
}
