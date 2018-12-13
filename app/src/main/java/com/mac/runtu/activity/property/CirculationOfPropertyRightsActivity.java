package com.mac.runtu.activity.property;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.BaseActivity;
import com.mac.runtu.adapter.PropertyListAdapter;
import com.mac.runtu.business.AddressBiz4FromPro3;
import com.mac.runtu.business.TypeSelectBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.PropertyDetailInfo;
import com.mac.runtu.javabean.PropertyInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * 产权流转
 */
public class CirculationOfPropertyRightsActivity extends BaseActivity {

    private static final String TAG = "CirculationOfPropertyRightsActivity";


    private int pageNum = 1;
    private int num;
    private ArrayList<PropertyDetailInfo> mAllList = new ArrayList<>();

    private PropertyListAdapter mPropertyAdapter;

    private ImageView confirmIv;
    private TextView  tvShuruType;
    private ImageView ivShuruType;

    private TextView tvCity;
    private TextView tvArea;

    private ImageView    ivCity;
    private ImageView    ivArea;
    private TextView     tvTown;
    private TextView     tvVillage;
    private ImageView    ivTown;
    private ImageView    ivVillage;
    private LinearLayout llSerachMenu;

    private ImageView confirmIv2;
    private TextView  tvShuruType2;
    private ImageView ivShuruType2;

    private TextView tvCity2;
    private TextView tvArea2;

    private ImageView    ivCity2;
    private ImageView    ivArea2;
    private TextView     tvTown2;
    private TextView     tvVillage2;
    private ImageView    ivTown2;
    private ImageView    ivVillage2;
    private LinearLayout llSerachMenu2;


    private boolean isRefresh = false;


    @Override
    protected int getTopTitleName() {
        return R.string.circulation_of_property_rights;
    }

    @Override
    protected View getFloatView() {
        return getLayoutInflater().inflate(R.layout
                .circulation_of_property_rights_float_menu_layout, null);
    }

    @Override
    public void initView() {

        confirmIv = ButterKnife.findById(floatView, R.id.confirm_Iv);
        tvCity = ButterKnife.findById(floatView, R.id.tv_city);
        tvArea = ButterKnife.findById(floatView, R.id.tv_area);
        tvTown = ButterKnife.findById(floatView, R.id.tv_town);
        tvVillage = ButterKnife.findById(floatView, R.id.tv_village);
        ivCity = ButterKnife.findById(floatView, R.id.iv_city);
        ivArea = ButterKnife.findById(floatView, R.id.iv_area);
        ivTown = ButterKnife.findById(floatView, R.id.iv_town);
        ivVillage = ButterKnife.findById(floatView, R.id.iv_village);

        tvShuruType = ButterKnife.findById(floatView, R.id.tv_shu_ru_type);
        ivShuruType = ButterKnife.findById(floatView, R.id.iv_shu_ru_type);

        llSerachMenu = ButterKnife.findById(floatView, R.id.ll_serach);

        TextView tvSearch = ButterKnife.findById(floatView, R.id.search_Tv);
        tvSearch.setText(R.string.cpr_search_hint);

        confirmIv2 = ButterKnife.findById(menuLL, R.id.confirm_Iv);
        tvCity2 = ButterKnife.findById(menuLL, R.id.tv_city);
        tvArea2 = ButterKnife.findById(menuLL, R.id.tv_area);
        tvTown2 = ButterKnife.findById(menuLL, R.id.tv_town);
        tvVillage2 = ButterKnife.findById(menuLL, R.id.tv_village);
        ivCity2 = ButterKnife.findById(menuLL, R.id.iv_city);
        ivArea2 = ButterKnife.findById(menuLL, R.id.iv_area);
        ivTown2 = ButterKnife.findById(menuLL, R.id.iv_town);
        ivVillage2 = ButterKnife.findById(menuLL, R.id.iv_village);

        tvShuruType2 = ButterKnife.findById(menuLL, R.id.tv_shu_ru_type);
        ivShuruType2 = ButterKnife.findById(menuLL, R.id.iv_shu_ru_type);

        llSerachMenu2 = ButterKnife.findById(menuLL, R.id.ll_serach);

        TextView tvSearch2 = ButterKnife.findById(menuLL, R.id.search_Tv);
        tvSearch2.setText(R.string.cpr_search_hint);
    }


