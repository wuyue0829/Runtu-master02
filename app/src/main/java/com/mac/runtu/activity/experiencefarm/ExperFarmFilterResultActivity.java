package com.mac.runtu.activity.experiencefarm;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.mac.runtu.R;
import com.mac.runtu.adapter.ExperFarmHomeAdapter;
import com.mac.runtu.business.SearchAndFilterGetData4Net;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnGetData4NetListener;
import com.mac.runtu.ipcamer.AddCameraActivity;
import com.mac.runtu.javabean.SpecialtyGoodDetailInfo;
import com.mac.runtu.javabean.SpecialtyGoodInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.view.PullToRefreshListView;

import java.util.ArrayList;

/**
 * 体验农场筛选结果列表页
 */
public class ExperFarmFilterResultActivity extends AppCompatActivity {

    private static final String TAG = "ExperFarmFilterResultActivity";

    private PullToRefreshListView lv;

    private ImageView backIv;
    private boolean   isLoading;
    private int pageNum = 1;

    private int       num;
    private ImageView ivBack;
    private ArrayList<SpecialtyGoodDetailInfo> mAllList = new ArrayList<>();
    private ExperFarmHomeAdapter mAdapter;
    private String               mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exper_farm_filter_result);


        mModel = getIntent().getStringExtra(GlobalConstants
                .key_model);

        LogUtils.e(TAG, "来自筛选的model=" + mModel);

        //还原标记
        //MarkerConstants.isFromFilter = false;

        initView();
        initData();
        initListener();
    }


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

    private void initView() {
        lv = (PullToRefreshListView) findViewById(R.id.lv);
        ivBack = (ImageView) findViewById(R.id.back_Iv);

    }

    private void initData() {
        //房屋网路
        //网络加载数据
        isLoading = true;

        SearchAndFilterGetData4Net.getData4Net(pageNum, "", mModel, new
                OnGetData4NetListener() {


                    @Override
                    public void onSuccess(String result) {
                        parserData(result);

                        lv.onRefreshComplete();
                    }

                    @Override
                    public void onFail() {
                        ToastUtils.makeTextShowNoNet
                                (ExperFarmFilterResultActivity.this);

                        lv.onRefreshComplete();
                    }
                });


    }

    //解析数据
    private void parserData(String result) {
        SpecialtyGoodInfo info = GSonUtil.parseJson(result,
                SpecialtyGoodInfo.class);

        num = info.num;

        LogUtils.e(TAG, "体验农场筛选num==" + num);

        ArrayList<SpecialtyGoodDetailInfo> infoList = info.objList;

        if (infoList != null && infoList.size() > 0) {

            mAllList.addAll(infoList);

            //LogUtils.e(TAG, "property" + mAllList.toString());
            if (mAdapter == null) {
                mAdapter = new ExperFarmHomeAdapter(this, mAllList);
                lv.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }

            pageNum++;
            //pageSize += 10;
            isLoading = false;
        } else {
            ToastUtils.makeTextShowNoData(this);
        }
    }


    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int
                    scrollState) {

                //滑动改变状态
                if (scrollState == SCROLL_STATE_IDLE) {//ListView空闲
                    //获取当前可见的最后一个item的位置
                    int lastVisiblePosition = lv
                            .getLastVisiblePosition();

                    //如果isLoading=true表示正在加载,还没结束,此时不允许加载下一页
                    if (lastVisiblePosition == mAllList.size() &&
                            !isLoading && mAllList.size() < num) {

                        lv.onRefreshOpen();
                        initData();
                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int
                    visibleItemCount, int totalItemCount) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener
                () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {
                //跳转详情页
                //跳转详情页
                Intent intent = new Intent(ExperFarmFilterResultActivity.this,
                        ExperienceFarmAdoptDetails
                                .class);
                intent.putExtra(GlobalConstants.key_uuid, mAllList.get
                        (position)
                        .uuid);
                startActivity(intent);
            }
        });
    }
}
