package com.mac.runtu.business;


import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.mac.runtu.adapter.SpinnerAddressShowAdapter;
import com.mac.runtu.databasehelper.AddressDataInfo;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.interfaceif.OnAddressDataListener;
import com.mac.runtu.utils.LogUtils;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/25 0025 上午 10:36
 */
public class AddressSelectData4FromPro2 {

    private static final String TAG = "AddressSelectData4FromPro2";
    private       Context context;
    private final Spinner spCity;
    private final Spinner spArea;
    private final Spinner spTown;
    private final Spinner spVillage;
    private final Spinner spPro;

    private SpinnerAddressShowAdapter mProAdapter;
    private SpinnerAddressShowAdapter mCityAdapter;
    private SpinnerAddressShowAdapter mAreaAdapter;
    private SpinnerAddressShowAdapter mTownAdapter;
    private SpinnerAddressShowAdapter mVillageAdapter;

   // private ArrayList<AddressDataInfo> mProList     = new ArrayList<>();
    //private ArrayList<AddressDataInfo> mCityList    = new ArrayList<>();
    //private ArrayList<AddressDataInfo> mAreaList    = new ArrayList<>();
   // private ArrayList<AddressDataInfo> mTownList    = new ArrayList<>();
    //private ArrayList<AddressDataInfo> mVillageList = new ArrayList<>();


    //private ArrayList<String> parent_codeInfo = new ArrayList<>();

    public AddressSelectData4FromPro2(Context context, Spinner spPro, Spinner
            spCity, Spinner spArea, Spinner spTown, Spinner spVillage) {
        this.context = context;
        this.spPro = spPro;

        this.spCity = spCity;
        this.spArea = spArea;
        this.spTown = spTown;
        this.spVillage = spVillage;


        if (spPro != null) {
            getAddressDataPro("0");
        } else {
            getAddressDataCity("27");
        }

    }

    //从网络地址加载数据
    public void getAddressDataPro(String parentCode) {

        NetWorkAddressBiz.getAddressDataFromNet(parentCode, new
                OnAddressDataListener() {

                    @Override
                    public void onSuccess(ArrayList<AddressDataInfo> info) {
                        // mProList = info;

                        mProAdapter = new
                                SpinnerAddressShowAdapter(context, info);
                        spPro.setAdapter(mProAdapter);
                        setProOnCilckListener(info);
                    }

                    @Override
                    public void onfail() {

                    }
                });
    }

    //省选择
    public void setProOnCilckListener(final ArrayList<AddressDataInfo>
                                              mAddressInfoList) {

        spPro.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {
                String parentCode = "";

                if (mAddressInfoList.size() > 0) {
                    AddressDataInfo addressDataInfo = mAddressInfoList.get
                            (position);

                    parentCode = addressDataInfo.district_code;

                }

                MarkerConstants.value_province = parentCode;

                if (spCity != null) {
                    getAddressDataCity(MarkerConstants.value_province);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //从网络地址加载数据 市
    public void getAddressDataCity(String parentCode) {

        NetWorkAddressBiz.getAddressDataFromNet(parentCode, new
                OnAddressDataListener() {

                    @Override
                    public void onSuccess(ArrayList<AddressDataInfo> info) {

                        LogUtils.e(TAG,"获取数据1.1");

                        mCityAdapter = new SpinnerAddressShowAdapter
                                (context, info);
                        spCity.setAdapter(mCityAdapter);
                        setCityOnCilckListener(info);
                    }


                    @Override
                    public void onfail() {

                    }
                });

    }

    //市选择
    public void setCityOnCilckListener(final ArrayList<AddressDataInfo>
                                               mAddressInfoList) {

        spCity.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {

                String parentCode = "";
                if (mAddressInfoList.size() > 0) {

                    parentCode =  mAddressInfoList.get
                            (position).district_code;
                }

                MarkerConstants.value_city = parentCode;
                if (spArea != null) {
                    getAddressDataArea(parentCode);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //县
    public void getAddressDataArea(String parent_code) {
        NetWorkAddressBiz.getAddressDataFromNet(parent_code, new
                OnAddressDataListener() {

                    @Override
                    public void onSuccess(ArrayList<AddressDataInfo> info) {


                        mAreaAdapter = new SpinnerAddressShowAdapter
                                (context, info);
                        spArea.setAdapter(mAreaAdapter);
                        setAreaOnCilckListener(info);
                    }

                    @Override
                    public void onfail() {

                    }
                });
    }


    public void setAreaOnCilckListener(final ArrayList<AddressDataInfo>
                                               mAddressInfoList) {
        spArea.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {

                String parentCode = "";
                if (mAddressInfoList.size() > 0) {

                    parentCode =  mAddressInfoList.get
                            (position).district_code;
                }


                MarkerConstants.value_county = parentCode;
                //获得县

                if (spTown != null) {
                    getAddressDataTown(parentCode);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //镇
    public void getAddressDataTown(String parent_code) {

        NetWorkAddressBiz.getAddressDataFromNet(parent_code, new
                OnAddressDataListener() {

                    @Override
                    public void onSuccess(ArrayList<AddressDataInfo> info) {


                        mTownAdapter = new SpinnerAddressShowAdapter(context,
                                info);
                        spTown.setAdapter(mTownAdapter);
                        setTownOnCilckListener(info);
                    }


                    @Override
                    public void onfail() {

                    }
                });

    }

    //镇的点击事件
    public void setTownOnCilckListener(final ArrayList<AddressDataInfo>
                                               mAddressInfoList) {

        spTown.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {

                String parentCode = "";

                if (mAddressInfoList.size() > 0) {

                    parentCode = mAddressInfoList.get
                            (position).district_code;
                }

                MarkerConstants.value_town = parentCode;

                if (spVillage != null) {
                    getAddressDataVillage(parentCode);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    //村
    public void getAddressDataVillage(String parent_code) {

        NetWorkAddressBiz.getAddressDataFromNet(parent_code, new
                OnAddressDataListener() {

                    @Override
                    public void onSuccess(ArrayList<AddressDataInfo> info) {


                        mVillageAdapter = new SpinnerAddressShowAdapter
                                (context, info);
                        spVillage.setAdapter(mVillageAdapter);
                        setVillageOnCilckListener(info);
                    }

                    @Override
                    public void onfail() {

                    }
                });
    }

    public void setVillageOnCilckListener(final ArrayList<AddressDataInfo>
                                                  mAddressInfoList) {

        spVillage.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {
                String parentCode = "";
                if (mAddressInfoList.size() > 0) {
                    parentCode = mAddressInfoList.get
                            (position).district_code;
                }

                MarkerConstants.value_village = parentCode;

                //获得县
                // getAddressDataVillage(parentCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

}
