package com.mac.runtu.activity.otherActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.personcentre.ManageReceiptAddressActivity;
import com.mac.runtu.adapter.MyBaseAdapter;
import com.mac.runtu.business.LoginBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.javabean.OrderPayCommitDetailInfo;
import com.mac.runtu.javabean.OrderPayCommitInfo;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.javabean.ShoppCarlistDetailInfo;
import com.mac.runtu.javabean.UserGetGoodAddressDetailInfo;
import com.mac.runtu.javabean.UserGetGoodAddressInfo;
import com.mac.runtu.utils.Arith;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;
import com.mac.runtu.view.MyListView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 生成订单页面
 */
public class OrderPayActivity extends PublishIsLoginActivity {

    private static final String TAG = "OrderPayActivity";
    @BindView(R.id.back_Iv)
    ImageView      backIv;
    @BindView(R.id.relativeLayout1)
    RelativeLayout relativeLayout1;
    @BindView(R.id.tv_people_name)
    TextView       tvPeopleName;
    @BindView(R.id.tv_phone)
    TextView       tvPhone;
    @BindView(R.id.address_Tv)
    TextView       addressTv;
    @BindView(R.id.change_Tv)
    TextView       changeTv;
    @BindView(R.id.linearLayout1)
    LinearLayout   linearLayout1;
    @BindView(R.id.lv_order_list)
    MyListView     lvOrderList;
    @BindView(R.id.total_commodity_price_Tv)
    TextView       totalCommodityPriceTv;
    @BindView(R.id.distribution_mode_Tv)
    TextView       distributionModeTv;
    @BindView(R.id.postage_Tv)
    TextView       postageTv;
    @BindView(R.id.total_price_Tv)
    TextView       totalPriceTv;

