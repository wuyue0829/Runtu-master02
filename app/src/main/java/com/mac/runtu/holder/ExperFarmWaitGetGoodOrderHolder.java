package com.mac.runtu.holder;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.business.OrderGetGoodInfrom2NetBiz;
import com.mac.runtu.fragment.WaitGetGoodsFragment;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnGetData2BooleanListener;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.javabean.UserCenertOrderDetailInfo;
import com.mac.runtu.javabean.UserOrderBusinessDetailInfo;
import com.mac.runtu.utils.FragmentFactory;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Description:待收货
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/29 0029 下午 8:28
 */
public class ExperFarmWaitGetGoodOrderHolder extends
        BaseHolder<UserCenertOrderDetailInfo> {


    private ImageView ivImage;
    private ImageView ivComfin;
    private TextView  tvTitle;
    private TextView  tvAddressType;
    private TextView  tvType;
    private TextView  tvCount;
    private TextView  tvPrice;
    private TextView  tvFinshPeople;
    private TextView  tvLocation;
    private Button    btnConfirm;
    private TextView  tvOrderNum;

    public ExperFarmWaitGetGoodOrderHolder(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        // resId = R.layout.my_adopt_pending_receipt_order_item_layout;
        View view = View.inflate(context, R.layout
                .my_adopt_pending_receipt_order_item_layout, null);

        ivImage = (ImageView) view.findViewById(R.id.imageView);
        btnConfirm = (Button) view.findViewById(R.id.confirm_receipt_Bt);


        tvTitle = (TextView) view.findViewById(R.id.title_Tv);
        tvAddressType = (TextView) view.findViewById(R.id.address_Tv);
        tvType = (TextView) view.findViewById(R.id.type_Tv);
        tvCount = (TextView) view.findViewById(R.id.amount_Tv);
        tvPrice = (TextView) view.findViewById(R.id.price_Tv);
        tvFinshPeople = (TextView) view.findViewById(R.id.passengers_Tv);
        tvLocation = (TextView) view.findViewById(R.id.localtion_Tv);

        return view;
    }

    @Override
    public void refreshView(final int position) {
        ArrayList<PictureInfo> pictureInfos = data.pictureInfos;
        if (pictureInfos != null && pictureInfos.size() > 0) {

            ImageLoadUtils.rectangleImageRadius(
                    GlobalConstants
                            .Base_imgae_url + pictureInfos.get(0).pictureName
                            .trim(),
                    ivImage);

        }

        UserOrderBusinessDetailInfo dataInfo = data.businessInfo;

        //第一个参数
        tvTitle.setText(dataInfo.name);

        tvAddressType.setText("" + dataInfo.county);

        tvType.setText(dataInfo.kindName);

        tvPrice.setText("￥" + data.cost);
        tvCount.setText(data.amount + "件");

        tvFinshPeople.setText("已有" + dataInfo.payment + "人收货");

       /* StringBuilder builder = new StringBuilder();
        builder.append(TextUtils.isEmpty(dataInfo.province) ? "" : dataInfo
        .province
                + "/");
        builder.append(TextUtils.isEmpty(dataInfo.city) ? "" : dataInfo.city
                + "/");
        builder.append(TextUtils.isEmpty(dataInfo.county) ? "" : dataInfo
                .county);
        tvLocation.setText(builder.toString());*/

        btnConfirm.setText("确认收货");


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转付款也面
                informFwd(position);

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
                    WaitGetGoodsFragment waitGetGoodsFragment =
                            (WaitGetGoodsFragment)
                            FragmentFactory.getOrderFragment(3);
                    waitGetGoodsFragment.refreshListData(position);

                }
            }

            @Override
            public void onFail() {
                ToastUtils.makeTextShowNoNet((Activity) context);
            }
        });

    }
}
