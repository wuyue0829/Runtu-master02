package com.mac.runtu.business;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.adapter.AddressShow4Adapter;
import com.mac.runtu.databasehelper.AddressDataInfo;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.interfaceif.OnAddressDataListener;
import com.mac.runtu.utils.LogUtils;

import java.util.ArrayList;


/**
 * Description: 市县镇村五级级联动
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/10 0010 上午 9:05
 */
public class AddressBiz4FromPro3 {

    private static final String TAG = "AddressBiz4FromPro3";

    private ListView listView;

    private Context     context;
    private PopupWindow pop;

    private TextView  tvPro;
    private ImageView ivPro;

    private TextView tvCity;
    private TextView tvArea;

    private ImageView ivCity;
    private ImageView ivArea;
    private TextView  tvTown;
    private TextView  tvVillage;
    private ImageView ivTown;
    private ImageView ivVillage;


    //标记点击位置
    private int DOWN_POSITON = 0;

    //public String code;

    private ArrayList<AddressDataInfo> mProList     = new ArrayList<>();
    private ArrayList<AddressDataInfo> mCitylist    = new ArrayList<>();
    private ArrayList<AddressDataInfo> mAreaList    = new ArrayList<>();
    private ArrayList<AddressDataInfo> mTownList    = new ArrayList<>();
    private ArrayList<AddressDataInfo> mVillageList = new ArrayList<>();


    public AddressBiz4FromPro3(Context context, TextView tvPro, ImageView
            ivPro, TextView tvCity, ImageView
                                       ivCity
            , TextView tvArea, ImageView ivArea, TextView tvTown,
                               ImageView ivTown, TextView tvVillage,
                               ImageView ivVillage) {
        this.context = context;

        this.tvPro = tvPro;
        this.ivPro = ivPro;


        this.tvCity = tvCity;
        this.ivCity = ivCity;


        this.tvArea = tvArea;
        this.ivArea = ivArea;

        this.tvTown = tvTown;
        this.ivTown = ivTown;



        this.tvVillage = tvVillage;
        this.ivVillage = ivVillage;

        //禁用图片点击

        listView = (ListView) View.inflate(context, R.layout.listview_address_4,
                null);

        if (tvPro != null) {
            getAddressDataPro("0");

        } else {
            getAddressDataCity("27");
        }


        //setCity(0);
        selectAddress();
    }

    //下拉框
    private void showPopUpWindow(TextView view) {
        // 参数1：popupwindow显示的内容对象
        // 参数2：popupwindow的宽度
        // 参数3：popupwindow的高度
        // 参数4：是否可以获取焦点
        pop = new PopupWindow(listView, view.getWidth() + 30, WindowManager
                .LayoutParams.WRAP_CONTENT,
                true);

        pop.setBackgroundDrawable(new ColorDrawable());

        pop.showAsDropDown(view);// 将popupwindown显示在某一个View的正下方

    }

    //从网络地址加载数据
    public void getAddressDataPro(String parentCode) {

        NetWorkAddressBiz.getAddressDataFromNet(parentCode, new
                OnAddressDataListener() {

                    @Override
                    public void onSuccess(ArrayList<AddressDataInfo> info) {


                            initDataProlist(info);

                    }

                    @Override
                    public void onfail() {

                    }
                });
    }

    //市选择
    public void initDataProlist(ArrayList<AddressDataInfo> info) {
        mProList = info;

        String district_code ="";
        String district_name ="无";

        if (info.size()>0) {

            AddressDataInfo addressDataInfo = info.get
                    (0);

            district_code = addressDataInfo.district_code;
            district_name = addressDataInfo.district_name;


        }

        if (tvPro!=null) {
            tvPro.setText(district_name);
        }

        MarkerConstants.value_province = district_code;
        //地址被选中通知activity
        //mListener.onSelect();
        //获得县
        getAddressDataCity(district_code);

    }


    //从网络地址加载数据
    public void getAddressDataCity(String parentCode) {

        NetWorkAddressBiz.getAddressDataFromNet(parentCode, new
                OnAddressDataListener() {

                    @Override
                    public void onSuccess(ArrayList<AddressDataInfo> info) {
                        LogUtils.e(TAG,"2步");

                            initDataCitylist(info);

                    }

                    @Override
                    public void onfail() {

                    }
                });
    }

    //市选择
    public void initDataCitylist(ArrayList<AddressDataInfo> info) {

        mCitylist = info;

        String district_code ="";
        String district_name ="无";

        if (info.size()>0) {
            AddressDataInfo addressDataInfo = info.get
                    (0);

            district_code = addressDataInfo.district_code;
            district_name = addressDataInfo.district_name;
        }

        if (tvCity!=null) {
            tvCity.setText(district_name);
        }

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

                            initDataArealist2(info);


                    }

