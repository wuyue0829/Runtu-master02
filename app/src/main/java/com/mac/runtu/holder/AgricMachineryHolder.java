package com.mac.runtu.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.AgricuMachineryDetailInfo;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Description: 农资农机条目的条目
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/26 23:37
 */
public class AgricMachineryHolder extends
        BaseHolder<AgricuMachineryDetailInfo> {
    private static final String TAG = "AgricMachineryHolder";

    private ImageView ivImage;
    private TextView  tvTitle;
    private TextView  tvPrice;
    private TextView  tvType;
    private TextView  tvCreattime;
    private TextView  tvLocaltion;

    public AgricMachineryHolder(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout
                .agricultural_machinery_list_item_layout, null);

        ivImage = (ImageView) view.findViewById(R.id.imageView);

        tvTitle = (TextView) view.findViewById(R.id.title_Tv);
        tvPrice = (TextView) view.findViewById(R.id.price_Tv);
        tvType = (TextView) view.findViewById(R.id.type_Tv);
        tvCreattime = (TextView) view.findViewById(R.id.creattime_Tv);
        tvLocaltion = (TextView) view.findViewById(R.id.localtion_Tv);
        return view;
    }

    @Override
    public void refreshView(int position) {
        ArrayList<PictureInfo> pictureInfos = data.pictureInfos;
        if (pictureInfos != null && pictureInfos.size() > 0) {

            ImageLoadUtils.rectangleImage(
                    GlobalConstants.Base_imgae_url + data
                            .pictureInfos.get(0).pictureName.trim(), ivImage);
        }else {
            ImageLoadUtils.rectangleImage(R.drawable
                    .image_51_1, ivImage);
        }

        tvTitle.setText(data.title);
        tvPrice.setText("￥" + data.price);
        //设置类型
        tvType.setText("");
        tvCreattime.setText(TimeUtils.setCreatTime(data.createTime));


        //地址




        StringBuilder builder = new StringBuilder();

        builder.append(TextUtils.isEmpty(data.city) ? "" : data.city + "/");
        builder.append(TextUtils.isEmpty(data.county) ? "" : data.county+ "/");
        builder.append(TextUtils.isEmpty(data.town) ? "" : data.town);
        tvLocaltion.setText(builder.toString());

    }
}
