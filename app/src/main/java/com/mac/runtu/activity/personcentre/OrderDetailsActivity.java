package com.mac.runtu.activity.personcentre;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.SelectPayWayActivity;
import com.mac.runtu.business.OrderGetGoodInfrom2NetBiz;
import com.mac.runtu.fragment.WaitGetGoodsFragment;
import com.mac.runtu.interfaceif.OnGetData2BooleanListener;
import com.mac.runtu.utils.FragmentFactory;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.SPUtils;

/**
 * Created by Administrator on 2018/5/11 0011.
 */

public class OrderDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private float EstCost;
    private ImageView back_Iv;
    private String busiUuid;
    private String cost;
    private String createTime;
    private String danjia;
    private ImageView im_phone;
    private String logisticsNumber;
    private Context mContext;
    private String orderCode;
    private int orderStatus;
    private String orderUuid;
    private Button order_details_Iv;
    private String paramName;
    private String pic;
    private String poname;
    private int position;
    private String reciveAddress;
    private String recivePhone;
    private String reciveUser;
    private RelativeLayout rl_bottom;
    private RelativeLayout rl_content;
    private RelativeLayout rl_danhao;
    private int shuliang;
    private TextView tv_address;
    private TextView tv_chuangjiandate;
    private TextView tv_danhao;
    private TextView tv_dingdanbianhao;
    private TextView tv_fahuodate;
    private TextView tv_fukuandate;
    private TextView tv_jiage;
    private TextView tv_minzongjia;
    private TextView tv_name;
    private TextView tv_orderstate;
    private TextView tv_paramName;
    private TextView tv_phone;
    private TextView tv_po_one;
    private TextView tv_poname;
    private TextView tv_shuliang;
    private TextView tv_zongjia;
    private String uuid;
    private String valueMode;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        this.mContext = this;
        initView();
        initData();
    }


    private void initView()
    {
        this.back_Iv = ((ImageView)findViewById(R.id.back_Iv));
        this.tv_orderstate = ((TextView)findViewById(R.id.tv_orderstate));
        this.tv_dingdanbianhao = ((TextView)findViewById(R.id.tv_dingdanbianhao));
        this.tv_chuangjiandate = ((TextView)findViewById(R.id.tv_chuangjiandate));
        this.tv_jiage = ((TextView)findViewById(R.id.tv_jiage));
        this.tv_poname = ((TextView)findViewById(R.id.tv_poname));
        this.rl_content = ((RelativeLayout)findViewById(R.id.rl_content));
        this.tv_fukuandate = ((TextView)findViewById(R.id.tv_fukuandate));
        this.tv_name = ((TextView)findViewById(R.id.tv_name));
        this.tv_fahuodate = ((TextView)findViewById(R.id.tv_fahuodate));
        this.im_phone = ((ImageView)findViewById(R.id.im_phone));
        this.tv_shuliang = ((TextView)findViewById(R.id.tv_shuliang));
        this.tv_address = ((TextView)findViewById(R.id.tv_address));
        this.tv_phone = ((TextView)findViewById(R.id.tv_phone));
        this.tv_zongjia = ((TextView)findViewById(R.id.tv_zongjia));
        this.tv_minzongjia = ((TextView)findViewById(R.id.tv_minzongjia));
        this.tv_po_one = ((TextView)findViewById(R.id.tv_po_one));
        this.tv_paramName = ((TextView)findViewById(R.id.tv_paramName));
        this.rl_danhao = ((RelativeLayout)findViewById(R.id.rl_danhao));
        this.tv_danhao = ((TextView)findViewById(R.id.tv_danhao));
        this.rl_bottom = ((RelativeLayout)findViewById(R.id.rl_bottom));
        this.order_details_Iv = ((Button)findViewById(R.id.order_details_Iv));
        this.back_Iv.setOnClickListener(this);
        this.rl_content.setOnClickListener(this);
    }


    private void initData()
    {
        this.orderStatus = getIntent().getExtras().getInt("orderStatus");
        this.pic = getIntent().getExtras().getString("pic");
        this.createTime = getIntent().getExtras().getString("createTime");
        this.poname = getIntent().getExtras().getString("poname");
        this.shuliang = getIntent().getExtras().getInt("shuliang");
        this.cost = getIntent().getExtras().getString("cost");
        this.reciveAddress = getIntent().getExtras().getString("reciveAddress");
        this.recivePhone = getIntent().getExtras().getString("recivePhone");
        this.reciveUser = getIntent().getExtras().getString("reciveUser");
        this.orderCode = getIntent().getExtras().getString("orderCode");
        this.danjia = getIntent().getExtras().getString("danjia");
        this.valueMode = getIntent().getExtras().getString("model");
        this.busiUuid = getIntent().getExtras().getString("busiUuid");
        this.orderUuid = getIntent().getExtras().getString("orderUuid");
        this.uuid = getIntent().getExtras().getString("uuid");
        this.position = getIntent().getExtras().getInt("position");
        this.paramName = getIntent().getExtras().getString("paramName");
        this.logisticsNumber = getIntent().getExtras().getString("logisticsNumber");
        this.EstCost = Float.parseFloat(this.cost);
        this.tv_paramName.setText(this.paramName);
        this.tv_dingdanbianhao.setText("订单编号:" + this.orderCode);
        this.tv_name.setText("收货人:" + this.reciveUser);
        this.tv_address.setText("收货地址:" + this.reciveAddress);
        this.tv_phone.setText(this.recivePhone + "");
        this.tv_po_one.setText("x" + this.cost);
        this.tv_jiage.setText("¥" + this.danjia);
        this.tv_shuliang.setText("x" + this.shuliang);
        this.tv_poname.setText(this.poname);
        this.tv_zongjia.setText("¥" + this.cost);
        this.tv_minzongjia.setText(this.cost);
        this.tv_chuangjiandate.setText("创建时间：" + this.createTime);
        if (this.orderStatus == 0)
        {
            this.tv_orderstate.setText("订单状态：等待您的付款");
            this.order_details_Iv.setText("确认付款");
            this.tv_fukuandate.setVisibility(View.GONE);
            this.tv_fahuodate.setVisibility(View.GONE);
            this.rl_danhao.setVisibility(View.GONE);
            this.order_details_Iv.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View paramAnonymousView)
                {
                    Intent intent = new Intent(OrderDetailsActivity.this.mContext, SelectPayWayActivity.class);
                    intent.putExtra("uuid", OrderDetailsActivity.this.uuid);
                    intent.putExtra("allPay", OrderDetailsActivity.this.EstCost);
                    SPUtils.setBoolean(OrderDetailsActivity.this.mContext, "pay_from", true);
                    OrderDetailsActivity.this.mContext.startActivity(intent);
                    OrderDetailsActivity.this.finish();
                }
            });
        }else if(this.orderStatus == 1){
            tv_orderstate.setText("订单状态：您已付款，等待卖家发货");
            this.rl_bottom.setVisibility(View.GONE);
            this.rl_danhao.setVisibility(View.GONE);
            this.tv_fahuodate.setVisibility(View.GONE);
        }else if(this.orderStatus == 2){
            this.tv_orderstate.setText("订单状态：卖家已发货，等待确认收货");
            this.order_details_Iv.setText("确认收货");
            this.rl_danhao.setVisibility(View.VISIBLE);
            if ((this.logisticsNumber != null) && (this.logisticsNumber.length() > 0)) {
                if (this.logisticsNumber.equals("null")) {
                    this.tv_danhao.setText("暂时没有物流信息");
                }else{
                    this.rl_bottom.setVisibility(View.GONE);
                    this.tv_danhao.setText("物流单号：" + this.logisticsNumber);
                    this.tv_danhao.setText("暂时没有物流信息");
                }
            }
        }else if(this.orderStatus == 3){
            this.tv_orderstate.setText("订单状态：订单已完成");
            if ((this.logisticsNumber != null) && (this.logisticsNumber.length() > 0)) {
                if (this.logisticsNumber.equals("null")) {
                    this.tv_danhao.setText("暂时没有物流信息");
                }else{
                    this.rl_bottom.setVisibility(View.GONE);
                    this.tv_danhao.setText("物流单号：" + this.logisticsNumber);
                    this.tv_danhao.setText("暂时没有物流信息");
                }
            }
        }else if(this.orderStatus == 4){
            this.tv_orderstate.setText("订单状态：订单已取消");
            this.rl_danhao.setVisibility(View.GONE);
            this.rl_bottom.setVisibility(View.GONE);
        }
        ImageLoadUtils.rectangleImage("http://101.201.102.161/upload/" + this.pic, this.im_phone);
    }

    private void informFwd(final int paramInt)
    {
        OrderGetGoodInfrom2NetBiz.infromFWD(this.uuid, "3", new OnGetData2BooleanListener()
        {
            public void onFail() {}

            public void onSuccess(Boolean paramAnonymousBoolean)
            {
                if (paramAnonymousBoolean.booleanValue()) {
                    ((WaitGetGoodsFragment) FragmentFactory.getOrderFragment(3)).refreshListData(paramInt);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_Iv:
                finish();
                break;
        }
    }
}
