package com.mac.runtu.activity.personcentre;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.PublishIsLoginActivity;
import com.mac.runtu.business.LoginBiz;
import com.mac.runtu.business.MyShowDialog;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.UserInfo;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.PhoneNetWordUitls;
import com.mac.runtu.utils.SPUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人中心
 */
public class PersonalCenterActitity extends PublishIsLoginActivity {

    private static final String  TAG               = "PersonalCenterActitity";
    private static final int     requestCode_login = 0;
    private              boolean orderCountGetOne  = true;


    ImageView profileImage;

    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.manage_site_Iv)
    ImageView manageSiteIv;
    @BindView(R.id.order_details_Tr)
    TableRow  orderDetailsTr;
    @BindView(R.id.personal_center_Tr)
    TableRow  personalCenterTr;
    @BindView(R.id.my_release_Tr)
    TableRow  myReleaseTr;

    @BindView(R.id.setting_Tr)
    TableRow settingTr;


    //联系客服
    @BindView(R.id.tr_service)
    TableRow serviceTr;
    //动态书写客服
    @BindView(R.id.tv_service)
    TextView tvService;

    //总订单
    @BindView(R.id.total_order_Tv)
    TextView totalOrderTv;
    //完成的
    @BindView(R.id.complete_order_Tv)
    TextView completeOrderTv;

    //未完成
    @BindView(R.id.incomplete_order_Tv)
    TextView incompleteOrderTv;

    @BindView(R.id.view)
    View view;

    //领养
    @BindView(R.id.adopt_Tr)
    TableRow adoptTr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        ButterKnife.bind(this);

        //使用事件总线模式   注册时间
        EventBus.getDefault().register(this);

        profileImage = (ImageView) findViewById(R.id.profile_image);

        //更新数据的广播
        tvService.setText("联系客服  " + GlobalConstants.service_phone);

        getUserInfo();
        getOrderCount();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setOrederUiData();

        String imageUrl = SPUtils.getString(UiUtils.getContext(),
                GlobalConstants.SP_use_iamgepath, "");

        ImageLoadUtils.roundImage(imageUrl, profileImage);

        LogUtils.e(TAG + "网路图片路径", imageUrl);

    }

    /**
     * 访问网路获得用户信息
     */
    private void getUserInfo() {

        LoginBiz.getInstance().getUserInfo4Net(new LoginBiz.OnDataListener() {
            @Override
            public void isSuccess(UserInfo.UserDesInfo info) {

                if (info.headimg ==null) {
                   return;
                }


                ImageLoadUtils.roundImage( GlobalConstants.Base_imgae_url +
                        info.headimg.trim(), profileImage);

                LogUtils.e(TAG + "网路图片路径", GlobalConstants.Base_imgae_url +
                        info.headimg.trim());

            }

            @Override
            public void onNetWorkFail() {

            }
        });
    }

    private void getOrderCount() {

        String ordercount = SPUtils.getString(this,
                GlobalConstants.sp_use_ordercount, "0");

        if (TextUtils.isEmpty(ordercount)||"0".equals(ordercount)) {
            LoginBiz.getInstance().getUserOrdrData(new LoginBiz.OnOrderListener() {
                @Override
                public void isSuccess() {
                    setOrederUiData();
                }

                @Override
                public void onNetWorkFail() {

                }
            });
        }
    }


    private void setOrederUiData() {


        String ordercount = SPUtils.getString(this,
                GlobalConstants.sp_use_ordercount, "0");

        ordercount = TextUtils.isEmpty(ordercount) ? "0" : ordercount;
        totalOrderTv.setText("订单总数:" + ordercount + "单");

        String finishcount = SPUtils.getString(this,
                GlobalConstants.sp_use_finishcount, "0");

        finishcount = TextUtils.isEmpty(finishcount) ? "0" : finishcount;
        completeOrderTv.setText("完成订单:" + finishcount + "单");


        String unfinishcount = SPUtils.getString(this,
                GlobalConstants.sp_use_unfinishcount, "0");
        unfinishcount = TextUtils.isEmpty(unfinishcount) ? "0" : unfinishcount;
        incompleteOrderTv.setText("未完成订单:" + unfinishcount + "单");


    }


    @OnClick({R.id.back_Iv, R.id.profile_image, R.id.manage_site_Iv, R.id
            .order_details_Tr,
            R.id.personal_center_Tr, R.id.my_release_Tr, R.id.adopt_Tr, R.id
            .setting_Tr, R
            .id.tr_service})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;

            case R.id.profile_image:
                //个人图片点击

                if (!UiUtils.isLogin()) {
                    UiUtils.go2LoginAcivity();
                    return;
                }

                startActivity(new Intent(PersonalCenterActitity.this,
                        PersonalInfoActivity.class));
                break;
            case R.id.manage_site_Iv:
                if (!PhoneNetWordUitls.isNetworkConnected(this)) {
                    ToastUtils.makeTextShow(this, "请打开网络!");
                    return;
                }

                //收货地址
                startActivity(new Intent(PersonalCenterActitity.this,
                        ManageReceiptAddressActivity.class));
                break;
            case R.id.order_details_Tr:
                //订单详情
                if (!PhoneNetWordUitls.isNetworkConnected(this)) {
                    ToastUtils.makeTextShow(this, "请打开网络!");
                    return;
                }
                Intent intent = new Intent(PersonalCenterActitity.this,
                        OrderActivity.class);

                intent.putExtra(GlobalConstants.key_model, GlobalConstants
                        .value_mode_filter_specialty);
                startActivity(intent);

                break;
            case R.id.personal_center_Tr:
                //个人资料
                /*startActivity(new Intent(PersonalCenterActitity.this,
                        PersonalCenterSettingActivity.class));*/
                //判断是否有网络


                startActivity(new Intent(PersonalCenterActitity.this,
                        PersonalInfoActivity.class));

                break;
            case R.id.my_release_Tr:
                //我的发布
                if (!PhoneNetWordUitls.isNetworkConnected(this)) {
                    ToastUtils.makeTextShow(this, "请打开网络!");
                    return;
                }
                startActivity(new Intent(this, UserPublishActivity.class));
                break;

            case R.id.adopt_Tr:
                //我的领养
                //订单详情
                if (!PhoneNetWordUitls.isNetworkConnected(this)) {
                    ToastUtils.makeTextShow(this, "请打开网络!");
                    return;
                }

                Intent intent1 = new Intent(PersonalCenterActitity.this,
                        OrderActivity.class);

                intent1.putExtra(GlobalConstants.key_model, GlobalConstants
                        .value_mode_filter_farm);
                startActivity(intent1);

                break;

            case R.id.setting_Tr:
                //设置中心
                startActivity(new Intent(PersonalCenterActitity.this,
                        PersonalCenterSettingActivity.class));

                break;


            case R.id.tr_service:

                MyShowDialog.ShowCallPhone(PersonalCenterActitity.this,
                        GlobalConstants
                        .service_phone,"客服人员");

                break;


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //使用事件总线模式   注册时间
        EventBus.getDefault().unregister(this);
    }

    /**
     * 访问网路得到用户订单
     */
    private void getUserOrderCount() {
        LoginBiz.getInstance().getUserOrdrData(new LoginBiz.OnOrderListener() {

            @Override
            public void isSuccess() {

                setOrederUiData();


            }

            @Override
            public void onNetWorkFail() {

            }
        });
    }


    /**
     * 得到通知 更新数据
     *
     * @param mess
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(String mess) {
        if (GlobalConstants.user_Order_Updata.equals(mess)) {
            getUserOrderCount();
        }
    }
}
