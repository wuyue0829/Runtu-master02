package com.mac.runtu.activity.personcentre;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.PublishIsLoginActivity;
import com.mac.runtu.activity.otherActivity.ScanResultActivity;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.utils.FragmentFactory;
import com.mac.runtu.utils.PhoneNetWordUitls;
import com.mac.runtu.utils.SPUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;
import com.nineoldandroids.view.ViewHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人订单
 */
public class OrderActivity extends PublishIsLoginActivity {

    private static final String TAG = "OrderActivity";

    public static final int TAB_finish = 4;
    public static final int TAB_order_all = 0;
    public static final int TAB_wait_get_good = 3;
    public static final int TAB_wait_pay = 1;
    public static final int TAB_wait_set_good = 2;


    @BindView(R.id.back_Iv)
    ImageView backIv;

    @BindView(R.id.ll_serach)
    LinearLayout llSerach;
    //搜索
    @BindView(R.id.search_Tv)
    TextView     searchTv;
    //全部订单
    @BindView(R.id.order_all_Tv)
    TextView     orderAllTv;

    @BindView(R.id.tv_wait_set_good)
    TextView tvWaitSetGood;

    //待付款
    @BindView(R.id.pending_payment_order_Tv)
    TextView    pendingPaymentOrderTv;
    //待收货
    @BindView(R.id.receipt_of_goods_order_Tv)
    TextView    receiptOfGoodsOrderTv;
    //已完成
    @BindView(R.id.completed_order_Tv)
    TextView    completedOrderTv;
    //指示器
    @BindView(R.id.view_indictor)
    View        viewIndictor;
    @BindView(R.id.fl_order_content)
    FrameLayout flOrderContent;
    @BindView(R.id.iv_scan)
    ImageView   ivScan;


    private String value_mode_filter_4one;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        value_mode_filter_4one = intent.getStringExtra(GlobalConstants
                .key_model);

        SPUtils.setString(this, GlobalConstants.key_model,
                value_mode_filter_4one);


        initData();

    }


    private void initData() {
        //给ViewPager设置数据

        if (!PhoneNetWordUitls.isNetworkConnected(this)) {
            ToastUtils.makeTextShow(this, "无可用网络");
        }


        //获取手机屏幕的宽度
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();

        //设置指示器的宽度为屏幕的
        viewIndictor.getLayoutParams().width = width / 5;

        //全部订单
        orderAllTv.getLayoutParams().width = width / 5;
        tvWaitSetGood.getLayoutParams().width = width / 5;
        //待付款
        pendingPaymentOrderTv.getLayoutParams().width = width / 5;


        receiptOfGoodsOrderTv.getLayoutParams().width = width / 5;
        //已完成

        completedOrderTv.getLayoutParams().width = width / 5;

        //设置默认的页面
        //updateTabs(TAB_order_all);

        showFragment(TAB_order_all);

    }

    private void showFragment(int position) {
        Fragment orderFragment = FragmentFactory.getOrderFragment(position);

        FragmentManager fm = getSupportFragmentManager();
        // 3. 获取fragment的事务
        FragmentTransaction ft = fm.beginTransaction();
        // 4. 设置Fragment的布局
        ft.replace(R.id.fl_order_content, orderFragment);

        // 5. 提交
        ft.commit();

        upDataUiIndictor(position);
    }


    @OnClick({R.id.back_Iv, R.id.iv_scan, R.id.ll_serach, R.id.order_all_Tv,
            R.id
                    .pending_payment_order_Tv, R.id
            .tv_wait_set_good, R.id
            .receipt_of_goods_order_Tv, R.id.completed_order_Tv, R.id
            .view_indictor})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;

            case R.id.iv_scan:

                //LogUtils.e(TAG,"扫描按钮");
                UiUtils.openScan4Activity(this);
                break;
            case R.id.ll_serach:
                //搜索条
                startActivity(new Intent(OrderActivity.this,
                        UserOrderSearchActivity.class));
                break;
            case R.id.order_all_Tv:


                showFragment(TAB_order_all);
                break;
            case R.id.pending_payment_order_Tv:


                showFragment(TAB_wait_pay);
                break;

            case R.id.tv_wait_set_good:
                //待发货

                showFragment(TAB_wait_set_good);
                break;

            case R.id.receipt_of_goods_order_Tv:

                showFragment(TAB_wait_get_good);
                break;
            case R.id.completed_order_Tv:

                showFragment(TAB_finish);
                break;
            case R.id.view_indictor:

                break;
        }
    }


    private void upDataUiIndictor(int position) {
        int width = viewIndictor.getWidth();

        //  起始位置：页面的位置 * 指示器的宽度
        int startIdex = position * width;

        //设置指示器的位置
        ViewHelper.setTranslationX(viewIndictor, startIdex);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
    }

    public String getValueMode() {
        return value_mode_filter_4one;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult
                (requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {

                ToastUtils.makeTextShow(getApplication(), "暂无扫描结果");

            } else {
                // ScanResult 为获取到的字符串
                String ScanResult = intentResult.getContents();
                //tvResult.setText(ScanResult);
                scanResultDispose(ScanResult);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 扫描结果处
     */
    private void scanResultDispose(String scanResult) {
        // ScanResult 为获取到的字符串

        //tvResult.setText(ScanResult);
        //Log.e(TAG, "ScanResult=" + ScanResult);

        if (scanResult.contains("http")) {

            Uri uri = Uri.parse(scanResult);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);

        } else {
            Intent intent = new Intent(OrderActivity.this, ScanResultActivity
                    .class);
            intent.putExtra(GlobalConstants.key_info, scanResult);
            startActivity(intent);
        }

    }
}
