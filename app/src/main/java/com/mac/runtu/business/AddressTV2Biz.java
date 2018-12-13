package com.mac.runtu.business;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.adapter.AddressShow4Adapter;
import com.mac.runtu.databasehelper.AddressDataInfo;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.interfaceif.OnAddressDataListener;

import java.util.ArrayList;

/**
 * Description: 以市开始
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/5 0005 下午 4:47
 */
public class AddressTV2Biz {

    private Context     context;
    private PopupWindow pop;
    private ListView    listView;

    private ArrayList<AddressDataInfo> mCitylist;  //省
    private ArrayList<AddressDataInfo> mAreaList;  //市
    private ArrayList<AddressDataInfo> mTownList;  //县


    private TextView tvArea;
    private TextView tvCity;




    //标记点击位置
    private int DOWN_POSITON = 0;


    public AddressTV2Biz(Context context, TextView tvCity, TextView
            tvArea) {
        this.context = context;

        this.tvCity = tvCity;
        this.tvArea = tvArea;
        //this.tvArea = tvArea;

        //禁用图片点击

        listView = (ListView) View.inflate(context, R.layout.listview_address_4,
                null);

        getAddressDataCity("27");


        selectAddress();
    }

    //下拉框
    private void showPopUpWindow(TextView view) {

        pop = new PopupWindow(listView, view.getWidth(), WindowManager
                .LayoutParams.WRAP_CONTENT,
                true);

        pop.setBackgroundDrawable(new ColorDrawable());

        pop.showAsDropDown(view);// 将popupwindown显示在某一个View的正下方

    }

    //从网络地址加载数据
    public void getAddressDataCity(String parentCode) {

        NetWorkAddressBiz.getAddressDataFromNet(parentCode, new
                OnAddressDataListener() {

                    @Override
                    public void onSuccess(ArrayList<AddressDataInfo> info) {
                            mCitylist = info;

                            //DOWN_POSITON = 0;

                            initDataCitylist();

                    }

                    @Override
                    public void onfail() {

                    }
                });
    }

    //省
    public void initDataCitylist() {


        String district_code ="";
        String district_name ="无";

        if (mCitylist.size()>0) {
            AddressDataInfo addressDataInfo = mCitylist.get
                    (0);

            district_code = addressDataInfo.district_code;
            district_name = addressDataInfo.district_name;
        }

        tvCity.setText(district_name);
        MarkerConstants.value_city = district_code;

        //地址被选中通知activity
        //mListener.onSelect();
        //获得县
        getAddressDataArea(district_code);

    }


    public void getAddressDataArea(String district_code) {
        NetWorkAddressBiz.getAddressDataFromNet(district_code, new
                OnAddressDataListener() {

                    @Override
                    public void onSuccess(ArrayList<AddressDataInfo> info) {

                            mAreaList = info;

                            //DOWN_POSITON = 1;

                            initDataArealist();

                    }

                    @Override
                    public void onfail() {

                    }
                });
    }

    //市
    public void initDataArealist() {

        String district_code ="";
        String district_name ="无";

        if (mAreaList.size()>0) {
            AddressDataInfo addressDataInfo = mAreaList.get
                    (0);
            district_code = addressDataInfo.district_code;
            district_name =  addressDataInfo.district_name;
        }

        //存入县的code
        tvArea.setText(district_name);
        MarkerConstants.value_county = district_code;
        //获得镇


    }



    //初始化监听
    public void selectAddress() {

        //省
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCitylist != null && mCitylist.size() > 0) {
                    listView.setAdapter(new AddressShow4Adapter
                            (context,
                                    mCitylist));

                    showPopUpWindow(tvCity);
                    DOWN_POSITON = 0;

                }
            }
        });

        //市
        tvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //得到文本框中输入的内容
                if (mAreaList != null && mAreaList.size() > 0) {
                    listView.setAdapter(new AddressShow4Adapter
                            (context,
                                    mAreaList));

                    showPopUpWindow(tvArea);
                    DOWN_POSITON = 1;
                }


            }
        });



        //点击事件
        listViewSelect();
    }

    private void listViewSelect() {
        //点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {

                switch (DOWN_POSITON) {

                    case 0:
                        setCity(position);
                        break;

                    case 1:
                        setArea(position);
                        break;


                }
                pop.dismiss();
            }
        });
    }


    //设置省
    private void setCity(int position) {

        if (mCitylist != null && mCitylist.size() > 0) {

            AddressDataInfo info =
                    mCitylist.get(position);
            tvCity.setText(info.district_name);

            MarkerConstants.value_city = info.district_code;
            getAddressDataArea(info.district_code);
        }
    }

    //自动
    private void setArea(int position) {
        //解封ivclist
        if (mAreaList != null && mAreaList.size() > 0) {
            //自动设置市
            AddressDataInfo info =
                    mAreaList.get(position);
            //市
            tvArea.setText(info.district_name);
            //得到县区
            //mTownList = areaBean.tList;
            MarkerConstants.value_county = info.district_code;

        }

    }



}