    @BindView(R.id.table_LL)
    TableLayout tableLL;

    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.tv_content)
    TextView     tvShowLoadingContent;


    @BindView(R.id.btn_creat_order)
    Button      btnCreatOrder;
    @BindView(R.id.btn_addGetGoodAddress)
    Button      btnAddGetGoodAddress;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    private float mAllPayMonery = 0.00f;

    private float mAllPayfinalMonery = 0.00f;

    private boolean isNoHaveDefaultAddress = true;
    private boolean isGo2AddressPage       = false;


    private PayMoneyListAdapter          mAdapter;
    private UserGetGoodAddressDetailInfo mAddressSelect;
    private UserGetGoodAddressDetailInfo mAddressSelectResult;


    private ArrayList<UserGetGoodAddressDetailInfo> infoList;
    private ArrayList<ShoppCarlistDetailInfo>       mShopCarInfoList;
    private ArrayList<Float>                        mAllMoneryList;

    private int    addressCode = 1;
    private String mModel      = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mModel = intent.getStringExtra(GlobalConstants.key_model);

        mShopCarInfoList = (ArrayList<ShoppCarlistDetailInfo>) intent
                .getSerializableExtra
                        (GlobalConstants.key_shop_car_info);

        btnAddGetGoodAddress.setVisibility(View.GONE);

        initData();

    }


    private void initData() {
        //访问网络得到地址
        getAddressData();

        //购物车传过来的集合数据
        mAdapter = new PayMoneyListAdapter
                (OrderPayActivity.this, mShopCarInfoList);
        lvOrderList.setAdapter(mAdapter);

        //遍历集合得到总价
        updataMonery();

    }

    private void updataMonery() {

        mAllPayMonery = 0.00f;

        mAllPayfinalMonery = 0.00f;

        //每次更新数据 新创建集合
        mAllMoneryList = new ArrayList<>();
        for (int i = 0; i < mShopCarInfoList.size(); i++) {

            mAllMoneryList.add(mShopCarInfoList.get(i).allPay);
            //算商品的总价
            //mAllPayMonery += mShopCarInfoList.get(i).allPay;
        }

        mAllPayMonery = Arith.add(mAllMoneryList);


        totalCommodityPriceTv.setText("￥" + mAllPayMonery + "元");

        distributionModeTv.setText("商家包邮");

        postageTv.setText("￥" + 0.00 + "元");

        mAllPayfinalMonery = mAllPayMonery + 0.00f;//到时加油费

        totalPriceTv.setText("￥" + mAllPayfinalMonery + "元");

        //LogUtils.e(TAG, "总钱数=" + mAllPayfinalMonery);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //更新地址
        if (isGo2AddressPage) {

            setAddressData();
        }

    }

    public void getAddressData() {

        MyHttpUtils.getStringDataFromNet(GlobalConstants
                        .user_get_good_address_url, null, GlobalConstants
                        .KEY_USERID,
                UiUtils.getUserID(), new MyHttpUtils
                        .OnNewWorkRequestListener() {


                    @Override
                    public void onNewWorkSuccess(String result) {
                        //解析字符创成为对象
                        if (result != null) {

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


            for (int i = 0; i < infoList.size(); i++) {
                //默认地址
                if (infoList.get(i).status == 1) {
                    mAddressSelect = infoList.get(i);
                    setAddressData();

                    //没有默认地址
                    isNoHaveDefaultAddress = false;
                    return;
                } else if (isNoHaveDefaultAddress && i == infoList.size() - 1) {
                    //没有默认地址 将第一条地址显示
                    mAddressSelect = infoList.get(0);
                    setAddressData();
                }
            }
        } else {
            linearLayout1.setVisibility(View.GONE);
            btnAddGetGoodAddress.setVisibility(View.VISIBLE);
            ToastUtils.makeTextShow(OrderPayActivity.this, "还没有添加过地址信息!");
        }
    }

    private void setAddressData() {

        linearLayout1.setVisibility(View.VISIBLE);
        btnAddGetGoodAddress.setVisibility(View.GONE);

        tvPeopleName.setText("收  件  人：" + mAddressSelect.contacts);
        tvPhone.setText("联系电话：" + mAddressSelect.phone);
        addressTv.setText("" + mAddressSelect.province + " " +
                mAddressSelect.city + " " + mAddressSelect.county + " " +
                mAddressSelect.consignee_address);
    }


    @OnClick({R.id.back_Iv, R.id.change_Tv, R.id.btn_creat_order, R.id
            .btn_addGetGoodAddress})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.change_Tv:
                //跳转地址列表
                isGo2AddressPage = true;
                Intent intent = new Intent(OrderPayActivity.this,
                        ManageReceiptAddressActivity.class);
                intent.putExtra(GlobalConstants.key_fromOrder,
                        isGo2AddressPage);
                startActivityForResult(intent, addressCode);
                break;
            case R.id.btn_creat_order:


                //判断地址有没有
                if (mAddressSelect == null) {
                    ToastUtils.makeTextShow(this, "你还没有选择地址");
                    return;
                }

                //封装数据
                btnCreatOrder.setEnabled(false);
                llLoading.setVisibility(View.VISIBLE);

                tvShowLoadingContent.setText("生成订单....");

                creatGoodJson();

                break;
            case R.id.btn_addGetGoodAddress:
                //跳转地址列表
                isGo2AddressPage = true;
                Intent intent2 = new Intent(OrderPayActivity.this,
                        ManageReceiptAddressActivity.class);
                intent2.putExtra(GlobalConstants.key_fromOrder,
                        isGo2AddressPage);
                startActivityForResult(intent2, addressCode);
                break;

        }
    }

    private void creatGoodJson() {

        OrderPayCommitInfo info = new
                OrderPayCommitInfo();
        info.userUuid = UiUtils.getUserID();
        info.consigneeUuid = mAddressSelect.uuid;
        info.busifee = mAllPayMonery;
        info.tranfee = 0.00f;
        info.cost = mAllPayfinalMonery;

        //是哪个付款
        //info.payModel = payModel;
        //是 特产电商还是 体验农场
        info.model = mModel;

        ArrayList<OrderPayCommitDetailInfo> orderPayInfo
                = new ArrayList<>();


        //判断
        for (int i = 0; i < mShopCarInfoList.size(); i++) {
            OrderPayCommitDetailInfo detailInfo = new
                    OrderPayCommitDetailInfo();

            detailInfo.amount = mShopCarInfoList.get(i).buyCount;

            detailInfo.busiUuid = mShopCarInfoList.get(i).busiUuid;

            detailInfo.paramUuid = mShopCarInfoList.get(i).paramUuid;

            //当前商品的总价
            detailInfo.cost = Arith.getFloat(mShopCarInfoList.get(i).allPay);

            detailInfo.shopCar = mShopCarInfoList.get(i).uuid;

            orderPayInfo.add(detailInfo);
        }

        info.busiInfo = orderPayInfo;

        String infojson = GSonUtil.obj2json(info);
        //访问网络
        getOrderString4Net(infojson);

    }

    //提交商品选择的信息到服务端
    private void getOrderString4Net(String infojson) {
        MyHttpUtils.getStringDataFromNet(GlobalConstants
                .user_get_order_detail_str_url, null, GlobalConstants
                .key_info, infojson, new MyHttpUtils.OnNewWorkRequestListener
                () {
            @Override
            public void onNewWorkSuccess(String result) {
                parseData2(result);


            }

            @Override
            public void onNewWorkError(String msg) {
                llLoading.setVisibility(View.GONE);
                ToastUtils.makeTextShowNoNet(OrderPayActivity.this);
                btnCreatOrder.setEnabled(true);
            }
        });

    }

    private void parseData2(String result) {
        LogUtils.e(TAG, "result=" + result);

        try {
            if (result != null) {

                JSONObject jb = new JSONObject(result);
                //String orderInfo = jb.getString("orderInfo");
                String orderUuid = jb.getString("orderUuid");
                //String cost = jb.getString("cost");
                //LogUtils.e(TAG, "orderInfo=" + orderInfo);
                LogUtils.e(TAG, "orderUuid=" + orderUuid);
                if (!TextUtils.isEmpty(orderUuid)) {
                    //跳转支付页
                    go2PayMonery(orderUuid);

                    //更新存储的数据
                    EventBus.getDefault().post(GlobalConstants
                            .user_Order_Updata);

                    LoginBiz.getInstance().getUserOrdrData();

                } else {
                    llLoading.setVisibility(View.GONE);
                    ToastUtils.makeTextShow(OrderPayActivity.this,
                            "生成订单失败!");
                    btnCreatOrder.setEnabled(true);
                }

            } else {
                llLoading.setVisibility(View.GONE);
                ToastUtils.makeTextShow(OrderPayActivity.this, "生成订单失败!");
                btnCreatOrder.setEnabled(true);
            }

        } catch (JSONException e) {
            llLoading.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    private void go2PayMonery(final String orderUuid) {

        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llLoading.setVisibility(View.GONE);
                        Intent intent = new Intent(OrderPayActivity.this,
                                SelectPayWayActivity.class);

                        intent.putExtra(GlobalConstants.key_uuid,
                                orderUuid);
                        intent.putExtra(GlobalConstants.key_allPay,
                                mAllPayfinalMonery);

                        MarkerConstants.activity = OrderPayActivity.this;

                        startActivityForResult(intent, 0);

                    }
                });
            }
        }.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 0) {
                finish();
            } else if (requestCode == addressCode) {
                mAddressSelect = (UserGetGoodAddressDetailInfo) data
                        .getSerializableExtra(GlobalConstants.key_object);
            }

        }

    }


    /**
     * 数据列表
     */
    class PayMoneyListAdapter extends MyBaseAdapter<ShoppCarlistDetailInfo> {


        public PayMoneyListAdapter(Context context,
                                   ArrayList<ShoppCarlistDetailInfo> data) {
            super(context, data);
        }


        @Override
        public BaseHolder getHolder(Context context) {
            return new ShoppCarHolder(context);
        }


    }


    class ShoppCarHolder extends BaseHolder<ShoppCarlistDetailInfo> {


        public TextView  tvTitle;
        public TextView  tvOnePrice;
        public TextView  tvAllNum;
        public ImageView ivImage;

        private TextView tvGoodNum;
        private Button   btnCut;
        private Button   btnAdd;
        private TextView tvProductParam;

        public ShoppCarHolder(Context context) {
            super(context);
        }

        @Override
        public View initView() {
            View view = View.inflate(OrderPayActivity.this, R.layout
                    .item_order_pay_list_view, null);


            tvTitle = (TextView) view.findViewById(R.id
                    .tv_product_name);

            tvOnePrice = (TextView) view.findViewById(R
                    .id.tv_price);

            tvAllNum = (TextView) view.findViewById(R
                    .id.tv_allNum);

            ivImage = (ImageView) view.findViewById(R.id
                    .iv_product_iamge);

            tvProductParam = (TextView) view.findViewById(R.id
                    .tv_product_param);

            tvGoodNum = (TextView) view.findViewById(R.id.tv_goodnum);

            btnCut = (Button) view.findViewById(R.id.btn_cut);
            btnAdd = (Button) view.findViewById(R.id.btn_add);
            return view;
        }


        @Override
        public void refreshView(final int position) {

            tvTitle.setText(data.businessInfo.name);
            ArrayList<PictureInfo> pictureInfos = data.pictureInfos;

            if (pictureInfos != null && pictureInfos.size() > 0) {

                ImageLoadUtils.rectangleImage((Activity) context,
                        GlobalConstants.Base_imgae_url + pictureInfos.get(0)
                                .pictureName, ivImage);
            }

            tvProductParam.setText("规格 : " + data.paramInfo.paramName);

            tvOnePrice.setText("￥" + data.paramInfo.paramPrice);

            tvGoodNum.setText(data.buyCount + "");

            tvAllNum.setText("数量: " + data.buyCount + "件");

            btnCut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.buyCount > 1) {
                        data.buyCount--;

                        tvGoodNum.setText(data.buyCount + "");
                        tvAllNum.setText("数量: " + data.buyCount + "件");

                        data.allPay = data.buyCount * data.paramInfo.paramPrice;

                        mAdapter.notifyDataSetChanged();

                        //更新总钱数
                        updataMonery();
                    }

                }
            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (data.buyCount <=
                            mShopCarInfoList.get(position).paramInfo.stock) {

                        data.buyCount++;
                        tvGoodNum.setText(data.buyCount + "");
                        tvAllNum.setText("数量: " + data.buyCount + "件");

                        data.allPay = data.buyCount * data.paramInfo.paramPrice;

                        mAdapter.notifyDataSetChanged();

                        updataMonery();
                    } else {
                        ToastUtils.makeTextShow((Activity) context, "已到最大库存数!");
                    }

                }
            });

        }
    }

}
