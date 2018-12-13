package com.mac.runtu.adapter;

import android.content.Context;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.holder.ExperFarmWaitPayOrderHolder;
import com.mac.runtu.javabean.UserCenertOrderDetailInfo;

import java.util.ArrayList;

/**
 * Description:体验农场待付款
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/23 0:29
 */
public class ExperFarmWaitPayOrderListAdapter extends
        MyBaseAdapter<UserCenertOrderDetailInfo> {


    public ExperFarmWaitPayOrderListAdapter(Context context,
                                            ArrayList<UserCenertOrderDetailInfo> data) {
        super(context, data);
    }

    @Override
    public BaseHolder getHolder(Context context) {
        return new ExperFarmWaitPayOrderHolder(context);
    }
}
