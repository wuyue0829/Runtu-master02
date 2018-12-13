package com.mac.runtu.fragment;

import android.widget.BaseAdapter;

import com.mac.runtu.adapter.ExperFarmFinishOrderListAdapter;
import com.mac.runtu.adapter.OrderListAdapter;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.utils.LogUtils;


/**
 * Description: 订单完成页
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/21 0021 上午 10:34
 */
public class FinishOrderFragment extends BaseFragment02 {

    @Override
    public String getOrderStatus() {
        return GlobalConstants.value_order_sta_final;
    }


    @Override
    protected BaseAdapter getExperienceFarmOrderAdapter() {

        LogUtils.e(TAG, "体验农场已完成订单num=" + num + ": pagerNum=" + pageNum);
        return new ExperFarmFinishOrderListAdapter(mActivity,
                mAllList);
    }

    @Override
    public BaseAdapter getUserOrderAdapter() {

        LogUtils.e(TAG, "特产电商已完成num=" + num + "pagerNum=" + pageNum);
        return new
                OrderListAdapter(mActivity, mAllList);
    }


}
