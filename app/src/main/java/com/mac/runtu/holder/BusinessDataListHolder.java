package com.mac.runtu.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.javabean.BusinessDataDetailInfo;
import com.mac.runtu.utils.Content2StrUtils;
import com.mac.runtu.utils.TimeUtils;

/**
 * Description: 客商的holder
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/25 0025 下午 3:13
 */
public class BusinessDataListHolder extends BaseHolder<BusinessDataDetailInfo> {

    private ImageView ivImage;
    private TextView  tvTiltle;
    private TextView  tvAddressType;
    private TextView  tvType;
    private TextView  tvConent;
    private TextView  tvTime;
    private TextView  tvLocaltionAddress;

    public BusinessDataListHolder(Context context) {
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
       /* ArrayList<PictureInfo> pictureInfos = data.pictureInfos;
        if (pictureInfos != null && pictureInfos.size() > 0) {
            ImageLoadUtils.rectangleImage((Activity) context, data
                    .pictureInfos.get(position).picture_net_url, ivImage);
        }*/
        ivImage.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(data.title)) {
            tvTiltle.setText(data.title);
        }

        tvAddressType.setText(data.city);
        tvType.setText(data.kindDictionaryInfo.kindName);

        Content2StrUtils.setContentStr(data.content, tvConent);


        tvTime.setText(TimeUtils.setCreatTime(data.createTime));

        tvLocaltionAddress.setText(data.city + "/" + data.county + "/" + data
                .town);
    }
}
