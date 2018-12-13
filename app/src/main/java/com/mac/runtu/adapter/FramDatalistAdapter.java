package com.mac.runtu.adapter;

import android.content.Context;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.holder.FramDataListHolder;
import com.mac.runtu.javabean.BusinessDataDetailInfo;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/25 0025 下午 3:12
 */
public class FramDatalistAdapter extends MyBaseAdapter<BusinessDataDetailInfo> {

    public FramDatalistAdapter(Context context, ArrayList data) {
        super(context, data);
    }

    @Override
    public BaseHolder getHolder(Context context) {
        return new FramDataListHolder(context);
    }
}
