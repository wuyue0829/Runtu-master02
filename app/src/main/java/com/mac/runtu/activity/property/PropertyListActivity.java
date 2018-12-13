package com.mac.runtu.activity.property;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.adapter.PropertyListAdapter;
import com.mac.runtu.business.FromNetInfo2AddsSelectBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.PropertyDetailInfo;
import com.mac.runtu.javabean.PropertyInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.view.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 产权管理的列表页
 */
public class PropertyListActivity extends AppCompatActivity {
    private static final String TAG = "PropertyListActivity";

    @BindView(R.id.back_Iv)
    ImageView      backIv;
    @BindView(R.id.title_RL)
    RelativeLayout titleRL;

    private ArrayList<PropertyDetailInfo> mAllList = new
            ArrayList<>();
    private PropertyListAdapter mPropertyAdapter;


    private int pageNum = 1;
    private PullToRefreshListView lv;
    private boolean               isLoading;
    private LinearLayout          llLoading;
    private String[] selectAddress = new String[5];
    private int          num;
    private TextView     tvTopName;
    private LinearLayout llSerachEt;
    private EditText     etSearch;
    private TextView     tvSearchBtn;
    private String       content;
    private int          search_select;
    private boolean isSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_list);
        ButterKnife.bind(this);
        initView();
        initListener();

        Intent intent = getIntent();
        String value = intent.getStringExtra(GlobalConstants.key_content_ui);
        String result = intent.getStringExtra(GlobalConstants.key_user);

        if (value != null && GlobalConstants.value_from_select.equals(value)) {
            //来自选择按钮
            search_select = 0;
            initData();

        } else if (result != null) {
            //isFromUser = true;
            parserData(result);
        } else {
            search_select = 1;
            llSerachEt.setVisibility(View.VISIBLE);
            etSearch.setHint(R.string.cpr_search_hint);
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

        llSerachEt = (LinearLayout) findViewById(R.id.ll_serach_et);
        llSerachEt.setVisibility(View.GONE);

        etSearch = (EditText) findViewById(R.id.et_serach);
        tvSearchBtn = (TextView) findViewById(R.id.tv_search_btn);

        lv = (PullToRefreshListView) findViewById(R.id.lv);
        //添加进入的不局顶部名称
        tvTopName = (TextView) findViewById(R.id.tv_top_name);
        tvTopName.setText("信息列表");
        //llLoading = (LinearLayout) findViewById(R.id.ll_loading);
    }


    //地区选择提交数据到网络
    private void setData2Netword() {

        // llLoading.setVisibility(View.VISIBLE);

        //还原pageNum和pageSize

        FromNetInfo2AddsSelectBiz.setData2Netword(GlobalConstants
                .property_select_adddress_url,pageNum, new FromNetInfo2AddsSelectBiz
                .OnAddressDataListener() {


            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    //解析数据
                    LogUtils.e(TAG, "产权流转信息列表" + result);
                    //设置给listview
                    parserData(result);

                }

                lv.onRefreshComplete();
            }

            @Override
            public void onfail() {
                ToastUtils.makeTextShowNoNet(PropertyListActivity.this);

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
                        .property_search_title_url,
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
                                (PropertyListActivity.this);
                        if (tvSearchBtn!=null) {
                            tvSearchBtn.setEnabled(true);
                        }
                        lv.onRefreshComplete();
                    }
                });
    }

    //解析数据
    private void parserData(String result) {

        PropertyInfo propertyInfo = GSonUtil.parseJson
                (result,
                        PropertyInfo.class);
        num = propertyInfo.num;

        LogUtils.e(TAG,"产权流转搜索页num="+num);

        ArrayList<PropertyDetailInfo>
                propertyDetailInfoArrayList =
                propertyInfo.objList;

        if (propertyDetailInfoArrayList != null &&
                propertyDetailInfoArrayList.size() > 0) {
            //数据添加到总集合
            if (isSearch) {
                mAllList.clear();
                isSearch = false;
            }

            mAllList.addAll(propertyDetailInfoArrayList);

            LogUtils.e(TAG, "property" + mAllList.toString());

            if (mPropertyAdapter == null) {
                mPropertyAdapter = new PropertyListAdapter
                        (PropertyListActivity
                                .this, mAllList);
                lv.setAdapter(mPropertyAdapter);
            } else {
                mPropertyAdapter.notifyDataSetChanged();

            }

            pageNum++;
            // pageSize += 10;
            isLoading = false;
            //llLoading.setVisibility(View.GONE);
        } else {
            // llLoading.setVisibility(View.GONE);
            ToastUtils.makeTextShowNoData(PropertyListActivity.this);
        }


    }

    private void initListener() {
        tvSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = etSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    //搜索方法
                    isSearch =true;
                    tvSearchBtn.setEnabled(false);
                    setSearchDataFromNetWork();
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
        //确定按钮


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

                Intent intent = new Intent(PropertyListActivity
                        .this, PropertyRightsDetailsActivity.class);

                intent.putExtra(GlobalConstants.key_uuid, mAllList.get
                        (position).uuid);

                LogUtils.e(TAG, "选中的数据" + mAllList.get
                        (position).title);
                startActivity(intent);
            }
        });
    }



}
