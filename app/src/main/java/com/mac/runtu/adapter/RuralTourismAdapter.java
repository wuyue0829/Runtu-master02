package com.mac.runtu.adapter;

import android.content.Context;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.holder.RuralTourismHolder;
import com.mac.runtu.javabean.RuralTourismListInfo;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/26 0026 上午 10:21
 */
public class RuralTourismAdapter extends MyBaseAdapter<RuralTourismListInfo> {
    public RuralTourismAdapter(Context context, ArrayList<RuralTourismListInfo>
            data) {
        super(context, data);
    }

    @Override
    public BaseHolder getHolder(Context context) {
        return new RuralTourismHolder(context);
    }
}
