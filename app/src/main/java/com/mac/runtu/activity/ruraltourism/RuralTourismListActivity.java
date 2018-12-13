package com.mac.runtu.activity.ruraltourism;

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
import com.mac.runtu.adapter.RuralTourismAdapter;
import com.mac.runtu.business.FromNetInfo2AddsSelectBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.RuralTourismInfo;
import com.mac.runtu.javabean.RuralTourismListInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.view.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 旅游信息结果列表
 */
public class RuralTourismListActivity extends AppCompatActivity {
    private static final String TAG = "RuralTourismListActivity";

    private ArrayList<RuralTourismListInfo> mAllList = new
            ArrayList<>();


    private int pageNum = 0;
    private boolean isLoading;
    private String  content;

    private PullToRefreshListView lv;
    private ImageView             ivBack;
    private RuralTourismAdapter   mRuralTourAdapter;
    private TextView              tvTopName;
    private int                   num;
    private LinearLayout          llSerachEt;
    private EditText              etSearch;
    private TextView              tvSearchBtn;
    private int                   search_select;
    private boolean isSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rural_tourism_list);
        initView();

        initListener();

        Intent intent = getIntent();
        String value = intent.getStringExtra(GlobalConstants.key_content_ui);

        if (GlobalConstants.value_from_select.equals(value)) {
            search_select = 0;
            //来自搜索条目
            initData();

        } else {
            search_select = 1;
            llSerachEt.setVisibility(View.VISIBLE);
            etSearch.setHint(R.string.rural_tourism_search_hint);

        }

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

        llSerachEt = (LinearLayout) findViewById(R.id.ll_serach_et);
        llSerachEt.setVisibility(View.GONE);

        etSearch = (EditText) findViewById(R.id.et_serach);
        tvSearchBtn = (TextView) findViewById(R.id.tv_search_btn);

        lv = (PullToRefreshListView) findViewById(R.id.lv);

        //添加进入的不局顶部名称
        tvTopName = (TextView) findViewById(R.id.tv_top_name);
        tvTopName.setText("旅游景点");

        ivBack = (ImageView) findViewById(R.id.back_Iv);

    }

    private void initData() {
        switch (search_select) {
            case 0:
                setData2Netword();
                break;
            case 1:
                setSearchDataFromNetWork();
                break;
        }
    }


    //地区选择提交数据到网络
    private void setData2Netword() {
        isLoading = true;
        //llLoading.setVisibility(View.VISIBLE);

        FromNetInfo2AddsSelectBiz.setData2Netword(GlobalConstants
                .tour_select_address_url,pageNum, new FromNetInfo2AddsSelectBiz
                .OnAddressDataListener() {


            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    //解析数据
                    LogUtils.e(TAG, "旅游信息列表" + result);
                    //设置给listview
                    parserData(result);

                }

                lv.onRefreshComplete();
            }

            @Override
            public void onfail() {
                lv.onRefreshComplete();
            }
        });
    }

    //条目搜索
    private void setSearchDataFromNetWork() {

        //网络加载数据
        isLoading = true;
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.key_tilte, content);
        hashMap.put(GlobalConstants.KEY_PAGENUM, pageNum + "");
        hashMap.put(GlobalConstants.KEY_PAGESIZE, GlobalConstants
                .value_page_size);
        MyHttpUtils.getStringDataFromNet(GlobalConstants
                        .tour_search_tilte_url,
                null, hashMap, new MyHttpUtils.OnNewWorkRequestListener() {

                    @Override
                    public void onNewWorkSuccess(String result) {

                        if (result != null) {
                            LogUtils.e(TAG, "乡村旅游" + result);
                            parserData(result);
                        }
                        if (tvSearchBtn!=null) {
                            tvSearchBtn.setEnabled(true);
                        }
                        lv.onRefreshComplete();
                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        ToastUtils.makeTextShowNoNet
                                (RuralTourismListActivity.this);
                        if (tvSearchBtn!=null) {
                            tvSearchBtn.setEnabled(true);
                        }
                        lv.onRefreshComplete();
                    }
                });
    }

    //解析数据
    private void parserData(String result) {

        RuralTourismInfo info = GSonUtil.parseJson
                (result,
                        RuralTourismInfo.class);
        num = info.num;

        LogUtils.e(TAG,"乡村旅游搜索页num="+num);
        ArrayList<RuralTourismListInfo> infoList = info.objList;

        if (infoList != null &&
                infoList.size() > 0) {
            //数据添加到总集合
            if (isSearch) {
                mAllList.clear();
                isSearch = false;
            }


            mAllList.addAll(infoList);

            LogUtils.e(TAG, "乡村旅游infoList" + mAllList.toString());
            if (mRuralTourAdapter == null) {
                mRuralTourAdapter = new
                        RuralTourismAdapter(RuralTourismListActivity.this,
                        mAllList);
                lv.setAdapter(mRuralTourAdapter);
            } else {
                mRuralTourAdapter.notifyDataSetChanged();

            }

            pageNum++;
            //pageSize += 10;
            isLoading = false;
            //llLoading.setVisibility(View.GONE);
        } else {
            // llLoading.setVisibility(View.GONE);
            ToastUtils.makeTextShowNoData(RuralTourismListActivity.this);
        }


    }

    private void initListener() {
        tvSearchBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                content = etSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    //搜索方法
                    //getDataFromNetByContent();
                   isSearch = true;
                    tvSearchBtn.setEnabled(false);
                    setSearchDataFromNetWork();
                }
            }
        });

        //返回按钮
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
                    if (lastVisiblePosition == mAllList.size()  &&
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
                Intent intent = new Intent(RuralTourismListActivity.this,
                        AttractionDetailsActivity.class);

                intent.putExtra(GlobalConstants.key_uuid, mAllList.get
                        (position).uuid);

                LogUtils.e(TAG, "选中的数据" + mAllList.get
                        (position).title);
                startActivity(intent);
            }
        });
    }
}
