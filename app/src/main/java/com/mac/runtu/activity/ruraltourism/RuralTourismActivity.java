package com.mac.runtu.activity.ruraltourism;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.BaseActivity;
import com.mac.runtu.adapter.RuralTourismAdapter;
import com.mac.runtu.business.AddressBiz4FromPro3;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.RuralTourismInfo;
import com.mac.runtu.javabean.RuralTourismListInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * 乡村旅游
 */
public class RuralTourismActivity extends BaseActivity {

    private static final String TAG = "RuralTourismActivity";




    private ImageView confirmIv;

    private TextView            tvCity;
    private TextView            tvArea;

    private ImageView ivCity;
    private ImageView ivArea;
    private TextView  tvTown;
    private TextView  tvVillage;
    private ImageView ivTown;
    private ImageView ivVillage;
    private LinearLayout        llSerachMenu;


    private ImageView confirmIv2;
    private TextView            tvCity2;
    private TextView            tvArea2;
    private ImageView ivCity2;
    private ImageView ivArea2;
    private TextView  tvTown2;
    private TextView  tvVillage2;
    private ImageView ivTown2;
    private ImageView ivVillage2;
    private LinearLayout        llSerachMenu2;

    private ArrayList<RuralTourismListInfo> mAllList = new
            ArrayList<>();

    private RuralTourismAdapter mRuralAdapter;

    private int                             pageNum  = 1;
    private int                 num;
    private boolean             isLoading;
    private boolean             isRefresh;

    @Override
    protected int getTopTitleName() {
        return R.string.rural_tourism;
    }

    @Override
    protected View getFloatView() {
        return getLayoutInflater().inflate(R.layout
                .rural_tourism_float_menu_layout, null);
    }

    @Override
    public void initView() {
       releaseIv.setVisibility(View.GONE);

        confirmIv = ButterKnife.findById(floatView, R.id.confirm_Iv);
        tvCity = ButterKnife.findById(floatView, R.id.tv_city);
        tvArea = ButterKnife.findById(floatView, R.id.tv_area);
        tvTown = ButterKnife.findById(floatView, R.id.tv_town);
        tvVillage = ButterKnife.findById(floatView, R.id.tv_village);
        ivCity = ButterKnife.findById(floatView, R.id.iv_city);
        ivArea = ButterKnife.findById(floatView, R.id.iv_area);
        ivTown = ButterKnife.findById(floatView, R.id.iv_town);
        ivVillage = ButterKnife.findById(floatView, R.id.iv_village);


        llSerachMenu = ButterKnife.findById(floatView, R.id
                .ll_serach);
        TextView tvSearch = ButterKnife.findById(floatView, R.id.search_Tv);
        tvSearch.setText(UiUtils.getString(R.string
                .rural_tourism_search_hint));


        confirmIv2 = ButterKnife.findById(menuLL, R.id.confirm_Iv);
        tvCity2 = ButterKnife.findById(menuLL, R.id.tv_city);
        tvArea2 = ButterKnife.findById(menuLL, R.id.tv_area);
        tvTown2 = ButterKnife.findById(menuLL, R.id.tv_town);
        tvVillage2 = ButterKnife.findById(menuLL, R.id.tv_village);
        ivCity2 = ButterKnife.findById(menuLL, R.id.iv_city);
        ivArea2 = ButterKnife.findById(menuLL, R.id.iv_area);
        ivTown2 = ButterKnife.findById(menuLL, R.id.iv_town);
        ivVillage2 = ButterKnife.findById(menuLL, R.id.iv_village);


        llSerachMenu2 = ButterKnife.findById(menuLL, R.id
                .ll_serach);
        TextView tvSearch2 = ButterKnife.findById(menuLL, R.id.search_Tv);
        tvSearch2.setText(UiUtils.getString(R.string
                .rural_tourism_search_hint));
    }



    @Override
    public String getAdType() {
        return GlobalConstants.AD_TYPE_TRAVEL;
    }

    @Override
    protected void initDataOther() {
        new AddressBiz4FromPro3(this,null,null,  tvCity, ivCity, tvArea,
                ivArea, tvTown, ivTown, tvVillage, ivVillage);

        new AddressBiz4FromPro3(this,null,null, tvCity2, ivCity2, tvArea2,
                ivArea2, tvTown2, ivTown2, tvVillage2, ivVillage2);
    }

    @Override
    protected Class<?> getReleaseActivity() {
        return null;
    }

    @Override
    protected String getUrl() {
        return GlobalConstants.tour_list_url;
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

        RuralTourismInfo ruralTourismInfo = GSonUtil.parseJson(result,
                RuralTourismInfo.class);
        num = ruralTourismInfo.num;

        LogUtils.e(TAG,"乡村旅游主页num="+num);

        ArrayList<RuralTourismListInfo> objList =
                ruralTourismInfo.objList;

        if (objList != null && objList.size() > 0) {

            if (isRefresh) {
                mAllList.clear();
                isRefresh = false;
            }


            //数据添加到总集合
            mAllList.addAll(objList);

            LogUtils.e(TAG, "tour_集合数据" + mAllList.toString());
            if (mRuralAdapter == null) {
                mRuralAdapter = new
                        RuralTourismAdapter(this, mAllList);
                crpLv.setAdapter(mRuralAdapter);
            } else {
                mRuralAdapter.notifyDataSetChanged();

            }

            pageNum++;
            isLoading = false;

            //写入换存
            mACache.put(cacheKey,result);
            //llLoading.setVisibility(View.GONE);
        } else {
            ToastUtils.makeTextShowNoData
                    (RuralTourismActivity.this);
        }


    }

    @Override
    public void refreshData() {
        if (!isLoading) {
            isRefresh = true;
            pageNum = 1;
            initData();
        }
    }

    @Override
    public void lvScrollAction() {
        if (lastVisiblePosition == mAllList.size() + 1 +1 &&
                !isLoading && mAllList.size() < num) {
            //加载数据
            crpLv.onRefreshOpen();

            initData();
        }

    }

    @Override
    public void lvOnItemClick(int position) {
        int listPosition = position - 2;
        if (position > 1) {
            Intent intent = new Intent(RuralTourismActivity.this,
                    AttractionDetailsActivity.class);
            intent.putExtra(GlobalConstants.key_uuid, mAllList.get
                    (listPosition).uuid);
            startActivity(intent);
        }
    }
    @Override
    public void initListener() {


        confirmIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面搜索页面
                Intent intent = new Intent(RuralTourismActivity.this,
                        RuralTourismListActivity.class);
                intent.putExtra(GlobalConstants.key_content_ui, GlobalConstants
                        .value_from_select);
                startActivity(intent);
            }
        });

        llSerachMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RuralTourismActivity.this,
                        RuralTourismListActivity.class));
            }
        });

        confirmIv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面搜索页面
                Intent intent = new Intent(RuralTourismActivity.this,
                        RuralTourismListActivity.class);
                intent.putExtra(GlobalConstants.key_content_ui, GlobalConstants
                        .value_from_select);
                startActivity(intent);
            }
        });

        llSerachMenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RuralTourismActivity.this,
                        RuralTourismListActivity.class));
            }
        });
    }


}
