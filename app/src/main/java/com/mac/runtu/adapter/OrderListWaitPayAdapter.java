package com.mac.runtu.adapter;

import android.content.Context;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.holder.OrderWaitPayHolder;
import com.mac.runtu.javabean.UserCenertOrderDetailInfo;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/23 0:29
 */
public class OrderListWaitPayAdapter extends MyBaseAdapter<UserCenertOrderDetailInfo> {


    public OrderListWaitPayAdapter(Context context, ArrayList<UserCenertOrderDetailInfo> data) {
        super(context, data);

    }

    @Override
    public BaseHolder getHolder(Context context) {
        return new OrderWaitPayHolder(context);
    }
}
