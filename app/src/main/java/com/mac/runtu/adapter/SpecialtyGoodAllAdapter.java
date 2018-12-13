package com.mac.runtu.adapter;

import android.content.Context;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.holder.SpecialtyGoodAllHolder;
import com.mac.runtu.javabean.SpecialtyGoodDetailInfo;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/5 0005 下午 7:08
 */
public class SpecialtyGoodAllAdapter extends
        MyBaseAdapter<SpecialtyGoodDetailInfo> {
    public SpecialtyGoodAllAdapter(Context context,
                                   ArrayList<SpecialtyGoodDetailInfo> data) {
        super(context, data);
    }

    @Override
    public BaseHolder getHolder(Context context) {
        return new SpecialtyGoodAllHolder(context);
    }
}
