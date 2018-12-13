package com.mac.runtu.holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.specialtyshop.SpecialtyDetailsActivity;
import com.mac.runtu.business.OrderGetGoodInfrom2NetBiz;

import com.mac.runtu.fragment.WaitGetGoodsFragment;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnGetData2BooleanListener;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.javabean.UserCenertOrderDetailInfo;
import com.mac.runtu.javabean.UserOrderBusinessDetailInfo;
import com.mac.runtu.utils.ExChange2StrUtils;
import com.mac.runtu.utils.FragmentFactory;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Description: 待发货订单
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/23 0:31
 */
public class Order3Holder extends BaseHolder<UserCenertOrderDetailInfo> {

    private static final String TAG = "Order3Holder";

    private TextView  tvState;
    private TextView  tvTotlePrice;
    private Button    ivGoodDetail;
    private ImageView ivGoodImage;
    private TextView  tvAdressCity;
    private TextView  tvAddressArea;
    private TextView  tvGoodDes;
    private TextView  tvNum;
    private TextView  tvOrderNum;

    public Order3Holder(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.order_item_layout, null);
        //状态
        tvState = (TextView) view.findViewById(R.id.state_Tv);
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

        //数量
        tvNum = (TextView) view.findViewById(R.id
                .order_product_number_Tv);

        tvOrderNum = (TextView) view.findViewById(R.id.tv_order_num);

        //view.findViewById(R.id.)
        return view;
    }

    @Override
    public void refreshView(final int position) {

        //根据状态变化设置字体
        UserOrderBusinessDetailInfo businessInfoOther = data
                .businessInfo;

        //图片
        ArrayList<PictureInfo> pictureInfosOther = data
                .pictureInfos;

        if (pictureInfosOther != null && pictureInfosOther.size() > 0) {
            ImageLoadUtils.rectangleImage((Activity) context,
                    GlobalConstants.Base_imgae_url +
                            pictureInfosOther.get
                                    (0).pictureName.trim(),
                    ivGoodImage);
        }


        tvState.setText(ExChange2StrUtils.getOrderStatud
                (data.orderStatus));

        LogUtils.e(TAG, "订单的stauts=" + data.orderStatus);


        tvTotlePrice.setText("￥" + data.cost+"元");


        if (data.orderStatus == 2) {
            ivGoodDetail.setText("确认收货");
        }
        ivGoodDetail.setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e(TAG, "商品详情被点击了");


                if (data.orderStatus == 2) {
                    //通知服务端
                    //确认收货
                    informFwd(position);

                } else {


                    if (data.busiUuid != null) {
                        Intent intent = new Intent(context,
                                SpecialtyDetailsActivity.class);

                        intent.putExtra(GlobalConstants.key_model,
                                GlobalConstants.value_mode_filter_specialty);
                        intent.putExtra(GlobalConstants.key_uuid, data
                                .busiUuid);
                        context.startActivity(intent);
                    }

                }


            }
        });

        //ImageLoadUtils.rectangleImage((Activity) context,"",
        // ivGoodImage);

        tvAdressCity.setText("【 " + data.businessInfo.county +
                " 】");


        tvAddressArea.setText(data.businessInfo.county);

        tvGoodDes.setText(businessInfoOther.name);
        tvNum.setText(data.amount + "件");
        tvOrderNum.setText("订单编号：" + data.orderInfo.getOrderCode());

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

    //通知服务端
    private void informFwd(final int position) {

        OrderGetGoodInfrom2NetBiz.infromFWD(data.uuid, GlobalConstants
                .value_order_sta_final, new OnGetData2BooleanListener() {


            @Override
            public void onSuccess(Boolean result) {
                if (result) {

                    //刷新数据当强页面数据
                    // RefreshOrderBiz.refresh2OrderData();
                    WaitGetGoodsFragment waitGetGoodsFragment = (WaitGetGoodsFragment)
                            FragmentFactory.getOrderFragment(3);

                    waitGetGoodsFragment.refreshListData(position);

                    //发送时间更新数据
                    EventBus.getDefault().post(GlobalConstants.user_Order_Updata);

                }
            }

            @Override
            public void onFail() {
                ToastUtils.makeTextShowNoNet((Activity) context);
            }
        });

    }
}
