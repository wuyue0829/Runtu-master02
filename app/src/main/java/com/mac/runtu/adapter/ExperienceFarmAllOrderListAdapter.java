package com.mac.runtu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.holder.ExperFarmFinishOrderHolder;
import com.mac.runtu.holder.ExperFarmWaitGetGoodOrderHolder;
import com.mac.runtu.holder.ExperFarmWaitPayOrderHolder;
import com.mac.runtu.holder.ExperFarmWaitSetGoodOrderHolder;
import com.mac.runtu.javabean.UserCenertOrderDetailInfo;

import java.util.ArrayList;

/**
 * Description:全部订单
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/11 0011 上午 11:20
 */
public class ExperienceFarmAllOrderListAdapter extends BaseAdapter {

    private static final String TAG = "ExperienceFarmAllOrderListAdapter";


    // 0 1 区分状态
    public static final int TAB_wait_pay      = 0;
    public static final int TAB_wait_set_good = 1;
    public static final int TAB_wait_get_good = 2;
    public static final int TAB_finish        = 3;


    private Context                              context;
    private ArrayList<UserCenertOrderDetailInfo> list;


    public ExperienceFarmAllOrderListAdapter(Context context,
                                             ArrayList<UserCenertOrderDetailInfo>
                                                     list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public UserCenertOrderDetailInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {

        return list.get(position).orderStatus;
        //return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int itemViewType = getItemViewType(position);

        switch (itemViewType) {

            case TAB_wait_pay:
                convertView = setDataWaitPay(convertView, position);
                break;
            case TAB_wait_set_good:

                convertView = setDataSetGood(convertView, position);
                break;

            case TAB_wait_get_good:

                convertView = setDataGetGood(convertView, position);
                break;
            case TAB_finish:

                convertView = setDataFinish(convertView, position);
                break;
        }

        return convertView;
    }

    //待付款
    private View setDataWaitPay(View convertView, int position) {

        BaseHolder holder1 = null;

        if (convertView == null) {
            holder1 = new ExperFarmWaitPayOrderHolder(context);
        } else {
            holder1 = (BaseHolder) convertView.getTag();

        }
        holder1.setData(getItem(position), position);

        return holder1.convertView;
    }

    //待发货
    private View setDataSetGood(View convertView, int position) {
        BaseHolder holder2 = null;
        if (convertView == null) {
            holder2 = new ExperFarmWaitSetGoodOrderHolder(context);
        } else {
            holder2 = (BaseHolder) convertView.getTag();

        }
        holder2.setData(getItem(position), position);
        return holder2.convertView;
    }

    //待收货
    private View setDataGetGood(View convertView, int position) {
        BaseHolder holder3 = null;
        if (convertView == null) {
            holder3 = new ExperFarmWaitGetGoodOrderHolder(context);
        } else {
            holder3 = (BaseHolder) convertView.getTag();

        }
        holder3.setData(getItem(position), position);
        return holder3.convertView;
    }

    //已完成
    private View setDataFinish(View convertView, int position) {
        BaseHolder holder4 = null;
        if (convertView == null) {
            holder4 = new ExperFarmFinishOrderHolder(context);
        } else {
            holder4 = (BaseHolder) convertView.getTag();

        }
        holder4.setData(getItem(position), position);
        return holder4.convertView;
    }
}
