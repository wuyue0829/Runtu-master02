package com.mac.runtu.activity.laborscrvice;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.BaseActivity;
import com.mac.runtu.adapter.LaborServiceAdapter;
import com.mac.runtu.business.AddressBiz4FromPro3;
import com.mac.runtu.business.FromNetInfo2AddsSelectBiz;
import com.mac.runtu.business.TypeSelectBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.LaborServiceDetailInfo;
import com.mac.runtu.javabean.LaborServiceInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * 劳务合作
 */
public class LaborServiceCooperationActivity extends BaseActivity{

    private static final String TAG = "LaborServiceCooperationActivity";

    private int pageNum = 1;

    private ArrayList<LaborServiceDetailInfo> mAllList = new ArrayList<>();
    private boolean isSelect_address;
    private boolean isRefresh = false;

    private int                 num;
    private LaborServiceAdapter mAdapter;


    ImageView conformIv;
    //百度地图
    ImageView mapModeIv;

    private TextView  tvCity;
    private TextView  tvArea;
    private ImageView ivCity;
    private ImageView ivArea;
    private TextView  tvTown;
    private TextView  tvVillage;
    private ImageView ivTown;
    private ImageView ivVillage;

    private TextView  tvWorkType;
    private TextView  tvWorkTime;
    private ImageView ivWorkType;
    private ImageView ivWorkTime;


    ImageView conformIv2;
    //百度地图
    ImageView mapModeIv2;

    private TextView  tvCity2;
    private TextView  tvArea2;
    private ImageView ivCity2;
    private ImageView ivArea2;
    private TextView  tvTown2;
    private TextView  tvVillage2;
    private ImageView ivTown2;
    private ImageView ivVillage2;

    private TextView  tvWorkType2;
    private TextView  tvWorkTime2;
    private ImageView ivWorkType2;
    private ImageView ivWorkTime2;
    private TextView merchants_release;
    private TextView merchants_release1;



    private String[] serviceTimesName = {"上午","中午", "下午","晚上","全天"};
    private String[] serviceTimesType = {"1", "2", "3", "4", "5"};

    private ListView             mPopListView;
    private PopupWindow          pop;
    private ArrayAdapter<String> mPopAdapter;
    private RelativeLayout rl_shangren;
    private RelativeLayout rl_zhaohuo;
    private View view_shangren;
    private View view_zhaohuo;
    private String type = "0";
    private ArrayList<LaborServiceDetailInfo> infoList = new ArrayList<>();


    @Override
    protected int getTopTitleName() {
        return R.string.labor_service_cooperation;
    }

    @Override
    protected View getFloatView() {
        return getLayoutInflater().inflate(R.layout
                .labor_service_cooperation_float_menu_layout, null);
    }

