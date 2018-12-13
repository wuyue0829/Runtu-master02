package com.mac.runtu.holder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.javabean.SpecialtyGoodDetailInfo;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.LogUtils;

import java.util.ArrayList;

/**
 * Description:特产电商 通用的holder
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/5 0005 下午 7:08
 */
public class SpecialtyGoodAllHolder extends
        BaseHolder<SpecialtyGoodDetailInfo> {

    private ImageView ivImage;
    private TextView  tvTitle;
    private TextView  tvAddressType;
    private TextView  tvType;
    private Button    btnProductDetail;
    private TextView  tvPrice;
    private TextView  tvPeopleGet;

    public SpecialtyGoodAllHolder(Context context) {
        super(context);
    }

    @Override
    public View initView() {

        View view = View.inflate(context, R.layout.specialty_item_layout,
                null);

        ivImage = (ImageView) view.findViewById(R.id.imageView);
        tvTitle = (TextView) view.findViewById(R.id.title_Tv);
        tvAddressType = (TextView) view.findViewById(R.id.address_Tv);
        tvType = (TextView) view.findViewById(R.id.type_Tv);
        btnProductDetail = (Button) view.findViewById(R.id.product_details_Bt);

        tvPrice = (TextView) view.findViewById(R.id.price_Tv);
        tvPeopleGet = (TextView) view.findViewById(R.id.quantity_sold_Tv);

        return view;
    }

    @Override
    public void refreshView(int position) {
        ArrayList<PictureInfo> pictureInfos = data.pictureInfos;
        if (pictureInfos != null && pictureInfos.size() > 0) {
            for(PictureInfo pictureInfo :pictureInfos){
                if(pictureInfo.pictureType == 1){
                    ImageLoadUtils.rectangleImage( GlobalConstants
                            .Base_imgae_url + pictureInfo.pictureName.trim(), ivImage);
                }
            }
        }
        tvTitle.setText(data.name);

        tvAddressType.setText("" + data.districtName);
       /* BusinessTypeInfo typeInfo = data.businessTypeInfo;
        if (typeInfo != null) {
            tvType.setText(data.businessTypeInfo.typeName);

            LogUtils.e("特产电商","类型typeName="+data.businessTypeInfo.typeName);
        }
*/

        tvType.setText("" + data.kindName);

        LogUtils.e("特产电商","类型kindName="+data.kindName);

        tvPrice.setText("￥" + data.minPrice);
        tvPeopleGet.setText(data.payment + "人收货");

       /* btnProductDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        SpecialtyDetailsActivity.class);
                MarkerConstants.specialty_good_uuid = data.uuid;
                context.startActivity(intent);
            }
        });*/
    }
}
