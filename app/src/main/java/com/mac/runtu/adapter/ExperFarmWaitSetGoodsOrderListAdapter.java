package com.mac.runtu.adapter;

import android.content.Context;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.holder.ExperFarmWaitSetGoodOrderHolder;
import com.mac.runtu.javabean.UserCenertOrderDetailInfo;

import java.util.ArrayList;

/**
 * Description:待收货
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/23 0:29
 */
public class ExperFarmWaitSetGoodsOrderListAdapter extends
        MyBaseAdapter<UserCenertOrderDetailInfo> {


    public ExperFarmWaitSetGoodsOrderListAdapter(Context context,
                                                 ArrayList<UserCenertOrderDetailInfo> data) {
        super(context, data);
    }

    @Override
    public BaseHolder getHolder(Context context) {
        return new ExperFarmWaitSetGoodOrderHolder(context);
    }
}