    @Override
    public void initView() {
        tvCity = ButterKnife.findById(floatView, R.id.tv_city);
        tvArea = ButterKnife.findById(floatView, R.id.tv_area);
        tvTown = ButterKnife.findById(floatView, R.id.tv_town);
        tvVillage = ButterKnife.findById(floatView, R.id.tv_village);
        ivCity = ButterKnife.findById(floatView, R.id.iv_city);
        ivArea = ButterKnife.findById(floatView, R.id.iv_area);
        ivTown = ButterKnife.findById(floatView, R.id.iv_town);
        ivVillage = ButterKnife.findById(floatView, R.id.iv_village);
        rl_shangren = ButterKnife.findById(floatView, R.id.rl_shangren);
        rl_zhaohuo = ButterKnife.findById(floatView, R.id.rl_zhaohuo);
        view_shangren = ButterKnife.findById(floatView, R.id.view_shangren);
        view_zhaohuo = ButterKnife.findById(floatView, R.id.view_zhaohuo);
        merchants_release = ButterKnife.findById(floatView, R.id.merchants_release);
        merchants_release1 = ButterKnife.findById(floatView, R.id.merchants_release1);

        //工作类型时间
        tvWorkType = ButterKnife.findById(floatView, R.id.tv_worktype);
        tvWorkTime = ButterKnife.findById(floatView, R.id.tv_worktime);
        ivWorkType = ButterKnife.findById(floatView, R.id.iv_worktype);
        ivWorkTime = ButterKnife.findById(floatView, R.id.iv_worktime);


        conformIv = ButterKnife.findById(floatView, R.id.confirm_Iv);
        mapModeIv = ButterKnife.findById(floatView, R.id.map_mode_Iv);

        tvCity2 = ButterKnife.findById(menuLL, R.id.tv_city);
        tvArea2 = ButterKnife.findById(menuLL, R.id.tv_area);
        tvTown2 = ButterKnife.findById(menuLL, R.id.tv_town);
        tvVillage2 = ButterKnife.findById(menuLL, R.id.tv_village);
        ivCity2 = ButterKnife.findById(menuLL, R.id.iv_city);
        ivArea2 = ButterKnife.findById(menuLL, R.id.iv_area);
        ivTown2 = ButterKnife.findById(menuLL, R.id.iv_town);
        ivVillage2 = ButterKnife.findById(menuLL, R.id.iv_village);

        //工作类型时间
        tvWorkType2 = ButterKnife.findById(menuLL, R.id.tv_worktype);
        tvWorkTime2 = ButterKnife.findById(menuLL, R.id.tv_worktime);
        ivWorkType2 = ButterKnife.findById(menuLL, R.id.iv_worktype);
        ivWorkTime2 = ButterKnife.findById(menuLL, R.id.iv_worktime);


        conformIv2 = ButterKnife.findById(menuLL, R.id.confirm_Iv);
        mapModeIv2 = ButterKnife.findById(menuLL, R.id.map_mode_Iv);


        rl_shangren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                merchants_release.setTextColor(getResources().getColor(R.color.titlebg));
                merchants_release1.setTextColor(getResources().getColor(R.color.txt_small));
                view_shangren.setVisibility(View.VISIBLE);
                view_zhaohuo.setVisibility(View.GONE);
                type = "0";
                refreshData();
            }
        });

        rl_zhaohuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                merchants_release1.setTextColor(getResources().getColor(R.color.titlebg));
                merchants_release.setTextColor(getResources().getColor(R.color.txt_small));
                view_zhaohuo.setVisibility(View.VISIBLE);
                view_shangren.setVisibility(View.GONE);
                type = "1";
                refreshData();
            }
        });
    }


    @Override
    public String getAdType() {
        return GlobalConstants.AD_TYPE_SERVICE;
    }

    @Override
    protected void initDataOther() {
        //初始化type类型
        new TypeSelectBiz(this, tvWorkType,
                ivWorkType, GlobalConstants
                .AD_TYPE_SERVICE);

        new AddressBiz4FromPro3(this,null,null, tvCity, ivCity, tvArea,
                ivArea, tvTown, ivTown, tvVillage, ivVillage);

        //劳务时间选择
        mPopAdapter = new ArrayAdapter<String>(this, R.layout
                .pop_service_item_textview, serviceTimesName);
        mPopListView = (ListView) View.inflate(this, R.layout
                        .listview_address,
                null);

        tvWorkTime.setText(serviceTimesName[0]);
        MarkerConstants.value_service_time_type = serviceTimesType[0];


        //初始化type类型
        new TypeSelectBiz(this, tvWorkType2,
                ivWorkType2, GlobalConstants
                .AD_TYPE_SERVICE);

        new AddressBiz4FromPro3(this,null,null, tvCity2, ivCity2, tvArea2,
                ivArea2, tvTown2, ivTown2, tvVillage2, ivVillage2);

        tvWorkTime2.setText(serviceTimesName[0]);

    }

    @Override
    protected Class<?> getReleaseActivity() {
        return LaborServiceCooperationReleaseActivity.class;
    }

    @Override
    protected String getUrl() {
        return GlobalConstants.lao_service_list_url;
    }

    @Override
    public HashMap<String, String> getHashMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.KEY_PAGENUM, pageNum + "");
        hashMap.put(GlobalConstants.KEY_PAGESIZE, GlobalConstants
                .value_page_size);
        return hashMap;
    }


    @Override
    public void parserData(String paramString)

    {

        LogUtils.e("内容是",paramString);
        if (this.type.equals("0"))
        {
            parserData(paramString, "0");
        }else{
            parserData(paramString, "1");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
            if (this.num == 0)
        {
            super.initListViewListener();
            super.initTopListener();
            refreshData();
        }
    }

    public void parserData(String result, String paramString2) {

        LogUtils.e("查询到的数据是1",result);
        LogUtils.e("查询到的数据是2",paramString2);

        LaborServiceInfo info = GSonUtil.parseJson(result,
                LaborServiceInfo.class);


        infoList.clear();

        if(null != info.objList){
            for(LaborServiceDetailInfo laborServiceDetailInfo:info.objList){
                if(laborServiceDetailInfo.type.equals(paramString2)){
                    infoList.add(laborServiceDetailInfo);

                }
            }

        }

        LogUtils.e("查询到的数据是3",infoList.size()+"");
        if (infoList != null && infoList.size() > 0) {


            if (isRefresh) {
                mAllList.clear();
                isRefresh = false;
            }

            if (isSelect_address) {
                //来自地址选择
                mAllList.clear();
                //将标记还原
                isSelect_address = false;
            }

            mAllList.clear();
            for(int i = 0;i<infoList.size();i++){
                if(infoList.get(i).type.equals(type)){
                    mAllList.add(infoList.get(i));
                }
            }
            num =  mAllList.size();
            if (mAdapter == null) {
                mAdapter = new LaborServiceAdapter
                        (LaborServiceCooperationActivity.this, mAllList);
                crpLv.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }

            isLoading = false;


            //写入换存
            mACache.put(cacheKey,result);

            LogUtils.e(TAG, "获得了数据");
        } else {
            ToastUtils.makeTextShowNoData(LaborServiceCooperationActivity.this);
        }

        LogUtils.e(TAG, "此方法走了");
    }

    @Override
    public void refreshData() {
        if (!isLoading) {
            //请集合
            isRefresh = true;
            pageNum = 1;
            initData();
        }
    }

    @Override
    public void lvScrollAction() {
        if (lastVisiblePosition == mAllList.size() - 1+2+1 &&
                !isLoading && mAllList.size() < num) {
            //判断是否到达最后一条数据
            //开始加载下一页数据
            crpLv.onRefreshOpen();
            initData();
        }

    }

    @Override
    public void lvOnItemClick(int position) {
        if (position >= 2) {
            int infoPositon = position - 2;
            //跳转详情页
            Intent intent = new Intent(LaborServiceCooperationActivity
                    .this, LaborServiceCooperationDetailsActivity
                    .class);
            intent.putExtra(GlobalConstants.key_uuid, mAllList.get
                    (infoPositon).uuid);
            startActivity(intent);
        }
    }


    @Override
    public void initListener() {

        mapModeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动地图
                startActivity(new Intent(LaborServiceCooperationActivity
                        .this, LaborServiceCooperationMapActivity.class));
            }
        });

        conformIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                conformIv.setEnabled(false);
                isSelect_address = true;
                setData2Netword();
            }
        });

        //劳务时间选择
        ivWorkTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpWindow(tvWorkTime);
                mPopListView.setAdapter(mPopAdapter);
            }
        });

        //时间选择
        mPopListView.setOnItemClickListener(new AdapterView
                .OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {
                MarkerConstants.value_service_time_type =
                        serviceTimesType[position];
                tvWorkTime.setText(serviceTimesName[position]);

                tvWorkTime2.setText(serviceTimesName[position]);
                pop.dismiss();
            }
        });


        mapModeIv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动地图
                startActivity(new Intent(LaborServiceCooperationActivity
                        .this, LaborServiceCooperationMapActivity.class));
            }
        });

        conformIv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conformIv2.setEnabled(false);
                isSelect_address = true;
                setData2Netword();
            }
        });


        //劳务时间选择
        ivWorkTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpWindow(tvWorkTime2);
                mPopListView.setAdapter(mPopAdapter);
            }
        });

    }

    private void setData2Netword() {

        FromNetInfo2AddsSelectBiz.setData2Netword(GlobalConstants
                .lao_service_address_select_url,pageNum, new
                FromNetInfo2AddsSelectBiz.OnAddressDataListener() {


                    @Override
                    public void onSuccess(String result) {
                        if (result != null) {
                            parserData(result);
                        }
                        conformIv.setEnabled(true);
                        conformIv2.setEnabled(true);
                    }

                    @Override
                    public void onfail() {
                        ToastUtils.makeTextShowNoNet
                                (LaborServiceCooperationActivity.this);
                        conformIv2.setEnabled(true);
                        conformIv.setEnabled(true);
                    }
                });
    }


    //下拉框
    private void showPopUpWindow(TextView view) {
        pop = new PopupWindow(mPopListView, view.getWidth(), WindowManager
                .LayoutParams.WRAP_CONTENT,
                true);

        pop.setBackgroundDrawable(new ColorDrawable());

        pop.showAsDropDown(view);// 将popupwindown显示在某一个View的正下方

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
           refreshData();
        }
    }
}
