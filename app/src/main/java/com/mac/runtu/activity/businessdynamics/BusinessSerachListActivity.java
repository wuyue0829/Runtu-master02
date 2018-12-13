package com.mac.runtu.activity.businessdynamics;

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
import com.mac.runtu.adapter.BusinessDatalistAdapter;
import com.mac.runtu.adapter.FramDatalistAdapter;
import com.mac.runtu.business.LoginBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.BusinessDataDetailInfo;
import com.mac.runtu.javabean.BusinessDataListInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.view.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

public class BusinessSerachListActivity extends AppCompatActivity {
    private static final String TAG = "BusinessSerachListActivity";


    private PullToRefreshListView lv;
    private int pageNum = 1;

    private String type;

    private BusinessDatalistAdapter mBusinessAdapter;
    private FramDatalistAdapter     mFramAdapter;
    private boolean                 isLoading;

    private ArrayList<BusinessDataDetailInfo> mAllList =
            new ArrayList<>();
    private String       value;
    private int          num;
    private EditText     etSearch;
    private TextView     tvSearchBtn;
    private LinearLayout llSerachEt;
    private String       content;
    private int          search_select;
    private ImageView    ivBack;
    private boolean      isSearch;

    //private boolean isFromUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_serach_list);
        ButterKnife.bind(this);

        initView();
        Intent intent = getIntent();
        type = intent.getStringExtra(GlobalConstants.key_type);
        String value = intent.getStringExtra(GlobalConstants
                .key_content_ui);

        String result = intent.getStringExtra(GlobalConstants.key_user);

        //地址搜索
        if (GlobalConstants.value_from_select.equals(value)) {
            //选择
            initData();
            search_select = 0;
        } else if (result != null) {
            // isFromUser =true;
            type = LoginBiz.getInstance().userDesInfo.usertype;

            parserData(result);
        } else {
            //市内容搜索
            llSerachEt.setVisibility(View.VISIBLE);
            etSearch.setHint(R.string.business_dynamics_search_hint);
            search_select = 1;
        }

        intentListener();

        /* String searchContent = etSearch.getText().toString().trim();
        MarkerConstants.value_serach_conent = searchContent;*/
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


    private void initView() {
        ivBack = (ImageView) findViewById(R.id.back_Iv);
        llSerachEt = (LinearLayout) findViewById(R.id.ll_serach_et);
        llSerachEt.setVisibility(View.GONE);
        lv = (PullToRefreshListView) findViewById(R.id.lv);

        TextView tvTopName = (TextView) findViewById(R.id.tv_top_name);
        tvTopName.setText("信息列表");
        etSearch = (EditText) findViewById(R.id.et_serach);
        tvSearchBtn = (TextView) findViewById(R.id.tv_search_btn);
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

    //地区选择提交数据到网络
    private void setData2Netword() {
        isLoading = true;
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.key_type, type);
        hashMap.put(GlobalConstants.key_city, MarkerConstants.value_city);
        hashMap.put(GlobalConstants.key_county, MarkerConstants.value_county);
        hashMap.put(GlobalConstants.key_town, MarkerConstants.value_town);
        hashMap.put(GlobalConstants.key_village, MarkerConstants.value_village);
        hashMap.put(GlobalConstants.key_province, MarkerConstants
                .value_province);

        hashMap.put(GlobalConstants.key_kind, MarkerConstants.value_kind);
        hashMap.put(GlobalConstants.KEY_PAGENUM, pageNum + "");
        hashMap.put(GlobalConstants.KEY_PAGESIZE, GlobalConstants
                .value_page_size);

        MyHttpUtils.getStringDataFromNet(GlobalConstants
                .business_search_area_url, null, hashMap, new MyHttpUtils
                .OnNewWorkRequestListener() {


            @Override
            public void onNewWorkSuccess(String result) {
                if (result != null) {

                    LogUtils.e(TAG, "客商动态选择搜需==" + result);
                    parserData(result);

                    //设置给listview
                }



                lv.onRefreshComplete();
            }

            @Override
            public void onNewWorkError(String msg) {
                ToastUtils.makeTextShowNoNet(BusinessSerachListActivity.this);


                lv.onRefreshComplete();
            }
        });
    }

