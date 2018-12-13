package com.mac.runtu.fragment;

import android.widget.BaseAdapter;

import com.mac.runtu.adapter.ExperFarmWaitPayOrderListAdapter;
import com.mac.runtu.adapter.OrderListWaitPayAdapter;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.utils.LogUtils;

/**
 * Description: 待付款订单也
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/21 0021 上午 10:34
 */
public class WaitPayFragment extends BaseFragment02 {
    @Override
    public String getOrderStatus() {
        return GlobalConstants.value_order_sta__waitpay;
    }

    @Override
    protected BaseAdapter getExperienceFarmOrderAdapter() {
        LogUtils.e(TAG,"体验农场待付款订单num="+num+": pagerNum="+pageNum);
        return new ExperFarmWaitPayOrderListAdapter(mActivity,
                mAllList);
    }

    @Override
    public BaseAdapter getUserOrderAdapter() {
        LogUtils.e(TAG,"特产电商待付款num="+num+"pagerNum="+pageNum);
        return new OrderListWaitPayAdapter(mActivity, mAllList);
    }


}
