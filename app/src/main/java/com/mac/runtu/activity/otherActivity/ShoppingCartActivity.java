package com.mac.runtu.activity.otherActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.mac.runtu.R;
import com.mac.runtu.activity.experiencefarm.ExperienceFarmAdoptDetails;
import com.mac.runtu.activity.specialtyshop.SpecialtyDetailsActivity;
import com.mac.runtu.adapter.MyBaseAdapter;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.javabean.ShoppCarlistDetailInfo;
import com.mac.runtu.javabean.ShoppCarlistInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.SPUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 购物车
 */
public class ShoppingCartActivity extends AppCompatActivity {
    private static final String TAG = "ShoppingCartActivity";

    Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    @BindView(R.id.back_Iv)
    ImageView         backIv;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;

    @BindView(R.id.totle_price_Tv)
    TextView  totlePriceTv;
    @BindView(R.id.crp_Lv)
    ListView  crpLv;
    @BindView(R.id.settlement_Iv)
    ImageView settlementIv;


    private ArrayList<ShoppCarlistDetailInfo> infoList;
    private ArrayList<ShoppCarlistDetailInfo> mSelectInfoList;
    private ShoppCarListAdapter               mAdapter;
    private LinearLayout                      llAllChoice;
    private ImageView                         ivAllChoice;

    private float   mAllPrice   = 0.00f;
    private boolean isAllSelect = false;
    private boolean isLoading;
    private boolean isRefresh;

    private String mModel;

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();

