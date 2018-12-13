package com.mac.runtu.activity.logisticsinformation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.BaseActivity;
import com.mac.runtu.adapter.LogisticsAdapter;
import com.mac.runtu.business.AddressBiz4FromFinalPro2;
import com.mac.runtu.business.AddressBiz4FromPro3;
import com.mac.runtu.business.TypeSelectBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.LogisticsDetailInfo;
import com.mac.runtu.javabean.LogisticsInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * 物流信息
 */
public class LogisticsInformationActivity extends BaseActivity {
    private static final String TAG = "LogisticsInformationActivity";

    private TextView  tvCity;
    private TextView  tvArea;
    private ImageView ivCity;
    private ImageView ivArea;
    private TextView  tvTown;

    private ImageView ivTown;

    private TextView  tvcartype;
    private ImageView ivcartype;
    private TextView  tvFinalCity;
    private TextView  tvFinalArea;
    private TextView  tvFinalTown;
    private ImageView ivFinalCity;
    private ImageView ivFinalArea;
    private ImageView ivFinalTown;
    private LinearLayout     llSerachMenu;
    private ImageView        confirmIv;

    private TextView  tvCity2;
    private TextView  tvArea2;
    private ImageView ivCity2;
    private ImageView ivArea2;
    private TextView  tvTown2;

    private ImageView ivTown2;

    private TextView  tvcartype2;
    private ImageView ivcartype2;
    private TextView  tvFinalCity2;
    private TextView  tvFinalArea2;
    private TextView  tvFinalTown2;
    private ImageView ivFinalCity2;
    private ImageView ivFinalArea2;
    private ImageView ivFinalTown2;
    private LinearLayout     llSerachMenu2;
    private ImageView        confirmIv2;



