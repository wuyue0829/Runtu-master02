package com.mac.runtu.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.experiencefarm.ExperienceFarmAdoptDetails;
import com.mac.runtu.activity.personcentre.OrderActivity;
import com.mac.runtu.activity.personcentre.OrderDetailsActivity;
import com.mac.runtu.activity.specialtyshop.SpecialtyDetailsActivity;
import com.mac.runtu.business.MyShowDialog;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnBooleanListener;
import com.mac.runtu.interfaceif.OnGetData2BooleanListener;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.javabean.UserCenertOrderDetailInfo;
import com.mac.runtu.javabean.UserCenertOrderInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.SPUtils;
import com.mac.runtu.utils.UiUtils;
import com.mac.runtu.view.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/21 0021 上午 10:28
 */
public abstract class BaseFragment02 extends Fragment {

    public static final String TAG = "BaseFragment02";

    // public ListView      mListView;
    public PullToRefreshListView mListView;
    public TextView              tvHint;
    //public ImageView      tvHint;
    public OrderActivity         mActivity;

    public int pageNum = 1;
    public int num;
    public ArrayList<UserCenertOrderDetailInfo> mAllList = new ArrayList<>();
    public BaseAdapter mAdapter;
    public boolean     isLoading;
    public boolean     isScroll;
    public boolean isOneStart = true;
    public  boolean      isRefresh;
    public  LinearLayout llLoading;
    public  TextView     tvContent;
    private View         view;
    private String       valueMode;
    private boolean isHaveDelete = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        mActivity = (OrderActivity) getActivity();

        valueMode = mActivity.getValueMode();


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {


        view = View.inflate(getActivity(), R.layout.fragment_base,
                null);

        //mListView = (ListView) view.findViewById(R.id.lv);
        mListView = (PullToRefreshListView) view.findViewById(R.id.lv);
        tvHint = (TextView) view.findViewById(R.id.tv_hint);
        tvHint.setVisibility(View.GONE);

        llLoading = (LinearLayout) view.findViewById(R.id.ll_loading);
        tvContent = (TextView) llLoading.findViewById(R.id.tv_content);


        //因为视频消耗内存严重,导致内存中用户的信息被系统回收,所以 在这判断

        initListener();
        initDataRefresh();

        return view;
    }


    //自类列表中 数据改变 调用次方法更新数据
    public void initDataRefresh() {

        LogUtils.e(TAG, "数据刷新");

        tvHint.setVisibility(View.GONE);
        mAdapter = null;

        pageNum = 1;

        isRefresh = true;


        initData();
    }

    public void initData() {
        isLoading = true;

        LogUtils.e(TAG, "BaseFragment02 区分体验农场=" + valueMode);

        //区分来自体验农场还是 个人中心
        //访问时参数已经区分
        if (TextUtils.isEmpty(valueMode)) {

            valueMode = SPUtils.getString(mActivity, GlobalConstants
                    .key_model, "");
        }


        HashMap<String, String> hashMap = new HashMap<>();

        String userId = SPUtils.getString(UiUtils
                .getContext(), GlobalConstants.SP_USERID, "");

        hashMap.put(GlobalConstants.KEY_USERID, userId);


        hashMap.put(GlobalConstants.KEY_PAGENUM, pageNum + "");

        hashMap.put(GlobalConstants.KEY_PAGESIZE, "6");

        hashMap.put(GlobalConstants.key_status, getOrderStatus());

        hashMap.put(GlobalConstants.key_model, valueMode);


        MyHttpUtils.getStringDataFromNet(GlobalConstants.user_order_data_url
                        .trim(),
                null, hashMap, new MyHttpUtils.OnNewWorkRequestListener() {
                    @Override
                    public void onNewWorkSuccess(String result) {

                        // LogUtils.e(TAG, "个人订单总数=======" + result);

                        mListView.onRefreshComplete();

                        if (result != null) {
                            parseData(result);
                            LogUtils.e(TAG, "订单Status" + getOrderStatus() +
                                    "=" + result);
                        }


                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        mListView.onRefreshComplete();
                    }
                });


    }


    public void parseData(String result) {
        //个人中心订单
        UserCenertOrderInfo info = GSonUtil.parseJson(result,
                UserCenertOrderInfo.class);

        num = info.num;

        ArrayList<UserCenertOrderDetailInfo> infoList = info.objList;

        if (infoList != null && infoList.size() > 0) {

            if (isRefresh) {
                //来自自类刷新
                mAllList.clear();
                isRefresh = false;
            }

            mAllList.addAll(infoList);


            if (mAdapter == null) {

                //显示数据
                setUiData();
                isOneStart = false;
            } else {
                mAdapter.notifyDataSetChanged();
            }


            isLoading = false;
            //隐藏提示
            pageNum++;
        } else {
            //ToastUtils.makeTextShowNoData(mActivity);

            //用户把订单全部删除了
            mAllList.clear();
            setUiData();
            mListView.setAdapter(mAdapter);
            tvHint.setVisibility(View.VISIBLE);
        }


    }

    public void setUiData() {
        //区分
        switch (valueMode) {

            case GlobalConstants.value_mode_filter_specialty:

                mAdapter = getUserOrderAdapter();


                break;
            case GlobalConstants.value_mode_filter_farm:

                mAdapter = getExperienceFarmOrderAdapter();

                break;

        }

        mListView.setAdapter(mAdapter);

    }

    /**
     * 体验农场的adapter
     *
     * @return
     */
    protected abstract BaseAdapter getExperienceFarmOrderAdapter();

    /**
     * 特产电商的adapter
     *
     * @return
     */
    public abstract BaseAdapter getUserOrderAdapter();

