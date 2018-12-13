package com.mac.runtu.adapter;

import android.content.Context;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.holder.LaborServiceHolder;
import com.mac.runtu.javabean.LaborServiceDetailInfo;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/27 1:55
 */
public class LaborServiceAdapter extends MyBaseAdapter<LaborServiceDetailInfo> {

    public LaborServiceAdapter(Context context,
                               ArrayList<LaborServiceDetailInfo> data) {
        super(context, data);
    }

    @Override
    public BaseHolder getHolder(Context context) {
        return new LaborServiceHolder(context);
    }
}
