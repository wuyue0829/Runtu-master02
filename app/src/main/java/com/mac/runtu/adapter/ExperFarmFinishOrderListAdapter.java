package com.mac.runtu.adapter;

import android.content.Context;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.holder.ExperFarmFinishOrderHolder;
import com.mac.runtu.javabean.UserCenertOrderDetailInfo;

import java.util.ArrayList;

/**
 * Description: 已完成 体验农场
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/23 0:29
 */
public class ExperFarmFinishOrderListAdapter extends
        MyBaseAdapter<UserCenertOrderDetailInfo> {


    public ExperFarmFinishOrderListAdapter(Context context,
                                           ArrayList<UserCenertOrderDetailInfo> data) {
        super(context, data);
    }

    @Override
    public BaseHolder getHolder(Context context) {
        return new ExperFarmFinishOrderHolder(context);
    }
}