                    @Override
                    public void onfail() {

                    }
                });
    }

    //市的点击事件
    public void initDataArealist2(ArrayList<AddressDataInfo> info) {

        mAreaList = info;

        String district_code ="";
        String district_name ="无";

        if (info.size()>0) {
            AddressDataInfo addressDataInfo = info.get
                    (0);
            district_code = addressDataInfo.district_code;
            district_name =  addressDataInfo.district_name;
        }

        //存入县的code

        if (tvArea!=null) {
            tvArea.setText(district_name);
        }
        MarkerConstants.value_county = district_code;

        //获得镇

        if (tvTown != null) {
            getAddressDataTown(district_code);
        }

    }

    //得到镇的数据
    public void getAddressDataTown(String district_code) {

        NetWorkAddressBiz.getAddressDataFromNet(district_code, new
                OnAddressDataListener() {

                    @Override
                    public void onSuccess(ArrayList<AddressDataInfo> info) {
                       setnitDataTownlist(info);
                    }

                    @Override
                    public void onfail() {

                    }
                });

    }

    //镇的点击事件
    public void setnitDataTownlist(ArrayList<AddressDataInfo> info) {

        mTownList = info;
        String district_code ="";
        String district_name ="无";

        if (info.size()>0) {
            AddressDataInfo addressDataInfo = info.get
                    (0);

            district_code = addressDataInfo.district_code;

            district_name = addressDataInfo.district_name;
        }

        //存入镇的code
        MarkerConstants.value_town = district_code;

        if (tvTown!=null) {
            tvTown.setText(district_name);
        }

        //获得县
        if (ivVillage != null) {
            getAddressDataVillage(district_code);
        }


    }

    public void getAddressDataVillage(String district_code) {
        NetWorkAddressBiz.getAddressDataFromNet(district_code, new
                OnAddressDataListener() {

                    @Override
                    public void onSuccess(ArrayList<AddressDataInfo> info) {

                            initDataVillagelist(info);

                    }

                    @Override
                    public void onfail() {

                    }
                });
    }

    public void initDataVillagelist(ArrayList<AddressDataInfo> info) {

        mVillageList = info;

        String district_code ="";
        String district_name ="无";

        if (info.size()>0) {
            AddressDataInfo addressDataInfo = info.get
                    (0);

            district_code = addressDataInfo.district_code;

            district_name = addressDataInfo.district_name;
        }



        MarkerConstants.value_village = district_code;
        if (tvVillage!=null) {
            tvVillage.setText(district_name);
        }

        //获得村

    }


    //初始化监听
    public void selectAddress() {


        //省
        if (ivPro != null) {
            ivPro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mProList != null && mProList.size() > 0) {
                        listView.setAdapter(new AddressShow4Adapter
                                (context,
                                        mProList));

                        showPopUpWindow(tvPro);
                        DOWN_POSITON = 0;

                        //isOneCounty = true;

                    }
                }
            });
        }

        //省
        ivCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCitylist != null && mCitylist.size() > 0) {
                    listView.setAdapter(new AddressShow4Adapter
                            (context,
                                    mCitylist));

                    showPopUpWindow(tvCity);
                    DOWN_POSITON = 1;

                    //isOneCounty = true;

                }
            }
        });

        //市
        ivArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //得到文本框中输入的内容
                if (mAreaList != null && mAreaList.size() > 0) {
                    listView.setAdapter(new AddressShow4Adapter
                            (context,
                                    mAreaList));

                    showPopUpWindow(tvArea);
                    DOWN_POSITON = 2;
                }


            }
        });

        //区 县
        if (ivTown != null) {
            ivTown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mTownList != null && mTownList.size() > 0) {
                        listView.setAdapter(new AddressShow4Adapter
                                (context,
                                        mTownList));
                        showPopUpWindow(tvTown);

                        DOWN_POSITON = 3;
                    }


                }
            });
        }


        if (ivVillage != null) {

            ivVillage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mVillageList != null && mVillageList.size() > 0) {
                        listView.setAdapter(new AddressShow4Adapter
                                (context,
                                        mVillageList));
                        showPopUpWindow(tvVillage);

                        DOWN_POSITON = 4;

                    }


                }
            });
        }

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
                        setPro(position);
                        break;
                    case 1:
                        setCity(position);
                        break;

                    case 2:

                        setArea(position);
                        break;
                    case 3:

                        setTown(position);
                        break;
                    case 4:
                        setVillage(position);
                        break;
                }
                pop.dismiss();
            }
        });
    }

    //设置
    private void setPro(int position) {

        if (mProList != null && mProList.size() > 0) {

            AddressDataInfo info =
                    mProList.get(position);

            tvPro.setText(info.district_name);

            //得到市的集合
            //mAreaList = mCitylist.get(position).aList;

            MarkerConstants.value_province = info.district_code;


            getAddressDataCity(info.district_code);

        }
    }


    //设置
    private void setCity(int position) {

        if (mCitylist != null && mCitylist.size() > 0) {

            AddressDataInfo info =
                    mCitylist.get(position);

            tvCity.setText(info.district_name);

            //得到市的集合
            //mAreaList = mCitylist.get(position).aList;
            LogUtils.e(TAG, "次方法走了=第1步=" + info.district_name + "code=" +
                    info.district_code);

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
            getAddressDataTown(info.district_code);

        }

    }

    //自动
    private void setTown(int position) {

        if (mTownList != null && mTownList.size() > 0) {
            //解封ivArea

            AddressDataInfo info =
                    mTownList.get(position);

            //市
            tvTown.setText(info.district_name);
            //得到县区
            //mTownList = areaBean.tList;
            MarkerConstants.value_town = info.district_code;
            getAddressDataVillage(info.district_code);
        }
    }

    //制动设置村
    public void setVillage(int position) {

        if (mVillageList != null && mVillageList.size() > 0) {
            //解封ivArea

            AddressDataInfo info =
                    mVillageList.get(position);

            MarkerConstants.value_village = info.district_code;

            if (tvVillage != null) {
                tvVillage.setText(info.district_name);
            }

        }
    }



}
