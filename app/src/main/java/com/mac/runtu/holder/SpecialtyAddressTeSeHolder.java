package com.mac.runtu.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.javabean.SpecialtyGoodDetailInfo;
import com.mac.runtu.utils.ImageLoadUtils;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/5 0005 下午 6:31
 */
public class SpecialtyAddressTeSeHolder extends
        BaseHolder<SpecialtyGoodDetailInfo> {

    private static final String TAG = "SpecialtyAddressTeSeHolder";

    private TextView  tvAddress;
    private TextView  tvTitle;
    private TextView  tvPrice;
    private ImageView ivProductImage;

    public SpecialtyAddressTeSeHolder(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout
                .local_specialties_grid_item_layout, null);
        tvAddress = (TextView) view.findViewById(R.id.tv_address);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvPrice = (TextView) view.findViewById(R.id.tv_price);
        ivProductImage = (ImageView) view.findViewById(R.id
                .iv_product_image);

        return view;
    }

    @Override
    public void refreshView(int position) {
        tvAddress.setText("【" + data.districtName + "】");
        tvTitle.setText("  "+data.name);
        tvPrice.setText("  "+"¥" + data.minPrice);

        ArrayList<PictureInfo> pictureInfos = data.pictureInfos;

        if (pictureInfos != null && pictureInfos.size() > 0) {
            ImageLoadUtils.rectangleImage( GlobalConstants
                            .Base_imgae_url + pictureInfos.get(0).pictureName.trim(),
                    ivProductImage);
        }
    }
}
