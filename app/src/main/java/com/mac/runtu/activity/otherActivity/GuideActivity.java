package com.mac.runtu.activity.otherActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mac.runtu.R;
import com.mac.runtu.utils.DensityUtils;
import com.mac.runtu.utils.SPUtils;

import java.util.ArrayList;

/**
 * 引导页
 */
public class GuideActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private Button btnStart;
    private ArrayList<ImageView> mImageViews;
    private LinearLayout llContainer;//小圆点的容器
    private ImageView ivRedPoint;//小红点
    private int mPointPos;//圆点每次移动的总距离
    private Button bt_jump;

    private int[] mImageIds = new int[] { R.drawable.guide01,
            R.drawable.guide02, R.drawable.guide03,R.drawable.guide04 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_guide);


        initViews();
        initData();
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

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);

        btnStart = (Button) findViewById(R.id.btn_start);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
        bt_jump = (Button) findViewById(R.id.bt_jump);
    }

    private void initData() {
        btnStart.setVisibility(View.GONE);

        //初始化3个引导页的imageView对象
        mImageViews = new ArrayList<ImageView>();
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);//以背景方式设置, 可以填充屏幕
            mImageViews.add(view);

            //初始化小圆点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.shape_point_stroke_gray);

            //设置布局参数, 修改左边距
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            if (i > 0) {
                params.leftMargin = DensityUtils.dp2px(this, 15);//设置左边距
            }

            //设置布局参数
            point.setLayoutParams(params);

            //给容器添加小圆点
            llContainer.addView(point);
        }

        //设置数据
        mViewPager.setAdapter(new GuideAdapter());

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //页面滑动监听
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                System.out.println("当前位置:" + position + ";偏移百分比:"
                        + positionOffset);
                //计算圆点当前的左边距, 注意加上当前页面所在位置本身已有的宽度
                int left = (int) (mPointPos * positionOffset) + position
                        * mPointPos;
                //通过不断修改小红点的左边距来达到移动小红点的目的
                //父控件是谁,就使用谁的布局参数
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint
                        .getLayoutParams();//获取小红点当前的布局参数
                //修改左边距
                params.leftMargin = left;
                //重新设置布局参数
                ivRedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == mImageIds.length - 1) {
                    //最后一个页面显示开始体验按钮
                    btnStart.setVisibility(View.VISIBLE);
                } else {
                    btnStart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });


        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    //layout执行结束, 布局的位置已经确定
                    @Override
                    public void onGlobalLayout() {
                        mPointPos = llContainer.getChildAt(1).getLeft()
                                - llContainer.getChildAt(0).getLeft();

                        //System.out.println("圆点每次移动的总距离:" + mPointPos);

                        //取消视图树的监听
                        ivRedPoint.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //表示已经进入新手引导, 下次启动不再进入
                SPUtils.setBoolean(GuideActivity.this, "is_guide_show",
                        true);

                startActivity(new Intent(getApplicationContext(),
                        MainActivity.class));
                finish();
            }
        });


        bt_jump.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //表示已经进入新手引导, 下次启动不再进入
                SPUtils.setBoolean(GuideActivity.this, "is_guide_show",
                        true);

                startActivity(new Intent(getApplicationContext(),
                        MainActivity.class));
                finish();
            }
        });
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //ImageView view = new ImageView(getApplicationContext());
            ImageView view = mImageViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
       // MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }
}
