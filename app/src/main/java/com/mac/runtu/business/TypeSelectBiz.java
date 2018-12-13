package com.mac.runtu.business;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.SelectTypeInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 初始化类型
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/25 0025 上午 11:17
 */
public class TypeSelectBiz {
    private static final String TAG = "TypeSelectBiz";

    private PopupWindow                                    pop;
    private Context                                        context;
    private TextView                                       tv;
    private ImageView                                      imageView;
    private String                                         model;
    private ArrayList<SelectTypeInfo.SelecttypeDetailInfo> mTypeList;
    private ListView                                       listView;

    public TypeSelectBiz(Context context, TextView tv, ImageView imageView,
                         String model) {
        this.context = context;
        this.tv = tv;
        this.imageView = imageView;
        this.model = model;

        listView = (ListView) View.inflate(context, R.layout.listview_address_4,
                null);
        getAddressDataType();
        //setCity(0);
        selectAddress();
    }

    //下拉框
    private void showPopUpWindow(TextView view) {
        // 参数1：popupwindow显示的内容对象
        // 参数2：popupwindow的宽度
        // 参数3：popupwindow的高度
        // 参数4：是否可以获取焦点
        pop = new PopupWindow(listView, view.getWidth()+30, WindowManager
                .LayoutParams.WRAP_CONTENT,
                true);

        pop.setBackgroundDrawable(new ColorDrawable());

        pop.showAsDropDown(view);// 将popupwindown显示在某一个View的正下方

    }

    public void getAddressDataType() {

        MyHttpUtils.getStringDataFromNet(GlobalConstants
                        .select_type_lsit_url, null,
                GlobalConstants.key_model
                , model, new MyHttpUtils.OnNewWorkRequestListener() {


                    @Override
                    public void onNewWorkSuccess(String result) {

                        if (result != null) {
                            //得到数据解析
                            LogUtils.e(TAG, "地址  类型查询===" + result);

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

            String kindName = mTypeList.get(0).kindName;
            if (!TextUtils.isEmpty(kindName)) {
                tv.setText(kindName);
            }
        }

    }

    //初始化监听
    public void selectAddress() {

        //省
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mTypeList != null && mTypeList.size() > 0) {
                    listView.setAdapter(new MyTypeSelectAdapter
                            (context,
                                    mTypeList));

                    showPopUpWindow(tv);

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

                tv.setText(mTypeList.get(position).kindName);
                MarkerConstants.value_kind = mTypeList.get(position).uuid;
                pop.dismiss();
            }
        });
    }

    //适配器字体是 十四号的
    class MyTypeSelectAdapter extends android.widget.BaseAdapter {

        private List<SelectTypeInfo.SelecttypeDetailInfo> list;
        private Context                                   context;

        public MyTypeSelectAdapter(Context context, List<SelectTypeInfo
                .SelecttypeDetailInfo> list) {

            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {

            return list.size();

        }

        @Override
        public SelectTypeInfo.SelecttypeDetailInfo getItem(int position) {

            return list.get(position);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView tv = null;

            if (convertView == null) {
                //MainActivity.this MainActivity对象
                tv = new TextView(context);
                tv.setSingleLine();
                tv.setTextSize(14);
            } else {

                tv = (TextView) convertView;

            }

            tv.setText(getItem(position).kindName);
            return tv;
        }

    }

}
