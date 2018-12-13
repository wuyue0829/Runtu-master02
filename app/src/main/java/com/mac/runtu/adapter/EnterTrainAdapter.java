package com.mac.runtu.adapter;

import android.content.Context;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.holder.EnterTrainHolder;
import com.mac.runtu.javabean.EntreTrainDetailInfo;

import java.util.ArrayList;

/**
 * Description:创业培训的适配器c
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/26 0026 下午 5:18
 */
public class EnterTrainAdapter extends MyBaseAdapter<EntreTrainDetailInfo> {
    public EnterTrainAdapter(Context context, ArrayList<EntreTrainDetailInfo>
            data) {
        super(context, data);
    }

    @Override
    public BaseHolder getHolder(Context context) {
        return new EnterTrainHolder(context);
    }
}
