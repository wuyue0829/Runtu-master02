package com.mac.runtu.holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.SelectPayWayActivity;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.javabean.UserCenertOrderDetailInfo;
import com.mac.runtu.javabean.UserOrderBusinessDetailInfo;
import com.mac.runtu.utils.ExChange2StrUtils;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.SPUtils;

import java.util.ArrayList;

/**
 * Description: 特产电商待待付款
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/23 0:31
 */
public class OrderWaitPayHolder extends BaseHolder<UserCenertOrderDetailInfo> {
    private static final String TAG = "Order3Holder";

    // private TextView  tvState;
    private TextView  tvTotlePrice;
    private Button    ivGoodDetail;
    private ImageView ivGoodImage;
    private TextView  tvAdressCity;
    private TextView  tvAddressArea;
    private TextView  tvGoodDes;
    private TextView  tvNum;
    private TextView  tvState;
    private TextView  tvOrderNum;
    private TextView tv_order_time;

    public OrderWaitPayHolder(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout
                .pending_payment_order_item_layout, null);

        //状态
        tvState = (TextView) view.findViewById
                (R.id.state_Tv);
        //总价
        tvTotlePrice = (TextView) view.findViewById(R.id
                .totle_price_Tv);
        //产品详情跳转按钮
        ivGoodDetail = (Button) view.findViewById(R.id
                .order_details_Iv);
        //图片t
        ivGoodImage = (ImageView) view.findViewById(R.id
                .order_product_Iv);
        //地址 中括号
        tvAdressCity = (TextView) view.findViewById(R.id
                .tv_addressCity);
        //地址
        tvAddressArea = (TextView) view.findViewById(R.id
                .tv_addressArea);

        //描述
        tvGoodDes = (TextView) view.findViewById(R.id
                .order_product_name_Tv);

        tv_order_time = (TextView) view.findViewById(R.id
                .tv_order_time);


        //数量
        tvNum = (TextView) view.findViewById(R.id
                .order_product_number_Tv);
        tvOrderNum = (TextView) view.findViewById(R.id.tv_order_num);
        //view.findViewById(R.id.)
        return view;
    }

    @Override
    public void refreshView(int position) {

        //根据状态变化设置字体
        UserOrderBusinessDetailInfo businessInfoWaitPay = data
                .businessInfo;

        //图片
        ArrayList<PictureInfo> pictureInfos = data.pictureInfos;

        if (pictureInfos != null && pictureInfos.size() > 0) {
            ImageLoadUtils.rectangleImage(
                    GlobalConstants.Base_imgae_url + pictureInfos.get
                            (0).pictureName.trim(), ivGoodImage);
        }

        tvTotlePrice.setText("￥" + data.cost + "元");


        ivGoodDetail.setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e(TAG, "商品详情被点击了");

                //跳转付款页面
                Intent intent = new Intent(context,
                        SelectPayWayActivity.class);

                //订单的id
                intent.putExtra(GlobalConstants.key_uuid, data
                        .uuid);

                intent.putExtra(GlobalConstants.key_allPay,
                        data.cost);
                //告诉支付完成时刷新数据 要不要刷新 从订单去要刷新从详情去不要刷新
                SPUtils.setBoolean(context, GlobalConstants.sp_pay_from, true);

                context.startActivity(intent);

            }
        });

        //ImageLoadUtils.rectangleImage((Activity) context,"",
        // ivGoodImage);

        tvAdressCity.setText("【 " + data.businessInfo.county +
                " 】");


        tvAddressArea.setText("" + data.businessInfo.county);

        tvGoodDes.setText(businessInfoWaitPay.name);

        tvNum.setText(data.amount + "件");

        tvState.setText(ExChange2StrUtils.getOrderStatud
                (data.orderStatus));
        tvOrderNum.setText("订单编号：" + data.orderInfo.getOrderCode());
        tv_order_time.setText("下单时间：" + data.orderInfo.getCreateTime().replace("T"," "));

        tvOrderNum.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LogUtils.e(TAG,"点单被长按");
                tvOrderNum.setSelectAllOnFocus(true);
                tvOrderNum.setTextIsSelectable(true);
                return true;
            }
        });

    }


}