    //条目搜索
    private void setSearchDataFromNetWork() {

        //网络加载数据
        isLoading = true;
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.key_type, type);
        hashMap.put(GlobalConstants.key_tilte, content);
        hashMap.put(GlobalConstants.KEY_PAGENUM, pageNum + "");
        hashMap.put(GlobalConstants.KEY_PAGESIZE, GlobalConstants
                .value_page_size);
        MyHttpUtils.getStringDataFromNet(GlobalConstants
                        .business_search_title_url,

                null, hashMap, new MyHttpUtils.OnNewWorkRequestListener() {

                    @Override
                    public void onNewWorkSuccess(String result) {

                        if (result != null) {
                            LogUtils.e(TAG, "  " + result);
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
                                (BusinessSerachListActivity.this);
                        if (tvSearchBtn!=null) {
                            tvSearchBtn.setEnabled(true);
                        }
                        lv.onRefreshComplete();
                    }
                });
    }


    //解析数据
    private void parserData(String result) {
        //解析数据
        BusinessDataListInfo businessDataListInfo = GSonUtil
                .parseJson(result, BusinessDataListInfo.class);

        ArrayList<BusinessDataDetailInfo> infoList =
                businessDataListInfo.objList;

        if (infoList != null && infoList.size() > 0) {
            num = businessDataListInfo.num;

            LogUtils.e(TAG, "客商动态体验搜索num=" + num);

            if (isSearch) {
                mAllList.clear();

                isSearch = false;

            }


            mAllList.addAll(infoList);
            showData();

            pageNum++;
            //pageSize += 10;
            isLoading = false;
        } else {
            ToastUtils.makeTextShowNoData(BusinessSerachListActivity.this);
        }
    }


    private void showData() {
        //来自地址搜索
        //type
        switch (type) {
            case "1":
                //客商数据类型
                if (mBusinessAdapter == null) {
                    mBusinessAdapter = new
                            BusinessDatalistAdapter
                            (this, mAllList);
                    lv.setAdapter(mBusinessAdapter);
                } else {
                    mBusinessAdapter.notifyDataSetChanged();
                }

                break;

            case "2":
                //农户数据
                if (mFramAdapter == null) {
                    mFramAdapter = new
                            FramDatalistAdapter
                            (this, mAllList);
                    lv.setAdapter(mFramAdapter);
                } else {
                    mFramAdapter.notifyDataSetChanged();
                }

                break;
        }
    }


    private void intentListener() {

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
                //LogUtils.e(TAG, "点击了搜索");
                if (!TextUtils.isEmpty(content)) {
                    //搜索方法

                    isSearch = true;
                    tvSearchBtn.setEnabled(false);
                    setSearchDataFromNetWork();
                }
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

        lv.setOnItemClickListener(new AdapterView
                .OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View
                    view, int position, long id) {
                //不同的详情页
                switch (type) {
                    case "1":
                        Intent intent = new Intent(BusinessSerachListActivity
                                .this,
                                BusinessDynamicsDetailsActivity.class);
                        //获取条目的id跳转详情页
                        intent.putExtra(GlobalConstants.key_uuid,
                                mAllList.get(position).uuid);
                        startActivity(intent);
                        break;
                    case "2":
                        Intent intent2 = new Intent(BusinessSerachListActivity
                                .this,
                                FarmerDynamicsDetailsActivity.class);
                        //获取条目的id跳转详情页

                        intent2.putExtra(GlobalConstants.key_uuid,
                                mAllList.get(position).uuid);
                        startActivity(intent2);
                        break;
                }
            }
        });

    }

}
