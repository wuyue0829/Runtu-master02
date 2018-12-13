package com.mac.runtu.activity.entrepreneurshiptraining;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.BaseActivity;
import com.mac.runtu.adapter.EnterTrainAdapter;
import com.mac.runtu.business.AddressBiz4FromPro3;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.EntreTrainDetailInfo;
import com.mac.runtu.javabean.EntreTrainInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * 创业培训
 */
public class EntrepreneurshipTrainingActivity extends BaseActivity {

    private static final String TAG = "EntrepreneurshipTrainingActivity";


    private ArrayList<EntreTrainDetailInfo> mAllList = new ArrayList<>();
    private boolean isLoading;
    private boolean isRefresh = false;
    private int     pageNum   = 1;
    private EnterTrainAdapter mEnterAdapter;

    private int num;

    private ImageView confirmIv;
    private TextView tvCity;
    private TextView tvArea;

    private ImageView           ivCity;
    private ImageView           ivArea;
    private TextView            tvTown;
    private TextView            tvVillage;
    private ImageView           ivTown;
    private ImageView           ivVillage;

    private LinearLayout        llSerachMenu;

    private ImageView confirmIv2;
    private TextView tvCity2;
    private TextView tvArea2;

    private ImageView           ivCity2;
    private ImageView           ivArea2;
    private TextView            tvTown2;
    private TextView            tvVillage2;
    private ImageView           ivTown2;
    private ImageView           ivVillage2;

    private LinearLayout        llSerachMenu2;


    @Override
    protected int getTopTitleName() {
        return R.string.entrepreneurship_training;
    }

    @Override
    protected View getFloatView() {
        return getLayoutInflater().inflate(R.layout
                .entrepreneurship_training_float_menu_layout, null);
    }

    @Override
    public void initView() {

        //没有发布
        releaseIv.setVisibility(View.GONE);

        tvCity = ButterKnife.findById(floatView, R.id.tv_city);
        tvArea = ButterKnife.findById(floatView, R.id.tv_area);
        tvTown = ButterKnife.findById(floatView, R.id.tv_town);
        tvVillage = ButterKnife.findById(floatView, R.id.tv_village);
        ivCity = ButterKnife.findById(floatView, R.id.iv_city);
        ivArea = ButterKnife.findById(floatView, R.id.iv_area);
        ivTown = ButterKnife.findById(floatView, R.id.iv_town);
        ivVillage = ButterKnife.findById(floatView, R.id.iv_village);
        confirmIv = ButterKnife.findById(floatView, R.id.confirm_Iv);

        llSerachMenu = ButterKnife.findById(floatView, R.id.ll_serach);
        TextView tvSearch = ButterKnife.findById(floatView, R.id.search_Tv);
        tvSearch.setText(UiUtils.getString(R.string
                .entrepreneurship_training_search_hint));


        TextView tvTopName = ButterKnife.findById(floatView, R.id.tv_top_name);
        tvTopName.setText("培训信息");

        tvCity2 = ButterKnife.findById(menuLL, R.id.tv_city);
        tvArea2 = ButterKnife.findById(menuLL, R.id.tv_area);
        tvTown2 = ButterKnife.findById(menuLL, R.id.tv_town);
        tvVillage2 = ButterKnife.findById(menuLL, R.id.tv_village);
        ivCity2 = ButterKnife.findById(menuLL, R.id.iv_city);
        ivArea2 = ButterKnife.findById(menuLL, R.id.iv_area);
        ivTown2 = ButterKnife.findById(menuLL, R.id.iv_town);
        ivVillage2 = ButterKnife.findById(menuLL, R.id.iv_village);
        confirmIv2 = ButterKnife.findById(menuLL, R.id.confirm_Iv);

        llSerachMenu2 = ButterKnife.findById(menuLL, R.id.ll_serach);
        TextView tvSearch2 = ButterKnife.findById(menuLL, R.id.search_Tv);
        tvSearch2.setText(UiUtils.getString(R.string
                .entrepreneurship_training_search_hint));


        TextView tvTopName2 = ButterKnife.findById(floatView, R.id.tv_top_name);
        tvTopName2.setText("培训信息");
    }


    @Override
    public String getAdType() {
        return GlobalConstants.AD_TYPE_EDUCATION;
    }

    @Override
    protected void initDataOther() {
        new AddressBiz4FromPro3(this,null,null, tvCity, ivCity, tvArea,
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
        return GlobalConstants
                .entre_train_list_url;
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
    public void parserData(String result) {

        LogUtils.e(TAG,"网路获得result="+result);

        EntreTrainInfo info = GSonUtil.parseJson(result,
                EntreTrainInfo.class);
        ArrayList<EntreTrainDetailInfo> infoList = info.objList;
        num = info.num;

        LogUtils.e(TAG,"创业培训num="+num);
        if (infoList != null && infoList.size() > 0) {


            if (isRefresh) {
                mAllList.clear();
                isRefresh = false;
            }
            mAllList.addAll(infoList);

            if (mEnterAdapter == null) {
                mEnterAdapter = new EnterTrainAdapter(this,
                        mAllList);

                crpLv.setAdapter(mEnterAdapter);

            } else {
                mEnterAdapter.notifyDataSetChanged();
            }

            pageNum++;
            //pageSize+=10;
            isLoading = false;

            //写入换存
            LogUtils.e(TAG,"result="+result);

            mACache.put(cacheKey,result);

        } else {
            ToastUtils.makeTextShowNoData(this);
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
        //如果isLoading=true表示正在加载,还没结束,此时不允许加载下一页
        if (lastVisiblePosition == mAllList.size() + 1+1 &&
                !isLoading && mAllList.size() < num) {
            //加载数据
            crpLv.onRefreshOpen();
            initData();
        }

    }

    @Override
    public void lvOnItemClick(int position) {
        if (position > 1) {
            int listPosition = position - 2;
            //跳转详情页
            Intent intent = new Intent(EntrepreneurshipTrainingActivity
                    .this, EpreneurshipTrainingActivity.class);
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
                Intent intent = new Intent(EntrepreneurshipTrainingActivity
                        .this, EpreneurshipTrainingListActivity.class);
                intent.putExtra(GlobalConstants.key_type,
                        GlobalConstants.value_from_select);
                startActivity(intent);
            }
        });


        llSerachMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EntrepreneurshipTrainingActivity
                        .this, EpreneurshipTrainingListActivity.class));
            }
        });

        confirmIv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EntrepreneurshipTrainingActivity
                        .this, EpreneurshipTrainingListActivity.class);
                intent.putExtra(GlobalConstants.key_type,
                        GlobalConstants.value_from_select);
                startActivity(intent);
            }
        });


        llSerachMenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EntrepreneurshipTrainingActivity
                        .this, EpreneurshipTrainingListActivity.class));
            }
        });

    }

}
