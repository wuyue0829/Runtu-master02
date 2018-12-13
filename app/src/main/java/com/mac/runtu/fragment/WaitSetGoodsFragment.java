package com.mac.runtu.fragment;

import android.widget.BaseAdapter;

import com.mac.runtu.adapter.ExperFarmWaitSetGoodsOrderListAdapter;
import com.mac.runtu.adapter.OrderListAdapter;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.utils.LogUtils;

/**
 * Description:待发货
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/11 0011 上午 10:26
 */
public class WaitSetGoodsFragment extends BaseFragment02 {

    @Override
    public String getOrderStatus() {
        return GlobalConstants.value_order_sta_waitsetgood;
    }

    @Override
    protected BaseAdapter getExperienceFarmOrderAdapter() {
        LogUtils.e(TAG,"体验农场待发货num="+num+": pagerNum="+pageNum);
        return new ExperFarmWaitSetGoodsOrderListAdapter(mActivity,
                mAllList);
    }

    @Override
    public BaseAdapter getUserOrderAdapter() {
        LogUtils.e(TAG,"特产电商待发货num="+num+"pagerNum="+pageNum);
        return new OrderListAdapter(mActivity, mAllList);
    }

}
