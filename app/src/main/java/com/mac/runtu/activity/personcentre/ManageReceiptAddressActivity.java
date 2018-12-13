package com.mac.runtu.activity.personcentre;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.PublishIsLoginActivity;
import com.mac.runtu.adapter.MyBaseAdapter;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.javabean.UserGetGoodAddressDetailInfo;
import com.mac.runtu.javabean.UserGetGoodAddressInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageReceiptAddressActivity extends PublishIsLoginActivity {

    private static final String TAG = "ManageReceiptAddressActivity";

    @BindView(R.id.address_Lv)
    ListView  addressLv;
    @BindView(R.id.add_address_Bt)
    Button    addAddressBt;
    @BindView(R.id.back_Iv)
    ImageView backIv;
    private ManageAddressAdapter                    addressAdapter;
    private String                                  mUserId;
    private ArrayList<UserGetGoodAddressDetailInfo> infoList;
    private int                                     mPosition;



    private String mOldUuid;
    private boolean isFromOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_address);
        ButterKnife.bind(this);

        mUserId = UiUtils.getUserID();
        isFromOrder = getIntent().getBooleanExtra(GlobalConstants
                .key_fromOrder, false);

        initData();


    }

    @Override
    protected void onResume() {
        super.onResume();
        //为了每次从网络获取数据
        //LogUtils.e(TAG, "onResume被调用");

        initData();
    }

    void initData() {

        //网络获得的数据

        //网络获得数据

        MyHttpUtils.getStringDataFromNet(GlobalConstants
                        .user_get_good_address_url, null, GlobalConstants
                        .KEY_USERID,
                mUserId, new MyHttpUtils.OnNewWorkRequestListener() {


                    @Override
                    public void onNewWorkSuccess(String result) {
                        //解析字符创成为对象
                        if (result != null) {

                            LogUtils.e(TAG, "地址信息result" + result);

                            parserData(result);
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {

                    }
                });


    }

    //解析数据
    private void parserData(String result) {

        UserGetGoodAddressInfo info = GSonUtil.parseJson
                (result, UserGetGoodAddressInfo.class);
        infoList = info.objList;
        if (infoList != null && infoList.size() > 0) {

            addressAdapter = new ManageAddressAdapter(this,
                    infoList);
            addressLv.setAdapter(addressAdapter);

        } else {

            //地址删除完毕 防止最后一条删除不掉(页面上还在) 页面不能刷新
            infoList = new ArrayList<>();
            addressAdapter = new ManageAddressAdapter(this,
                    infoList);
            addressLv.setAdapter(addressAdapter);

            //ToastUtils.makeTextShowNoData(ManageReceiptAddressActivity.this);
        }
    }

    @OnClick({R.id.add_address_Bt, R.id.back_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_address_Bt:
                startActivity(new Intent(ManageReceiptAddressActivity.this,
                        AddReceiptAddressActivity.class));

                break;
            case R.id.back_Iv:
                finish();
                break;
        }
    }

    public class ManageAddressAdapter extends
            MyBaseAdapter<UserGetGoodAddressDetailInfo> {


        public ManageAddressAdapter(Context context,
                                    ArrayList<UserGetGoodAddressDetailInfo
                                            > data) {
            super(context, data);
        }

        @Override
        public BaseHolder getHolder(Context context) {
            return new ManageAddressHolder(context);
        }
    }

    public class ManageAddressHolder extends
            BaseHolder<UserGetGoodAddressDetailInfo> {

        private TextView    tvConsignee;
        private TextView    tvNumber;
        private TextView    tvAddress;
        private RadioButton rbDefaultAddress;
        private TextView    tvDeleteAdress;


        public ManageAddressHolder(Context context) {
            super(context);
        }

        @Override
        public View initView() {

            View view = View.inflate(ManageReceiptAddressActivity.this, R.layout
                    .manage_address_item_layout, null);
            //收件人
            tvConsignee = (TextView) view.findViewById(R.id.consignee_Tv);
            //电话
            tvNumber = (TextView) view.findViewById(R.id.phone_num_Tv);
            //地址
            tvAddress = (TextView) view.findViewById(R.id.address_Tv);
            //默认地址按钮
            rbDefaultAddress = (RadioButton) view.findViewById(R.id
                    .set_default_addtess_Rb);
            //是否删除
            tvDeleteAdress = (TextView) view.findViewById(R.id.delete_Tv);

            return view;
        }

        @Override
        public void refreshView(final int position) {

            mPosition = position;

            tvConsignee.setText(data.contacts);

            tvNumber.setText(data.phone);

            //省 市 县
            tvAddress.setText(data.province + " " + data.city + " " + data
                    .county + " " + data.consignee_address);


            if (!isFromOrder) {
                rbDefaultAddress.setChecked(getDefaultAdress());

            } else {
                rbDefaultAddress.setText("选择地址");
                //隐藏删除按钮
                tvDeleteAdress.setVisibility(View.GONE);
            }

            tvDeleteAdress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    LogUtils.e(TAG, "删除");
                    deleteAddress2Net(data);

                }
            });

            rbDefaultAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.e(TAG, "RadiuButton被点击");

                    if (!isFromOrder) {


                        setDefaultAddress(data);

                    } else {
                        //选择地址以后
                        //关闭当前页

                        //返回特产电商付款也
                        Intent intent = new Intent();
                        intent.putExtra(GlobalConstants.key_object,data);
                        setResult(RESULT_OK,intent);

                        //将地址信息付值到全局变量
                        finish();
                    }
                }
            });


        }
    }

    //设置默认地址
    private void setDefaultAddress(UserGetGoodAddressDetailInfo data) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.KEY_USERID, mUserId);

        // hashMap.put(GlobalConstants.key_olduuid, mOldUuid);

        //得到地址的id
        hashMap.put(GlobalConstants.key_uuid, data.uuid);
        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                .user_nomal_address_url, null, hashMap, new
                MyHttpUtils.OnNewWorkRequestListener2Boolean() {


                    @Override
                    public void onNewWorkSuccess(Boolean result) {
                        if (result) {

                            initData();

                            // addressAdapter.notifyDataSetChanged();
                            //得到地址对应的id 提交服务器
                        } else {
                            ToastUtils.makeTextShow(ManageReceiptAddressActivity
                                    .this, "默认地址设置失败");

                            addressAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        ToastUtils.makeTextShowNoNet
                                (ManageReceiptAddressActivity.this);
                        addressAdapter.notifyDataSetChanged();
                    }
                });
    }

    //删除地址
    private void deleteAddress2Net(UserGetGoodAddressDetailInfo data) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.KEY_USERID, mUserId);
        //得到地址的id
        hashMap.put(GlobalConstants.key_uuid, data.uuid);

        //得到地址id 向服务器提交数据
        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                .user_delete_address_url, null, hashMap, new
                MyHttpUtils.OnNewWorkRequestListener2Boolean() {


                    @Override
                    public void onNewWorkSuccess(Boolean result) {
                        if (result) {
                            ToastUtils.makeTextShow(ManageReceiptAddressActivity
                                    .this, "删除地址成功");


                            initData();

                        } else {
                            ToastUtils.makeTextShow(ManageReceiptAddressActivity
                                    .this, "删除地址失败");
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        ToastUtils.makeTextShow
                                (ManageReceiptAddressActivity.this,
                                        "无可用网路");
                    }
                });
    }

    public boolean getDefaultAdress() {
        UserGetGoodAddressDetailInfo info = infoList
                .get(mPosition);
        if (1 == info.status) {
            //保存原地址地uuid
            mOldUuid = info.uuid;
            return true;
        } else {
            return false;
        }
    }

}
