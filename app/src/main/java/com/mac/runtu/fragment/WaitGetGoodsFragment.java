package com.mac.runtu.fragment;

import android.view.View;
import android.widget.BaseAdapter;

import com.mac.runtu.adapter.ExperFarmWaitGetGoodsOrderListAdapter;
import com.mac.runtu.adapter.OrderListAdapter;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.utils.LogUtils;

/**
 * Description: 待收货
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/21 0021 上午 10:34
 */
public class WaitGetGoodsFragment extends BaseFragment02 {

    @Override
    public String getOrderStatus() {
        return GlobalConstants.value_order_sta_waitgetgood;
    }

    @Override
    protected BaseAdapter getExperienceFarmOrderAdapter() {

        LogUtils.e(TAG, "体验农场待收货num=" + num + "pagerNum=" + pageNum);

        return new ExperFarmWaitGetGoodsOrderListAdapter(mActivity,
                mAllList);
    }

    @Override
    public BaseAdapter getUserOrderAdapter() {
        LogUtils.e(TAG, "特产电商待收货num=" + num + "pagerNum=" + pageNum);

        return new OrderListAdapter(mActivity, mAllList);
    }


    public void refreshListData(final int position) {

        //根据位置删除元素
        llLoading.setVisibility(View.VISIBLE);
        tvContent.setText("更新数据");
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                       initDataRefresh();

                        llLoading.setVisibility(View.GONE);

                    }
                });
            }
        }.start();

    }


}
