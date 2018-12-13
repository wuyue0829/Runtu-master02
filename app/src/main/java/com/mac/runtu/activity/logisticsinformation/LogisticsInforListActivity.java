package com.mac.runtu.activity.logisticsinformation;

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
import com.mac.runtu.adapter.LogisticsAdapter;
import com.mac.runtu.business.FromNetInfo2AddsSelectStartFainlBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.LogisticsDetailInfo;
import com.mac.runtu.javabean.LogisticsInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.view.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 物流信息搜索结果列表页
 */
public class LogisticsInforListActivity extends AppCompatActivity {

    private static final String TAG = "LogisticsInforListActivity";

    private LinearLayout          llSerachEt;
    private PullToRefreshListView lv;
    private EditText              etSearch;
    private TextView              tvSearchBtn;


    private boolean isLoading = false;
    private int     pageNum   = 1;
    private int num;
    private ArrayList<LogisticsDetailInfo> mAllList = new ArrayList<>();
    private LogisticsAdapter mAdapter;
    private String           content;
    private int              search_select;
    private boolean          isSearch;
    private ImageView ivBack;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_infor_list);

        initView();

        Intent intent = getIntent();
        String value = intent.getStringExtra(GlobalConstants.key_content_ui);
        String result = intent.getStringExtra(GlobalConstants.key_user);

        if (GlobalConstants.value_from_select.equals(value)) {
            search_select = 1;
            //来自搜索条目
            initData();

        } else if (result != null) {

            parserData(result);

        } else {
            search_select = 0;
            llSerachEt.setVisibility(View.VISIBLE);
            etSearch.setHint(R.string.logistics_information_search_hint);

        }

        initListener();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.back_Iv);

        llSerachEt = (LinearLayout) findViewById(R.id.ll_serach_et);
        llSerachEt.setVisibility(View.GONE);

        TextView tvTopName = (TextView) findViewById(R.id.tv_top_name);
        tvTopName.setText("信息列表");
        lv = (PullToRefreshListView) findViewById(R.id.lv);
        etSearch = (EditText) findViewById(R.id.et_serach);
        tvSearchBtn = (TextView) findViewById(R.id.tv_search_btn);
    }

    private void initData() {

        switch (search_select) {
            case 0:
                setSearchDataFromNetWork();
                break;
            case 1:
                setData2Netword();
                break;
        }
    }

    private void setData2Netword() {
        isLoading = true;

        //搜索条地址查询
        FromNetInfo2AddsSelectStartFainlBiz.setData2Netword(pageNum, new
                FromNetInfo2AddsSelectStartFainlBiz.OnDataListener() {
                    @Override
                    public void onSuccess(String result) {
                        if (result != null) {
                            parserData(result);
                        }

                        lv.onRefreshComplete();
                    }

                    @Override
                    public void onfail() {
                        ToastUtils.makeTextShowNoNet
                                (LogisticsInforListActivity.this);


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
                        .Logistics_search_title_url,
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
                                (LogisticsInforListActivity.this);

                        if (tvSearchBtn!=null) {
                            tvSearchBtn.setEnabled(true);
                        }
                        lv.onRefreshComplete();
                    }
                });
    }

    //解析数据
    private void parserData(String result) {
        LogisticsInfo info = GSonUtil.parseJson(result,
                LogisticsInfo.class);
        num = info.num;

        LogUtils.e(TAG, "物流信息搜索num=" + num);


        ArrayList<LogisticsDetailInfo> infoList = info.objList;

        if (infoList != null && infoList.size() > 0) {

            if (isSearch) {
                mAllList.clear();
                isSearch = false;
            }

            mAllList.addAll(infoList);

            if (mAdapter == null) {
                mAdapter = new LogisticsAdapter
                        (LogisticsInforListActivity.this, mAllList);
                lv.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }

            isLoading = false;

            pageNum++;
        } else {
            ToastUtils.makeTextShowNoData(LogisticsInforListActivity.this);
        }
    }

    private void initListener() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = etSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    //搜索方法
                    //getDataFromNetByContent();
                    isSearch = true;
                    tvSearchBtn.setEnabled(false);
                    initData();
                }
            }
        });

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int
                    scrollState) {
                int lastVisiblePosition = lv
                        .getLastVisiblePosition();
                //如果isLoading=true表示正在加载,还没结束,此时不允许加载下一页
                if (lastVisiblePosition == mAllList.size() - 1 +
                        2 + 1 &&
                        !isLoading && mAllList.size() < num) {

                    lv.onRefreshOpen();
                    initData();
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int
                    visibleItemCount, int totalItemCount) {

            }

        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {


                Intent intent = new Intent(LogisticsInforListActivity
                        .this, LogisticsInformationDetailsActivity.class);
                intent.putExtra(GlobalConstants.key_uuid, mAllList.get
                        (position).uuid);
                startActivity(intent);

            }
        });
    }


}