    @Override
    public String getAdType() {
        return GlobalConstants.AD_TYPE_PROPERTY_RIGHT;
    }

    @Override
    protected void initDataOther() {
        new AddressBiz4FromPro3(this,null,null, tvCity, ivCity, tvArea,
                ivArea, tvTown, ivTown, tvVillage, ivVillage);


        new TypeSelectBiz(this, tvShuruType,
                ivShuruType, GlobalConstants.AD_TYPE_PROPERTY_RIGHT);

        new AddressBiz4FromPro3(this,null,null, tvCity2, ivCity2, tvArea2,
                ivArea2, tvTown2, ivTown2, tvVillage2, ivVillage2);


        new TypeSelectBiz(this, tvShuruType2,
                ivShuruType2, GlobalConstants.AD_TYPE_PROPERTY_RIGHT);
    }

    @Override
    protected Class<?> getReleaseActivity() {
        return PropertyRightsRealse1Activity.class;
    }

    @Override
    protected String getUrl() {
        return GlobalConstants.property_list_url;
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

        PropertyInfo propertyInfo = GSonUtil.parseJson
                (result,
                        PropertyInfo.class);
        num = propertyInfo.num;

        LogUtils.e(TAG, "产权流转主页num=" + num);

        ArrayList<PropertyDetailInfo>
                infoList =
                propertyInfo.objList;

        if (infoList != null &&
                infoList.size() > 0) {
            //数据添加到总集合
            if (isRefresh) {
                mAllList.clear();
                isRefresh = false;
            }
            mAllList.addAll(infoList);

            LogUtils.e(TAG, "property" + mAllList.toString());

            if (mPropertyAdapter == null) {
                mPropertyAdapter = new PropertyListAdapter
                        (CirculationOfPropertyRightsActivity
                                .this, mAllList);
                crpLv.setAdapter(mPropertyAdapter);
            } else {
                mPropertyAdapter.notifyDataSetChanged();

            }


            pageNum++;
            //pageSize += 10;
            isLoading = false;


            //写入换存
            mACache.put(cacheKey,result);
            //llLoading.setVisibility(View.GONE);
        } else {
            ToastUtils.makeTextShowNoData
                    (CirculationOfPropertyRightsActivity.this);
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
        if (lastVisiblePosition == mAllList.size() + 1 + 1 &&
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

            LogUtils.e(TAG, "产权流转点击的条木" + position);

            Intent intent = new Intent
                    (CirculationOfPropertyRightsActivity
                            .this, PropertyRightsDetailsActivity.class);
            intent.putExtra(GlobalConstants.key_uuid, mAllList.get
                    (listPosition).uuid);
            LogUtils.e(TAG, "选中的数据 , 对象传输==" + mAllList.get
                    (listPosition).uuid);
            startActivity(intent);
        }
    }

    //出事话监听
    @Override
    public void initListener() {


        confirmIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转选择好的列表页
                Intent intent = new Intent
                        (CirculationOfPropertyRightsActivity.this,
                                PropertyListActivity.class);
                intent.putExtra(GlobalConstants.key_content_ui, GlobalConstants
                        .value_from_select);
                startActivity(intent);
            }
        });

        llSerachMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CirculationOfPropertyRightsActivity
                        .this, PropertyListActivity.class));
            }
        });

        confirmIv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转选择好的列表页
                Intent intent = new Intent
                        (CirculationOfPropertyRightsActivity.this,
                                PropertyListActivity.class);
                intent.putExtra(GlobalConstants.key_content_ui, GlobalConstants
                        .value_from_select);
                startActivity(intent);
            }
        });

        llSerachMenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CirculationOfPropertyRightsActivity
                        .this, PropertyListActivity.class));
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            refreshData();
        }
    }
}