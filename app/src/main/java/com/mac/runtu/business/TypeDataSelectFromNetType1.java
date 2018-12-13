package com.mac.runtu.business;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.mac.runtu.adapter.SpinnerTypeSelectAdapter;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.SelectTypeInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.MyHttpUtils;

import java.util.ArrayList;

/**
 * Description:类型选择 spineer l
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/30 16:08
 */
public class TypeDataSelectFromNetType1 {


    private       Context context;
    private final Spinner spType;
    //区分类型
    private       String  model;

    private ArrayList<SelectTypeInfo.SelecttypeDetailInfo> mTypeList;

    public TypeDataSelectFromNetType1(Context context, Spinner spType, String
            model) {
        this.context = context;
        this.spType = spType;
        this.model = model;
        getAddressDataType();
    }

    public void getAddressDataType() {

        MyHttpUtils.getStringDataFromNet(GlobalConstants
                        .select_type_lsit_url, null,
                GlobalConstants.key_model
                , model, new MyHttpUtils.OnNewWorkRequestListener() {


                    @Override
                    public void onNewWorkSuccess(String result) {
                        if (result != null) {

                            initDataType(result);
                            //给sp设置adapter
                            // spType.setAdapter();
                        }

                    }

                    @Override
                    public void onNewWorkError(String msg) {


                    }
                });
    }

    private void initDataType(String result) {

        SelectTypeInfo info = GSonUtil.parseJson(result,
                SelectTypeInfo.class);
        mTypeList = info.objList;

        if (mTypeList != null && mTypeList.size() > 0) {

            spType.setAdapter(new SpinnerTypeSelectAdapter(context, mTypeList));


            setItemSelectedListener();
        }

    }


    //市选择
    public void setItemSelectedListener() {

        spType.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {

                SelectTypeInfo.SelecttypeDetailInfo info =
                        mTypeList.get(position);

                MarkerConstants.value_kind = info.uuid;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