    /**
     * 订单状态
     *
     * @return
     */
    public abstract String getOrderStatus();


    public void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {


                /*switch (valueMode) {

                    case GlobalConstants.value_mode_filter_specialty:

                        Intent intent = new Intent(mActivity,
                                SpecialtyDetailsActivity.class);

                        intent.putExtra(GlobalConstants.key_model, valueMode);
                        intent.putExtra(GlobalConstants.key_uuid, mAllList
                                .get(position).busiUuid);
                        mActivity.startActivity(intent);


                        break;
                    case GlobalConstants.value_mode_filter_farm:

                        Intent intent2 = new Intent(mActivity,
                                ExperienceFarmAdoptDetails.class);

                        intent2.putExtra(GlobalConstants.key_model, valueMode);
                        intent2.putExtra(GlobalConstants.key_uuid, mAllList
                                .get(position).busiUuid);
                        mActivity.startActivity(intent2);

                        break;

                }*/

                Intent intent = new Intent(BaseFragment02.this.mActivity, OrderDetailsActivity.class);
                intent.putExtra("model", BaseFragment02.this.valueMode);
                intent.putExtra("uuid", ((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).busiUuid);
                intent.putExtra("orderStatus", ((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).orderStatus);
                intent.putExtra("pic", ((PictureInfo)((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).pictureInfos.get(0)).pictureName);
                intent.putExtra("createTime", ((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).orderInfo.getCreateTime().replace("T", " "));
                intent.putExtra("poname", ((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).businessInfo.name);
                intent.putExtra("shuliang", ((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).amount);
                intent.putExtra("cost", ((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).cost + "");
                intent.putExtra("reciveAddress", ((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).reciveAddress);
                intent.putExtra("recivePhone", ((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).recivePhone);
                intent.putExtra("reciveUser", ((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).reciveUser);
                intent.putExtra("orderCode", ((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).orderInfo.getOrderCode());
                intent.putExtra("danjia", ((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).businessParameterInfo.paramPrice);
                intent.putExtra("orderUuid", ((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).order_uuid);
                intent.putExtra("position", position);
                intent.putExtra("uuid", ((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).uuid);
                intent.putExtra("busiUuid", ((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).busiUuid);
                intent.putExtra("paramName", ((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).businessParameterInfo.paramName);
                intent.putExtra("logisticsNumber", ((UserCenertOrderDetailInfo)BaseFragment02.this.mAllList.get(position)).logisticsNumber);
                BaseFragment02.this.mActivity.startActivity(intent);



            }
        });

        mListView.setOnItemLongClickListener(new AdapterView
                .OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                LogUtils.e(TAG, "订单条目长按");
                //判断是否是已完成

                if (GlobalConstants.value_mode_filter_farm.equals(valueMode)) {

                    showHintInfo(position);
                } else {
                    ShowdeleteOrder(position);
                }

                return true;
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int
                    scrollState) {
                //滑动改变状态
                if (scrollState == SCROLL_STATE_IDLE) {//ListView空闲
                    //获取当前可见的最后一个item的位置
                    int lastVisiblePosition = mListView
                            .getLastVisiblePosition();

                    LogUtils.e(TAG, "mAllList.size()=" + mAllList.size());
                    LogUtils.e(TAG, "lastVisiblePosition=" +
                            lastVisiblePosition);
                    LogUtils.e(TAG, "getFirstVisiblePosition=" + mListView
                            .getFirstVisiblePosition());
                    LogUtils.e(TAG, "isLoading=" + isLoading);

                    //如果isLoading=true表示正在加载,还没结束,此时不允许加载下一页
                    if (lastVisiblePosition == mAllList.size() &&
                            !isLoading && mAllList.size() < num) {


                        //判断是否到达最后一条数据
                        //开始加载下一页数据

                        mListView.onRefreshOpen();

                        initData();
                    }


                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int
                    visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 弹窗提示删除
     */
    private void showHintInfo(final int position) {

        MyShowDialog.showDialogInfo(mActivity, "删除订单 :", "是否删除此条订单？", "确定",
                new OnGetData2BooleanListener() {


                    @Override
                    public void onSuccess(Boolean result) {
                        deleteOreder4Net(position);
                    }

                    @Override
                    public void onFail() {

                    }
                });
    }

    private void ShowdeleteOrder(final int position) {
        MyShowDialog.showDialogDelete(mActivity, new OnBooleanListener() {


            @Override
            public void onResultIsTrue() {
                //用户点确定

                deleteOreder4Net(position);

            }
        });
    }

    //提交订单号到网络 删除数据
    private void deleteOreder4Net(final int position) {
        UserCenertOrderDetailInfo info = mAllList.get
                (position);
        if (info != null) {
            MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                            .user_order_delete_url, null, GlobalConstants
                            .key_uuid,
                    info.uuid, new MyHttpUtils
                            .OnNewWorkRequestListener2Boolean() {


                        @Override
                        public void onNewWorkSuccess(Boolean result) {
                            if (result) {

                                isHaveDelete = true;

                                initDataRefresh();


                            }
                        }

                        @Override
                        public void onNewWorkError(String msg) {

                        }
                    });
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        //页面结束的时候发送数据 通知程序底层更新 数据
        if (isHaveDelete) {
            EventBus.getDefault().post(GlobalConstants.user_Order_Updata);
        }

        EventBus.getDefault().unregister(this);
    }

    /**
     * 得到通知 更新数据
     *
     * @param mess
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(String mess) {
        if (GlobalConstants.send_updata_Logistics.equals(mess)) {

            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    UiUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            initDataRefresh();
                        }
                    });
                }
            }.start();

        }
    }
}
