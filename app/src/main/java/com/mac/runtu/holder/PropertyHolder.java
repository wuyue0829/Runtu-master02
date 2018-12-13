package com.mac.runtu.holder;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.javabean.PropertyDetailInfo;
import com.mac.runtu.utils.ExChange2StrUtils;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.LogUtils;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/25 0025 下午 5:05
 */
public class PropertyHolder extends BaseHolder<PropertyDetailInfo> {

    private static final String TAG = "PropertyHolder";

    private ImageView ivImage;
    private TextView  tvTiltle;
    private TextView  tvAddressType;
    private TextView  tvType;
    private TextView  tvConent;
    private TextView  tvTime;
    private TextView  tvLocaltionAddress;
    private TextView  tvPrice;
    private TextView  tvSize;

    public PropertyHolder(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout
                .circulation_of_property_rights_list_item_layout, null);

        ivImage = (ImageView) view.findViewById(R.id.imageView);

        tvTiltle = (TextView) view.findViewById(R.id.title_Tv);
        //洛川
        tvAddressType = (TextView) view.findViewById(R.id.address_Tv);
        //富士 橘子
        tvType = (TextView) view.findViewById(R.id.type_Tv);

        tvPrice = (TextView) view.findViewById(R.id.price_Tv);

        tvTime = (TextView) view.findViewById(R.id.time_Tv);
        tvSize = (TextView) view.findViewById(R.id.size_Tv);

        //当地地址
        tvLocaltionAddress = (TextView) view.findViewById(R.id.localtion_Tv);

        return view;
    }

    @Override
    public void refreshView(int position) {
        ArrayList<PictureInfo> pictureInfos = data.pictureInfos;
        if (pictureInfos != null && pictureInfos.size() > 0) {

            LogUtils.e(TAG, "产权流转图片地址" + GlobalConstants.Base_imgae_url +
                    data.pictureInfos.get(0).pictureName);

            ImageLoadUtils.rectangleImage(
                    GlobalConstants.Base_imgae_url + data
                            .pictureInfos.get(0).pictureName.trim(), ivImage);
        } else {
            ImageLoadUtils.rectangleImageId((Activity) context, R.mipmap
                    .logostr, ivImage);
        }

        //LogUtils.e(TAG, "tiltle==" + data.title);
        tvTiltle.setText(data.title);
        // 流转方式1为转让，2为转包，3为互换，4为入股，5为出租
        if (data.kindDictionaryInfo != null && data.kindDictionaryInfo
                .kindName != null) {

            tvAddressType.setText(data.kindDictionaryInfo.kindName);
        }


        String exchange = data.exchange;

        if (!TextUtils.isEmpty(exchange)) {
            tvType.setText(ExChange2StrUtils.setExChange(data.exchange));

        }

        tvPrice.setText(data.price);
        tvTime.setText(data.years + "年");
        tvSize.setText(data.area + "亩");


        //地址

        StringBuilder builder = new StringBuilder();
        builder.append("");

        builder.append(TextUtils.isEmpty(data.city) ? "" : data.city + "/");
        builder.append(TextUtils.isEmpty(data.county) ? "" : data.county+ "/");
        builder.append(TextUtils.isEmpty(data.town) ? "" : data.town);
        tvLocaltionAddress.setText(builder.toString());


    }
}