    private boolean isLoading = false;
    private boolean isRefresh;
    private int pageNum = 1;
    private int num;
    private ArrayList<LogisticsDetailInfo> mAllList = new ArrayList<>();
    private LogisticsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //使用事件总线模式   注册时间
        EventBus.getDefault().register(this);

    }

    @Override
    protected int getTopTitleName() {
        return R.string.logistics_information;
    }

    @Override
    protected View getFloatView() {
        return getLayoutInflater().inflate(R.layout
                .logistics_information_float_menu_layout, null);
    }

    @Override
    public void initView() {

        confirmIv = ButterKnife.findById(floatView, R.id.confirm_Iv);


        //选择按钮
        tvCity = ButterKnife.findById(floatView, R.id.tv_city);
        tvArea = ButterKnife.findById(floatView, R.id.tv_area);
        tvTown = ButterKnife.findById(floatView, R.id.tv_town);

        ivCity = ButterKnife.findById(floatView, R.id.iv_city);
        ivArea = ButterKnife.findById(floatView, R.id.iv_area);
        ivTown = ButterKnife.findById(floatView, R.id.iv_town);


        tvcartype = ButterKnife.findById(floatView, R.id.tv_cartype);
        ivcartype = ButterKnife.findById(floatView, R.id.iv_cartype);

        tvFinalCity = ButterKnife.findById(floatView, R.id.tv_final_place_city);
        tvFinalArea = ButterKnife.findById(floatView, R.id.tv_final_place_area);
        tvFinalTown = ButterKnife.findById(floatView, R.id.tv_final_place_town);
        ivFinalCity = ButterKnife.findById(floatView, R.id.iv_final_place_city);
        ivFinalArea = ButterKnife.findById(floatView, R.id.iv_final_place_area);
        ivFinalTown = ButterKnife.findById(floatView, R.id.iv_final_place_town);

        //搜索条
        llSerachMenu = ButterKnife.findById(floatView, R.id.ll_serach);
        TextView tvSearch = ButterKnife.findById(floatView, R.id.search_Tv);
        tvSearch.setText(UiUtils.getString(R.string
                .logistics_information_search_hint));



        confirmIv2 = ButterKnife.findById(menuLL, R.id.confirm_Iv);
        //选择按钮
        tvCity2 = ButterKnife.findById(menuLL, R.id.tv_city);
        tvArea2 = ButterKnife.findById(menuLL, R.id.tv_area);
        tvTown2 = ButterKnife.findById(menuLL, R.id.tv_town);

        ivCity2 = ButterKnife.findById(menuLL, R.id.iv_city);
        ivArea2 = ButterKnife.findById(menuLL, R.id.iv_area);
        ivTown2 = ButterKnife.findById(menuLL, R.id.iv_town);


        tvcartype2 = ButterKnife.findById(menuLL, R.id.tv_cartype);
        ivcartype2 = ButterKnife.findById(menuLL, R.id.iv_cartype);

        tvFinalCity2 = ButterKnife.findById(menuLL, R.id.tv_final_place_city);
        tvFinalArea2 = ButterKnife.findById(menuLL, R.id.tv_final_place_area);
        tvFinalTown2 = ButterKnife.findById(menuLL, R.id.tv_final_place_town);
        ivFinalCity2 = ButterKnife.findById(menuLL, R.id.iv_final_place_city);
        ivFinalArea2 = ButterKnife.findById(menuLL, R.id.iv_final_place_area);
        ivFinalTown2 = ButterKnife.findById(menuLL, R.id.iv_final_place_town);

        //搜索条
        llSerachMenu2 = ButterKnife.findById(menuLL, R.id.ll_serach);
        TextView tvSearch2 = ButterKnife.findById(menuLL, R.id.search_Tv);
        tvSearch2.setText(UiUtils.getString(R.string
                .logistics_information_search_hint));


    }


    @Override
    public String getAdType() {
        return GlobalConstants.AD_TYPE_wului;
    }

    @Override
    protected void initDataOther() {
        new TypeSelectBiz(this, tvcartype,
                ivcartype, GlobalConstants.AD_TYPE_wului);

        new AddressBiz4FromPro3(this, tvCity, ivCity, tvArea,
                ivArea, tvTown, ivTown, null, null,null, null);

        new AddressBiz4FromFinalPro2(this, tvFinalCity,
                ivFinalCity,
                tvFinalArea, ivFinalArea, tvFinalTown, ivFinalTown, null, null, null, null);

        new TypeSelectBiz(this, tvcartype2,
                ivcartype2, GlobalConstants.AD_TYPE_wului);

        new AddressBiz4FromPro3(this, tvCity2, ivCity2, tvArea2,
                ivArea2, tvTown2, ivTown2, null, null,null, null);

        new AddressBiz4FromFinalPro2(this, tvFinalCity2,
                ivFinalCity2,
                tvFinalArea2, ivFinalArea2, tvFinalTown2, ivFinalTown2, null, null, null, null);
    }

    @Override
    protected Class<?> getReleaseActivity() {
        return LogisticsInformationRelease1Activity.class;
    }

    @Override
    protected String getUrl() {
        return GlobalConstants.Logistics_list_url;
    }

    @Override
    public HashMap<String, String> getHashMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.KEY_PAGENUM, pageNum + "");
        hashMap.put(GlobalConstants.KEY_PAGESIZE, GlobalConstants
                .value_page_size);
        return hashMap;
    }

    //解析数据
    @Override
    public void parserData(String result) {
        LogisticsInfo info = GSonUtil.parseJson(result,
                LogisticsInfo.class);
        num = info.num;

        LogUtils.e(TAG,"物流信息主页num="+num);

        ArrayList<LogisticsDetailInfo> infoList = info.objList;

        if (infoList != null && infoList.size() > 0) {


            if (isRefresh) {
                mAllList.clear();
                isRefresh = false;
            }
            mAllList.addAll(infoList);

            if (mAdapter == null) {
                mAdapter = new LogisticsAdapter
                        (LogisticsInformationActivity.this, mAllList);
                crpLv.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }

            isLoading = false;

            //写入换存
            mACache.put(cacheKey,result);

        } else {
            ToastUtils.makeTextShowNoData(LogisticsInformationActivity.this);
        }
    }

    @Override
    public void refreshData() {
        if (!isLoading) {
            pageNum = 1;
            isRefresh = true;
            initData();
        }
    }

    @Override
    public void lvScrollAction() {
        if (lastVisiblePosition == mAllList.size() - 1 +
                2 +1&&
                !isLoading && mAllList.size() < num) {

            //判断是否到达最后一条数据
            //开始加载下一页数据
            crpLv.onRefreshOpen();
            initData();
        }

    }

    @Override
    public void lvOnItemClick(int position) {
        int infoPosition = position - 2;
        if (position > 1) {
            Intent intent = new Intent(LogisticsInformationActivity
                    .this, LogisticsInformationDetailsActivity.class);
            intent.putExtra(GlobalConstants.key_uuid, mAllList.get
                    (infoPosition).uuid);
            LogUtils.e(TAG, "物理的uuid==" + mAllList.get
                    (infoPosition).uuid);
            startActivity(intent);
        }
    }


    @Override
    public void initListener() {

        llSerachMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //搜索条
                startActivity(new Intent(LogisticsInformationActivity.this,
                        LogisticsInforListActivity.class));
            }
        });

        confirmIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //列表页
                Intent intent = new Intent(LogisticsInformationActivity.this,
                        LogisticsInforListActivity.class);
                intent.putExtra(GlobalConstants.key_content_ui,
                        GlobalConstants.value_from_select);
                startActivity(intent);
            }
        });

        llSerachMenu2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //搜索条
                startActivity(new Intent(LogisticsInformationActivity.this,
                        LogisticsInforListActivity.class));
            }
        });

        confirmIv2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //列表页
                Intent intent = new Intent(LogisticsInformationActivity.this,
                        LogisticsInforListActivity.class);
                intent.putExtra(GlobalConstants.key_content_ui,
                        GlobalConstants.value_from_select);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
           refreshData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //使用事件总线模式   注册时间
        EventBus.getDefault().unregister(this);
    }

    /**
     * 得到通知 更新数据
     * @param mess
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(String mess) {
        if (GlobalConstants.send_updata_Logistics.equals(mess)) {
            refreshData();
        }
    }
}
