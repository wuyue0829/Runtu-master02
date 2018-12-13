package com.mac.runtu.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.BusinessTypeInfo;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.javabean.SpecialtyGoodDetailInfo;
import com.mac.runtu.utils.ImageLoadUtils;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/30 22:59
 */
public class ExperFarmHomeHolder extends BaseHolder<SpecialtyGoodDetailInfo> {

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

    public ExperFarmHomeHolder(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout
                .experience_farm_list_item_layout, null);
        ivImage = (ImageView) view.findViewById(R.id.imageView);
        //ivComfin = (ImageView) view.findViewById(R.id.imageView);

        // btnConfirm = (Button) view.findViewById(R.id.confirm_receipt_Bt);
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
    public void refreshView(int position) {

        ArrayList<PictureInfo> pictureInfos = data.pictureInfos;
        if (pictureInfos != null && pictureInfos.size() > 0) {

            ImageLoadUtils.rectangleImageRadius(
                    GlobalConstants
                            .Base_imgae_url + pictureInfos.get(0).pictureName
                            .trim(),
                    ivImage);

        }

        tvTitle.setText(data.name);

        tvAddressType.setText("" + data.districtName);
        BusinessTypeInfo typeInfo = data.businessTypeInfo;
       /* if (typeInfo != null) {
            tvType.setText(data.businessTypeInfo.typeName);
        }*/

        tvType.setText("" + data.kindName);

        tvPrice.setText("￥" + data.minPrice);

        tvFinshPeople.setText(data.payment + "人收货");

        StringBuilder builder = new StringBuilder();
        builder.append(TextUtils.isEmpty(data.province) ? "" : data.province
                + "/");
        builder.append(TextUtils.isEmpty(data.city) ? "" : data.city + "/");
        builder.append(TextUtils.isEmpty(data.county) ? "" : data.county);
        tvLocation.setText(builder.toString());
    }
}
