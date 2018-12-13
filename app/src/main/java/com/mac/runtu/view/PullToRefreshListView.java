package com.mac.runtu.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.mac.runtu.R;
import com.mac.runtu.utils.UiUtils;


/**
 * 下拉刷新ListView
 */
public class PullToRefreshListView extends ListView {

    private static final String TAG = "PullToRefreshListView";
    public PullToRefreshListView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);

        setHeaderDividersEnabled(true);
        //禁止底部出现分割线
        setFooterDividersEnabled(false);
        setDivider(new ColorDrawable(0x996e6e6e));
        setDividerHeight(UiUtils.px2dip(1));

        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public PullToRefreshListView(Context context) {
        this(context, null);
    }


    //初始化脚布局
    private void initFooterView() {
        mFooterView = View.inflate(getContext(),
                R.layout.pull_to_refresh_footer, null);
        addFooterView(mFooterView);

        mFooterView.measure(0, 0);
        //mFooterViewHeight = mFooterView.getMeasuredHeight();

        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);


        onRefreshComplete();
        //设置滑动监听


    }


    private View mFooterView;
    private int mFooterViewHeight = UiUtils.px2dip(200);


    //收起下拉刷新控件
    public void onRefreshComplete() {
        //正在加载更多


        //隐藏脚布局
        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);

        if (mFooterView!= null){
            mFooterView.setVisibility(View.GONE);
        }


    }

    //收起下拉刷新控件
    public void onRefreshOpen() {
        //正在加载更多
        //隐藏脚布局
       // mFooterView.setPadding(0, 10, 0, 0);
        if (mFooterView!= null){
            mFooterView.setVisibility(View.VISIBLE);
        }
        mFooterView.setPadding(0,20,0,20);

    }

}
