package com.mac.runtu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.holder.Order3Holder;
import com.mac.runtu.holder.OrderWaitPayHolder;
import com.mac.runtu.javabean.UserCenertOrderDetailInfo;

import java.util.ArrayList;

/**
 * Description:全部订单
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/11 0011 上午 11:20
 */
public class UserAllOrderListAdapter extends BaseAdapter {

    private static final String TAG = "UserAllOrderListAdapter";


    // 0 1 区分状态
    private static final int other_order = 1;
    private static final int wait_pay    = 0;

    private Context                              context;
    private ArrayList<UserCenertOrderDetailInfo> list;
    private UserCenertOrderDetailInfo            dataOther;
    private UserCenertOrderDetailInfo            dataWaitPay;


    public UserAllOrderListAdapter(Context context,
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
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        UserCenertOrderDetailInfo info = list.get
                (position);


        if (0 == info.orderStatus) {

            return wait_pay;
        } else {
            return other_order;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int itemViewType = getItemViewType(position);
        switch (itemViewType) {

            case wait_pay:

                convertView = setDataWaitPay(convertView, position);

               /* ViewHolderWaitPay holderPay = null;

                if (convertView == null) {
                    holderPay = new ViewHolderWaitPay();
                    convertView = View.inflate(context, R.layout
                            .pending_payment_order_item_layout, null);

                    //状态
                    holderPay.tvState = (TextView) convertView.findViewById
                            (R.id.state_Tv);

                    //总价
                    holderPay.tvTotlePrice = (TextView) convertView
                            .findViewById(R.id
                                    .totle_price_Tv);
                    //产品详情跳转按钮
                    holderPay.ivGoodDetail = (Button) convertView
                            .findViewById(R.id
                                    .order_details_Iv);
                    //图片t
                    holderPay.ivGoodImage = (ImageView) convertView
                            .findViewById(R.id
                                    .order_product_Iv);
                    //地址 中括号
                    holderPay.tvAdressCity = (TextView) convertView
                            .findViewById(R.id
                                    .tv_addressCity);
                    //地址
                    holderPay.tvAddressArea = (TextView) convertView
                            .findViewById(R.id
                                    .tv_addressArea);

                    //描述
                    holderPay.tvGoodDes = (TextView) convertView.findViewById
                            (R.id.order_product_name_Tv);

                    //数量
                    holderPay.tvNum = (TextView) convertView.findViewById(R
                            .id.order_product_number_Tv);


                    convertView.setTag(holderPay);

                } else {
                    holderPay = (ViewHolderWaitPay) convertView.getTag();
                }

                dataWaitPay = getItem(position);

                UserOrderBusinessDetailInfo businessInfoWaitPay = dataWaitPay
                        .businessInfo;

                //图片
                ArrayList<PictureInfo> pictureInfos = dataWaitPay.pictureInfos;

                if (pictureInfos != null && pictureInfos.size() > 0) {
                    ImageLoadUtils.rectangleImage((Activity) context,
                            GlobalConstants.Base_imgae_url + pictureInfos.get
                                    (0).pictureName.trim(), holderPay
                                    .ivGoodImage);
                }

                holderPay.tvTotlePrice.setText("￥" + dataWaitPay.cost);


                holderPay.ivGoodDetail.setOnClickListener(new View
                        .OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.e(TAG, "商品详情被点击了");

                        //MarkerConstants.value_good_uuid = dataWaitPay
                        // .busi_uuid;

                        //订单用两种  特产电商 和 体验农场
                       *//* if (GlobalConstants.mode_user_center_order_1 ==
                                dataWaitPay.model) {
                            //特产电商
                            context.startActivity(new Intent(context,
                                    SpecialtyDetailsActivity.class));
                        } else {
                            //体验农场
                            context.startActivity(new Intent(context,
                                    ExperienceFarmAdoptDetails.class));
                        }
*//*

                        //跳转付款页面
                        Intent intent = new Intent(context,
                                SelectPayWayActivity.class);

                        intent.putExtra(GlobalConstants.key_uuid, dataWaitPay
                                .uuid);

                        intent.putExtra(GlobalConstants.key_allPay,
                                dataWaitPay.cost);
                        context.startActivity(intent);

                    }
                });

                //ImageLoadUtils.rectangleImage((Activity) context,"",
                // ivGoodImage);

                holderPay.tvAdressCity.setText("【 " + dataWaitPay.district +
                        " 】");


                holderPay.tvAddressArea.setText("" + dataWaitPay.district);

                holderPay.tvGoodDes.setText(businessInfoWaitPay.name);

                holderPay.tvNum.setText(dataWaitPay.amount + "件");

                holderPay.tvState.setText(ExChange2StrUtils.getOrderStatud
                        (dataWaitPay.order_status));*/

                break;


            case other_order:

                convertView = setDataOther3(convertView, position);

               /* ViewHolderOther holderOther = null;

                if (convertView == null) {

                    holderOther = new ViewHolderOther();

                    convertView = View.inflate(context, R.layout
                            .order_item_layout, null);
                    //状态
                    holderOther.tvState = (TextView) convertView.findViewById
                            (R.id.state_Tv);
                    //总价
                    holderOther.tvTotlePrice = (TextView) convertView
                            .findViewById(R.id.totle_price_Tv);
                    //产品详情跳转按钮
                    holderOther.ivGoodDetail = (ImageView) convertView
                            .findViewById(R.id.order_details_Iv);
                    //图片t
                    holderOther.ivGoodImage = (ImageView) convertView
                            .findViewById(R.id.order_product_Iv);
                    //地址 中括号
                    holderOther.tvAdressCity = (TextView) convertView
                            .findViewById(R.id.tv_addressCity);
                    //地址
                    holderOther.tvAddressArea = (TextView) convertView
                            .findViewById(R.id.tv_addressArea);

                    //描述
                    holderOther.tvGoodDes = (TextView) convertView
                            .findViewById(R.id
                                    .order_product_name_Tv);

                    //数量
                    holderOther.tvNum = (TextView) convertView.findViewById(R.id
                            .order_product_number_Tv);

                    convertView.setTag(holderOther);

                } else {
                    holderOther = (ViewHolderOther) convertView.getTag();
                }

                dataOther = getItem(position);
                UserOrderBusinessDetailInfo businessInfoOther = dataOther
                        .businessInfo;

                //图片
                ArrayList<PictureInfo> pictureInfosOther = dataOther
                        .pictureInfos;

                if (pictureInfosOther != null && pictureInfosOther.size() > 0) {
                    ImageLoadUtils.rectangleImage((Activity) context,
                            GlobalConstants.Base_imgae_url +
                                    pictureInfosOther.get
                                            (0).pictureName.trim(),
                            holderOther.ivGoodImage);
                }


                holderOther.tvState.setText(ExChange2StrUtils.getOrderStatud
                        (dataOther.order_status));


                holderOther.tvTotlePrice.setText("￥" + dataOther.cost);


                holderOther.ivGoodDetail.setOnClickListener(new View
                        .OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.e(TAG, "商品详情被点击了");

                        MarkerConstants.value_good_uuid = dataOther.busi_uuid;

                        if (GlobalConstants.mode_user_center_order_1 ==
                                dataOther.model) {

                            context.startActivity(new Intent(context,
                                    SpecialtyDetailsActivity.class));
                        } else {
                            context.startActivity(new Intent(context,
                                    ExperienceFarmAdoptDetails.class));
                        }

                    }
                });

                //ImageLoadUtils.rectangleImage((Activity) context,"",
                // ivGoodImage);

                holderOther.tvAdressCity.setText("【 " + dataOther.district +
                        " 】");


                holderOther.tvAddressArea.setText(dataOther.district);

                holderOther.tvGoodDes.setText(businessInfoOther.name);
                holderOther.tvNum.setText(dataOther.amount + "件");*/


                break;
        }


        return convertView;
    }


    //待付款
    private View setDataWaitPay(View convertView, int position) {

        BaseHolder holder1 = null;

        if (convertView == null) {
            holder1 = new OrderWaitPayHolder(context);
        } else {
            holder1 = (BaseHolder) convertView.getTag();

        }
        holder1.setData(getItem(position), position);

        return holder1.convertView;

    }

    //待发货
    private View setDataOther3(View convertView, int position) {
        BaseHolder holder2 = null;
        if (convertView == null) {
            holder2 = new Order3Holder(context);
        } else {
            holder2 = (BaseHolder) convertView.getTag();

        }
        holder2.setData(getItem(position), position);
        return holder2.convertView;
    }


}
