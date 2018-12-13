package com.mac.runtu.activity.specialtyshop;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.adapter.AgricMachineryAdapter;
import com.mac.runtu.adapter.SpecialtyGoodAllAdapter;
import com.mac.runtu.business.SearchAndFilterGetData4Net;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.interfaceif.OnGetData4NetListener;
import com.mac.runtu.javabean.SpecialtyGoodDetailInfo;
import com.mac.runtu.javabean.SpecialtyGoodInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.view.PullToRefreshListView;

import java.util.ArrayList;

/**
 * 搜索结果列表页
 */
public class SpecizltySearchListActivity extends AppCompatActivity {

    private static final String TAG = "SpecizltySearchListActivity";

    private ArrayList<SpecialtyGoodDetailInfo> mAllList = new
            ArrayList<>();

    private PullToRefreshListView lv;

    private ImageView backIv;
    private boolean   isLoading;
    private int pageNum = 1;

    private int                   num;
    private AgricMachineryAdapter mAgricMachAdapter;

    private LinearLayout          llSerachEt;
    private EditText              etSearch;
    private TextView              tvSearchBtn;
    private String content = "";
    private int                     search_select;
    private SpecialtyGoodAllAdapter mAdapter;
    private String                  mModel;
    private boolean isSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specizlty_search_list);

        Intent intent = getIntent();

        mModel = intent.getStringExtra(GlobalConstants.key_model);

        boolean isFromFilter = intent.getBooleanExtra(GlobalConstants
                .key_from_fitler, false);

        initView();

        LogUtils.e(TAG, "来自筛选的model=" + mModel);

        if (isFromFilter) {
            //来自筛选页
            llSerachEt.setVisibility(View.GONE);
            //标记还原
            initData();
        } else {
            //来自选择按钮
            llSerachEt.setVisibility(View.VISIBLE);
            etSearch.setHint(R.string.specialty_search_hint);

            //fargment同时加载  把筛选页面的赋值 还原
            MarkerConstants.value_kind = "";
            MarkerConstants.value_county = "";

        }


        initListener();
    }

    private void initView() {

        llSerachEt = (LinearLayout) findViewById(R.id.ll_serach_et);

        etSearch = (EditText) findViewById(R.id.et_serach);
        tvSearchBtn = (TextView) findViewById(R.id.tv_search_btn);
        lv = (PullToRefreshListView) findViewById(R.id.lv);
        backIv = (ImageView) findViewById(R.id.back_Iv);
        //添加进入的不局顶部名称

        //llLoading = (LinearLayout) findViewById(R.id.ll_loading);

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

    private void initData() {
        //房屋网路
        //网络加载数据
        isLoading = true;

        SearchAndFilterGetData4Net.getData4Net(pageNum, "" + content, mModel,
                new OnGetData4NetListener() {


                            @Override
                            public void onSuccess(String result) {
                                parserData(result);

                                if (tvSearchBtn!=null) {
                                    tvSearchBtn.setEnabled(true);
                                }
                                lv.onRefreshComplete();
                            }

                            @Override
                            public void onFail() {
                                ToastUtils.makeTextShowNoNet
                                        (SpecizltySearchListActivity.this);
                                lv.onRefreshComplete();

                                if (tvSearchBtn!=null) {
                                    tvSearchBtn.setEnabled(true);
                                }
                            }
                        });


    }


    //解析数据
    private void parserData(String result) {
        SpecialtyGoodInfo info = GSonUtil.parseJson(result,
                SpecialtyGoodInfo.class);

        num = info.num;

        LogUtils.e(TAG, "特产电商搜索num==" + num);

        ArrayList<SpecialtyGoodDetailInfo> infoList = info.objList;

        if (infoList != null && infoList.size() > 0) {

            if (isSearch) {
               mAllList.clear();
                isSearch = false;
            }
            mAllList.addAll(infoList);

            //LogUtils.e(TAG, "property" + mAllList.toString());
            if (mAdapter == null) {
                mAdapter = new
                        SpecialtyGoodAllAdapter(SpecizltySearchListActivity
                        .this,
                        mAllList);
                lv.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }

            pageNum++;
            //pageSize += 10;
            isLoading = false;
        } else {
            ToastUtils.makeTextShowNoData(SpecizltySearchListActivity.this);
        }
    }

    private void initListener() {

        tvSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                content = etSearch.getText().toString().trim();

                LogUtils.e(TAG,"content="+content);

                if (!TextUtils.isEmpty(content)) {
                    //搜索方法
                    isSearch = true;
                    tvSearchBtn.setEnabled(false);
                    initData();
                }
            }
        });

        //返回按钮
        backIv.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(SpecizltySearchListActivity.this,
                        SpecialtyDetailsActivity
                                .class);
                intent.putExtra(GlobalConstants.key_uuid, mAllList.get
                        (position)
                        .uuid);
                startActivity(intent);

            }
        });
    }
}
