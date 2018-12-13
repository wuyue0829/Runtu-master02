package com.mac.runtu.adapter;

import android.content.Context;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.holder.ExperFarmHomeHolder;
import com.mac.runtu.javabean.SpecialtyGoodDetailInfo;

import java.util.ArrayList;

/**
 * Description:体验农场主页listview适配器
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/30 22:56
 */
public class ExperFarmHomeAdapter extends MyBaseAdapter<SpecialtyGoodDetailInfo> {
    public ExperFarmHomeAdapter(Context context,
                                ArrayList<SpecialtyGoodDetailInfo> data) {
        super(context, data);
    }

    @Override
    public BaseHolder getHolder(Context context) {
        return new ExperFarmHomeHolder(context);
    }
}
