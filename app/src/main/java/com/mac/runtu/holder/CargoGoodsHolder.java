package com.mac.runtu.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.javabean.CargoDetailInfo;
import com.mac.runtu.javabean.KindDictionaryWuLiuInfo;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/28 0028 下午 7:16
 */
public class CargoGoodsHolder extends BaseHolder<CargoDetailInfo> {

    private TextView tvNum;
    private TextView tvFinalAddress;
    private TextView tvGoodType;
    private TextView tvWeight;
    private TextView tvName;
    private TextView tvNumber;

    public CargoGoodsHolder(Context context) {
        super(context);
    }

    @Override
    public View initView() {

        View view = View.inflate(context, R.layout
                .logistcs_information_shipment_list_item_layout, null);

        tvNum = (TextView) view.findViewById(R.id.tv_num);
        tvFinalAddress = (TextView) view.findViewById(R.id.tv_final_address);
        tvGoodType = (TextView) view.findViewById(R.id.tv_goods_type);
        tvWeight = (TextView) view.findViewById(R.id.tv_weight);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvNumber = (TextView) view.findViewById(R.id.tv_number);

        return view;
    }

    @Override
    public void refreshView(int position) {
        int num = position + 1;
        if (position < 9) {

            tvNum.setText("0" + num);
        } else {
            tvNum.setText("" + num);
        }

        tvFinalAddress.setText(data.targetCity);
        //tvGoodType.setText(
        KindDictionaryWuLiuInfo kind = data.kindDictionaryInfo;
        if (kind != null) {
            tvGoodType.setText(kind.kindName);
        }


        tvWeight.setText(data.weight + "");
        tvName.setText(data.publisher);
        tvNumber.setText(data.phone);

    }
}
