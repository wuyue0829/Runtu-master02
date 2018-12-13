package com.mac.runtu.activity.agricumach;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.BaseActivity;
import com.mac.runtu.adapter.AgricMachineryAdapter;
import com.mac.runtu.business.AddressBiz4FromPro3;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.AgricuMachineryDetailInfo;
import com.mac.runtu.javabean.AgricuMachineryInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * 农资农机在线市场
 */
public class AgriculturalMachineryActivity extends BaseActivity {

    private static final String TAG = "AgriculturalMachineryActivity";
    private ArrayList<AgricuMachineryDetailInfo> mAllList = new ArrayList<>();
    private AgricMachineryAdapter mAgricMachAdapter;

    private TextView            tvCity;
    private TextView            tvArea;
    private ImageView           ivCity;
    private ImageView           ivArea;
    private TextView            tvTown;
    private TextView            tvVillage;
    private ImageView           ivTown;
    private ImageView           ivVillage;
    private ImageView           confirmIv;
    private LinearLayout        llSerachMenu;

    private TextView     tvCity2;
    private TextView     tvArea2;
    private ImageView    ivCity2;
    private ImageView    ivArea2;
    private TextView     tvTown2;
    private TextView     tvVillage2;
    private ImageView    ivTown2;
    private ImageView    ivVillage2;
    private ImageView    confirmIv2;
    private LinearLayout llSerachMenu2;

    private boolean isRefresh = false;
    private int pageNum   = 1;
    private int num;


    @Override
    protected int getTopTitleName() {
        return R.string.agricultural_machinery;
    }


    @Override
    protected View getFloatView() {
        return View.inflate(this, R.layout
                .agricultural_machinery_float_menu_layout, null);
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

        confirmIv = ButterKnife.findById(floatView, R.id.confirm_Iv);

        llSerachMenu = ButterKnife.findById(floatView, R.id.ll_serach);
        TextView tvSearch = ButterKnife.findById(floatView, R.id.search_Tv);

        tvSearch.setText(UiUtils.getString(R.string
                .agricultural_machinery_search_hint));

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
                .agricultural_machinery_search_hint));


    }


    @Override
    public String getAdType() {
        return GlobalConstants.AD_TYPE_MACHINE;
    }


    @Override
    protected void initDataOther() {

        new AddressBiz4FromPro3(this,null,null, tvCity, ivCity, tvArea,
                ivArea, tvTown, ivTown, tvVillage, ivVillage);

        new AddressBiz4FromPro3(this,null,null,tvCity2, ivCity2, tvArea2,
                ivArea2, tvTown2, ivTown2, tvVillage2, ivVillage2);

    }

    @Override
    protected Class<?> getReleaseActivity() {
        return AgriculturaMachineryRelease1Activity.class;
    }

    @Override
    protected String getUrl() {
        return GlobalConstants.mach_list_url;
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
    public void parserData(String result) {
        AgricuMachineryInfo info = GSonUtil.parseJson(result,
                AgricuMachineryInfo.class);

        num = info.num;
        ArrayList<AgricuMachineryDetailInfo> infoList = info.objList;
        if (infoList != null && infoList.size() > 0) {

            if (isRefresh) {
                mAllList.clear();
                isRefresh = false;
            }
            mAllList.addAll(infoList);


            if (mAgricMachAdapter == null) {
                mAgricMachAdapter = new
                        AgricMachineryAdapter(AgriculturalMachineryActivity
                        .this,
                        mAllList);
                crpLv.setAdapter(mAgricMachAdapter);
            } else {
                mAgricMachAdapter.notifyDataSetChanged();

            }

            pageNum++;
            //pageSize += 10;
            isLoading = false;

            //写入换存
            mACache.put(cacheKey,result);

        } else {
            ToastUtils.makeTextShowNoData(AgriculturalMachineryActivity.this);
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
        //如果isLoading=true表示正在加载,还没结束,此时不允许加载下一页
        if (lastVisiblePosition == mAllList.size() - 1 + 2 + 1 &&
                !isLoading && mAllList.size() < num) {
            //加载数据
            crpLv.onRefreshOpen();
            initData();
        }
    }

    @Override
    public void lvOnItemClick(int position) {
        int infoPosition = position - 2;
        if (position > 1) {

            if (mAllList.get(infoPosition)==null) {
                return;

            }
            Intent intent = new Intent(AgriculturalMachineryActivity
                    .this,
                    AgriculturalMachineryDetailsActivity.class);
            intent.putExtra(GlobalConstants.key_uuid, mAllList.get
                    (infoPosition).uuid);
            startActivity(intent);
        }
    }


    //初始化监听
    public void initListener() {

        llSerachMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AgriculturalMachineryActivity
                        .this,
                        AgricuMachineryListActivity.class));
            }
        });

        confirmIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转查询列表页
                Intent intent = new Intent(AgriculturalMachineryActivity
                        .this, AgricuMachineryListActivity.class);
                //标记是哪个
                intent.putExtra(GlobalConstants.key_content_ui,
                        GlobalConstants.value_from_select);
                startActivity(intent);
            }
        });

        llSerachMenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AgriculturalMachineryActivity
                        .this,
                        AgricuMachineryListActivity.class));
            }
        });

        confirmIv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转查询列表页
                Intent intent = new Intent(AgriculturalMachineryActivity
                        .this, AgricuMachineryListActivity.class);
                //标记是哪个
                intent.putExtra(GlobalConstants.key_content_ui,
                        GlobalConstants.value_from_select);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            refreshData();
        }
    }

}
