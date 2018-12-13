package com.mac.runtu.adapter;

import android.content.Context;

import com.mac.runtu.holder.AgricMachineryHolder;
import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.javabean.AgricuMachineryDetailInfo;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/26 23:36
 */
public class AgricMachineryAdapter extends MyBaseAdapter<AgricuMachineryDetailInfo> {
    public AgricMachineryAdapter(Context context, ArrayList<AgricuMachineryDetailInfo> data) {
        super(context, data);
    }

    @Override
    public BaseHolder getHolder(Context context) {
        return new AgricMachineryHolder(context);
    }
}
