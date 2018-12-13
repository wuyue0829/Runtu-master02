package com.mac.runtu.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.javabean.RuralTourismListInfo;
import com.mac.runtu.utils.Content2StrUtils;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.StringUtils;

import java.util.Iterator;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/26 0026 上午 10:23
 */
public class RuralTourismHolder extends BaseHolder<RuralTourismListInfo> {
    private static final String TAG = "RuralTourismHolder";

    private ImageView ivImage;
    private TextView  tvTiltle;
    private TextView  tvConent;
    private TextView  tvLocaltionAddress;
    private TextView  tvTourType;

    public RuralTourismHolder(Context context) {
        super(context);
    }

    @Override
    public View initView() {

        View view = View.inflate(context, R.layout
                .rural_tourism_list_item_layout, null);


        ivImage = (ImageView) view.findViewById(R.id.imageView);

        tvTiltle = (TextView) view.findViewById(R.id.title_Tv);
        tvConent = (TextView) view.findViewById(R.id.content_Tv);


        tvTourType = (TextView) view.findViewById(R.id.tour_type_tv);

        //当地地址
        tvLocaltionAddress = (TextView) view.findViewById(R.id.localtion_Tv);
        return view;
    }

    @Override
    public void refreshView(int position) {
        if (data.pictureInfos.size() > 0) {
            LogUtils.e(TAG, "图片地址" + GlobalConstants.Base_imgae_url+   data.pictureInfos.toString());

            Iterator localIterator = ((RuralTourismListInfo)this.data).pictureInfos.iterator();
            while (localIterator.hasNext())
            {
                PictureInfo localPictureInfo = (PictureInfo)localIterator.next();
                if (localPictureInfo.pictureType == 1) {
                    ImageLoadUtils.rectangleImage("http://101.201.102.161/upload/" + localPictureInfo.pictureName.trim(), this.ivImage);
                }
            }
        }

        tvTiltle.setText(data.title);
        if (!TextUtils.isEmpty(data.content)) {
            Content2StrUtils.setContentStr1(data.content, tvConent);
        }

        // tvTourType.setText();
        tvLocaltionAddress.setText(data.city + "/" + data.county );
        this.tvTourType.setText(StringUtils.getRedSpots(((RuralTourismListInfo)this.data).redSpots));

    }
}