            res.updateConfiguration(newConfig, res.getDisplayMetrics());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                createConfigurationContext(newConfig);
            } else {
                res.updateConfiguration(newConfig, res.getDisplayMetrics());
            }
        }
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        //标记是特产电商还是体验农场
        mModel = intent.getStringExtra(GlobalConstants.key_model);

        LogUtils.e(TAG, "购物车model=" + mModel);

        ivAllChoice = (ImageView) findViewById(R.id.all_choice_Iv);

        llAllChoice = (LinearLayout) findViewById(R.id
                .ll_allSelect);
        ivAllChoice.setImageResource(R.drawable
                .shoping_cart_normal);

        totlePriceTv.setText("￥" + mAllPrice + "元");

        initData();

        initListener();

    }




    private void initListener() {


        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout
                .OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //获取数据代码填这里
                        if (!isLoading) {
                            initData();
                        }

                        LogUtils.e(TAG, "刷新");

                        refreshHandler.sendEmptyMessage(1);
                    }
                }, 3000);


            }


        });

        llAllChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (infoList != null && infoList.size() > 0) {

                    if (!isAllSelect) {

                        ivAllChoice.setImageResource(R.drawable
                                .shopping_cart_choise);
                        isAllSelect = true;
                        //改变所选的chockbox
                        setCheckBoxSelect();
                        addAllPay();

                    } else {
                        ivAllChoice.setImageResource(R.drawable
                                .shoping_cart_normal);
                        isAllSelect = false;
                        //改变所选的chockbox
                        setCheckBoxNoSelect();
                        totlePriceTv.setText("￥" + 0.00 + "元");
                    }

                }


            }
        });


    }

    //改变CheckBox状态
    private void setCheckBoxSelect() {
        for (int i = 0; i < infoList.size(); i++) {
            infoList.get(i).isSelect = true;
            mAdapter.notifyDataSetChanged();
        }
    }

    private void setCheckBoxNoSelect() {
        for (int i = 0; i < infoList.size(); i++) {
            infoList.get(i).isSelect = false;
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initData() {

        isLoading = true;

        MyHttpUtils.getStringDataFromNet(GlobalConstants
                        .user_shoppint_car_url, null, GlobalConstants
                        .KEY_USERID,
                UiUtils.getUserID(), GlobalConstants.key_model,
                mModel, new MyHttpUtils
                        .OnNewWorkRequestListener() {


                    @Override
                    public void onNewWorkSuccess(String result) {

                        if (result != null) {
                            parseData(result);
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {

                    }
                });
    }

    private void parseData(String result) {

        ShoppCarlistInfo info = GSonUtil.parseJson(result,
                ShoppCarlistInfo.class);
        infoList = info.objList;

        //清空chockbox集合


        if (infoList != null && infoList.size() > 0) {

            mAdapter = new
                    ShoppCarListAdapter(ShoppingCartActivity.this, infoList);
            crpLv.setAdapter(mAdapter);

            //计算所有的钱数

        } else {
            infoList = new ArrayList<>();
            mAdapter = new
                    ShoppCarListAdapter(ShoppingCartActivity.this, infoList);
            crpLv.setAdapter(mAdapter);

            //钱数归为0
            totlePriceTv.setText("￥ " + mAllPrice + "元");
        }

        isLoading = false;
    }

    @OnClick({R.id.back_Iv, R.id.settlement_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;

            case R.id.settlement_Iv:

                go2orderPayActivity();

                break;
        }
    }


    private void go2orderPayActivity() {

        if (infoList != null && infoList.size() > 0) {

            Intent intent = new Intent(ShoppingCartActivity.this,
                    OrderPayActivity.class);

            mSelectInfoList = new ArrayList<>();

            for (int i = 0; i < infoList.size(); i++) {

                if (infoList.get(i).isSelect) {
                    mSelectInfoList.add(infoList.get(i));
                }

            }

            if (mSelectInfoList.size()>0) {

                intent.putExtra(GlobalConstants.key_shop_car_info,
                        mSelectInfoList);

                intent.putExtra(GlobalConstants.key_model,mModel);

                startActivity(intent);

                //告诉支付完成时刷新数据 要不要刷新 从订单去要刷新从详情去不要刷新
                SPUtils.setBoolean(this,GlobalConstants.sp_pay_from,false);
            }
        }
    }


    /**
     * 购物车listview 适配器
     */
    class ShoppCarListAdapter extends MyBaseAdapter<ShoppCarlistDetailInfo> {


        public ShoppCarListAdapter(Context context,
                                   ArrayList<ShoppCarlistDetailInfo> data) {
            super(context, data);
        }


        @Override
        public BaseHolder getHolder(Context context) {
            return new ShoppCarHolder(context);
        }


    }

    /**
     * 列表的holder
     */
    class ShoppCarHolder extends BaseHolder<ShoppCarlistDetailInfo> {

        public TextView  tvDelete;
        public TextView  tvTitle;
        public TextView  tvTotlePrice;
        public TextView  tvGuigeCount;
        public ImageView ivImage;
        public CheckBox  cbSelect;
        public Button    btnGoodDetail;

        public ShoppCarHolder(Context context) {
            super(context);
        }

        @Override
        public View initView() {
            View view = View.inflate(ShoppingCartActivity.this, R.layout
                    .shopping_cart_item_layout, null);
            tvDelete = (TextView) view.findViewById(R.id
                    .tv_delete);
            tvTitle = (TextView) view.findViewById(R.id
                    .tv_title);
            tvTotlePrice = (TextView) view.findViewById(R
                    .id.totle_price_Tv);
            tvGuigeCount = (TextView) view.findViewById(R
                    .id.tv_guige_count);
            ivImage = (ImageView) view.findViewById(R.id
                    .order_product_Iv);
            cbSelect = (CheckBox) view.findViewById(R.id
                    .cb_select);
            btnGoodDetail = (Button) view.findViewById(R.id
                    .btn_goodDetail);

            return view;
        }

        @Override
        public void refreshView(final int position) {

            cbSelect.setChecked(data.isSelect);

            tvTitle.setText(data.businessInfo.name);
            tvTotlePrice.setText("总价: ￥" + data.allPay);
            ArrayList<PictureInfo> pictureInfos = data.pictureInfos;

            if (pictureInfos != null && pictureInfos.size() > 0) {

                ImageLoadUtils.rectangleImage((Activity) context,
                        GlobalConstants.Base_imgae_url + pictureInfos.get(0)
                                .pictureName, ivImage);
            }

            tvGuigeCount.setText("规格: " + data.paramInfo.paramName +
                    "×" +
                    data.buyCount + "件");

            //删除
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LogUtils.e(TAG, "删除购物车");

                    deleteShopCarData(data);
                }
            });

            btnGoodDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = null;

                    switch (mModel) {
                        case GlobalConstants.value_mode_filter_farm:
                            intent = new Intent(ShoppingCartActivity.this,
                                    ExperienceFarmAdoptDetails.class);
                            break;

                        case GlobalConstants.value_mode_filter_specialty:
                            intent = new Intent(ShoppingCartActivity.this,
                                    SpecialtyDetailsActivity.class);
                            break;

                    }
                    if (TextUtils.isEmpty(data.uuid)) {
                       return;
                    }
                    LogUtils.e(TAG,"体验农场的数据="+data.uuid);
                    intent.putExtra(GlobalConstants.key_uuid, data.businessInfo.uuid);
                    context.startActivity(intent);
                }
            });

            cbSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    infoList.get(position).isSelect = !infoList.get(position)
                            .isSelect;

                    addAllPay();
                }
            });


        }
    }

    private void addAllPay() {
        mAllPrice = 0.00f;
        for (int i = 0; i < infoList.size(); i++) {

            if (infoList.get(i).isSelect) {
                mAllPrice += infoList.get(i).allPay;
            }

        }
        //设置总金额
        totlePriceTv.setText("￥" + mAllPrice + "元");

        //更新全选按钮
        updataUiAllSelect();
    }

    private void updataUiAllSelect() {
        //全选的状态改变
        int j = 0;
        for (int i = 0; i < infoList.size(); i++) {
            j++;
            if (!infoList.get(i).isSelect) {
                ivAllChoice.setImageResource(R.drawable
                        .shoping_cart_normal);
                isAllSelect = false;
                j--;
            } else if (j == infoList.size()) {
                ivAllChoice.setImageResource(R.drawable
                        .shopping_cart_choise);
                isAllSelect = true;
            }
        }

    }

    private void deleteShopCarData(ShoppCarlistDetailInfo data) {

        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                .user_shoppint_car_delete_url, null, GlobalConstants
                .key_uuid, data.uuid, new MyHttpUtils
                .OnNewWorkRequestListener2Boolean() {


            @Override
            public void onNewWorkSuccess(Boolean result) {
                if (result) {
                    LogUtils.e(TAG, "删除result" + result);
                    initData();

                } else {
                    ToastUtils.makeTextShow(ShoppingCartActivity.this, "删除失败!");
                }
            }

            @Override
            public void onNewWorkError(String msg) {
                ToastUtils.makeTextShowNoNet(ShoppingCartActivity.this);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
