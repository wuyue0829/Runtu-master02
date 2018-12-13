package com.mac.runtu.holder;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.BusinessDataDetailInfo;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.utils.Content2StrUtils;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Description: 农户的holder的holder
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/25 0025 下午 3:13
 */
public class FramDataListHolder extends BaseHolder<BusinessDataDetailInfo> {
    private static final String TAG = "FramDataListHolder";
    private ImageView ivImage;
    private TextView  tvTiltle;
    private TextView  tvAddressType;
    private TextView  tvType;
    private TextView  tvConent;
    private TextView  tvTime;
    private TextView  tvLocaltionAddress;

    public FramDataListHolder(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout
                .business_dynamics_list_item_layout, null);

        ivImage = (ImageView) view.findViewById(R.id.imageView);

        tvTiltle = (TextView) view.findViewById(R.id.title_Tv);
        //洛川
        tvAddressType = (TextView) view.findViewById(R.id.address_Tv);
        //富士 橘子
        tvType = (TextView) view.findViewById(R.id.type_Tv);

        tvConent = (TextView) view.findViewById(R.id.conent_Tv);
        tvTime = (TextView) view.findViewById(R.id.time_Tv);

        //当地地址
        tvLocaltionAddress = (TextView) view.findViewById(R.id.localtion_Tv);

        return view;
    }

    @Override
    public void refreshView(int position) {
        ArrayList<PictureInfo> pictureInfos = data.pictureInfos;

        if (pictureInfos != null && pictureInfos.size() > 0) {

            LogUtils.e(TAG, "客商动态图片" + GlobalConstants
                    .Base_imgae_url + data
                    .pictureInfos.get(0).pictureName);

            ImageLoadUtils.rectangleImage( GlobalConstants
                    .Base_imgae_url + data
                    .pictureInfos.get(0).pictureName.trim(), ivImage);
        } else {
            ImageLoadUtils.rectangleImageId((Activity) context, R.drawable
                    .image_9, ivImage);
        }

        if (!TextUtils.isEmpty(data.title)) {
            tvTiltle.setText(data.title);
        }

        tvAddressType.setText(data.city);
        tvType.setText(data.kindDictionaryInfo.kindName);

        Content2StrUtils.setContentStr(data.content, tvConent);


        tvTime.setText(TimeUtils.setCreatTime(data.createTime));
        //"/" + data.town
        tvLocaltionAddress.setText(data.city + "/" + data.county);
    }
}
