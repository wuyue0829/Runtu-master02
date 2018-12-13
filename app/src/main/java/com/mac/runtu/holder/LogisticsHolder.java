package com.mac.runtu.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.javabean.LogisticsDetailInfo;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.TimeUtils;
import com.mac.runtu.utils.UiUtils;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/28 0028 下午 3:45
 */
public class LogisticsHolder extends BaseHolder<LogisticsDetailInfo> {

    private static final String TAG = "LogisticsHolder";

    private TextView tvTitle;
    private TextView tvState;
    private TextView tvRoute;
    private TextView tvWeight;
    private TextView tvDepartureTime;
    private TextView tvRequestsNum;
    private TextView tvHitCount;
    private TextView tvCreartTv;

    public LogisticsHolder(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout
                .logistics_information_list_item_layout, null);

        tvTitle = (TextView) view.findViewById(R.id.title_Tv);
        //已发车
        tvState = (TextView) view.findViewById(R.id.state_Tv);

        //途径
        tvRoute = (TextView) view.findViewById(R.id.tv_route);
        tvWeight = (TextView) view.findViewById(R.id.cargo_weight_Tv);
        //发车时间
        tvDepartureTime = (TextView) view.findViewById(R.id.departure_time_Tv);

        tvRequestsNum = (TextView) view.findViewById(R.id.requests_Tv);

        tvHitCount = (TextView) view.findViewById(R.id.browse_volume_Tv);

        tvCreartTv = (TextView) view.findViewById(R.id.time_Tv);

        return view;
    }

    @Override
    public void refreshView(int position) {

        tvTitle.setText(data.title);

        String carStuat = TimeUtils.getCarStuat(data.departureTime);
        if ("已发车".equals(carStuat)) {
          tvState.setBackgroundColor(UiUtils.getColor(R.color.already_expired_bg));

        }else {
            tvState.setBackgroundColor(UiUtils.getColor(R.color.already_started_bg));
        }
        tvState.setText(carStuat);
        //tvState.setText(TimeUtils.getCarStuat("2016-10-29 15:38:20"));

        if (!TextUtils.isEmpty(data.route)) {
            String[] arrAddress = data.route.split("-");
            StringBuilder builder = new StringBuilder();

            if (arrAddress.length > 1) {
                for (int i = 0; i < arrAddress.length; i++) {

                    if (i == arrAddress.length) {
                        builder.append(arrAddress[i]);
                    } else {
                        builder.append(arrAddress[i]).append(",");
                    }

                }
            } else {
                builder.append(arrAddress[0]);
            }


            tvRoute.setText(builder.toString());
        }


        tvWeight.setText(data.weight + "吨");

        tvRequestsNum.setText("已有" + data.requests + "请求");

        LogUtils.e(TAG,"发车时间==="+data.departureTime);

        tvDepartureTime.setText(TimeUtils.setDepartureTime(data.departureTime));
        //tvDepartureTime.setText(TimeUtils.setDepartureTime("2016-10-29 15:38:20"));

        tvHitCount.setText("" + data.hitCount + "人已浏览");

        tvCreartTv.setText(TimeUtils.setCreatTime(data.createTime));
    }
}
