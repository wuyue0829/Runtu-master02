package com.mac.runtu.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.EntreTrainDetailInfo;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.utils.Content2StrUtils;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/26 0026 下午 5:18
 */
public class EnterTrainHolder extends BaseHolder<EntreTrainDetailInfo> {
    private static final String TAG = "EnterTrainHolder";

    private ImageView ivImage;
    private TextView  tvTiltle;
    private TextView  tvConent;
    private TextView  tvTime;

    public EnterTrainHolder(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout
                .entrepreneurship_training_list_item_layout, null);

        ivImage = (ImageView) view.findViewById(R.id.imageView);

        tvTiltle = (TextView) view.findViewById(R.id.title_Tv);
        tvConent = (TextView) view.findViewById(R.id.content_Tv);


        tvTime = (TextView) view.findViewById(R.id.time_Tv);

        //当地地址

        return view;
    }

    @Override
    public void refreshView(int position) {
        ArrayList<PictureInfo> pictureInfos = data.pictureInfos;
        if (pictureInfos != null &&pictureInfos.size() > 0) {

            LogUtils.e(TAG, "图片地址" + GlobalConstants.Base_imgae_url+  data.pictureInfos.toString());

            ImageLoadUtils.rectangleImage(
                    GlobalConstants.Base_imgae_url+  data
                    .pictureInfos.get(0).pictureName.trim(), ivImage);

        }


        tvTiltle.setText(data.title);
        String content = data.content;
        if (!TextUtils.isEmpty(content)) {
            Content2StrUtils.setContentStr(data.content, tvConent);
        }

        // tvTourType.setText();
        tvTime.setText(TimeUtils.setCreatTime(data.createTime));
    }
}
