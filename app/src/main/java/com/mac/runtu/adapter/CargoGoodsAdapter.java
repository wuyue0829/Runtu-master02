package com.mac.runtu.adapter;

import android.content.Context;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.holder.CargoGoodsHolder;
import com.mac.runtu.javabean.CargoDetailInfo;

import java.util.ArrayList;

/**
 * Description:捎货
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/28 0028 下午 7:15
 */
public class CargoGoodsAdapter extends MyBaseAdapter<CargoDetailInfo> {
    public CargoGoodsAdapter(Context context, ArrayList<CargoDetailInfo> data) {
        super(context, data);
    }

    @Override
    public BaseHolder getHolder(Context context) {
        return new CargoGoodsHolder(context);
    }
}
