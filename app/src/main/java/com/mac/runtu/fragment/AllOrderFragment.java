package com.mac.runtu.fragment;

import android.widget.BaseAdapter;

import com.mac.runtu.adapter.ExperienceFarmAllOrderListAdapter;
import com.mac.runtu.adapter.UserAllOrderListAdapter;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.utils.LogUtils;

/**
 * Description: 个人订单全部查询
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/21 0021 上午 10:34
 */
public class AllOrderFragment extends BaseFragment02 {



    @Override
    public String getOrderStatus() {
        return GlobalConstants.value_order_sta__all;
    }


    @Override
    protected BaseAdapter getExperienceFarmOrderAdapter() {

        LogUtils.e(TAG, "体验农场所有订单" + num + ": pageNum=" + pageNum);

        return new ExperienceFarmAllOrderListAdapter(mActivity,
                mAllList);
    }

    @Override
    public BaseAdapter getUserOrderAdapter() {

        LogUtils.e(TAG, "特产电商所有订单" + num + "===" + pageNum);
        return new UserAllOrderListAdapter(mActivity, mAllList);
    }

}
