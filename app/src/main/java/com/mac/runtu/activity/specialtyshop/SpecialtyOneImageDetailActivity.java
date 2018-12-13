package com.mac.runtu.activity.specialtyshop;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mac.runtu.R;
import com.mac.runtu.business.MyShowDialog;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.UiUtils;
import com.mac.runtu.view.photoview.PhotoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 特产电商详情图片
 */
public class SpecialtyOneImageDetailActivity extends AppCompatActivity
        implements ViewPager.OnPageChangeListener,View.OnClickListener {

    private static final String TAG = "SpecialtyOneImageDetailActivity";

    @BindView(R.id.tv_one)
    TextView  tvOne;
    @BindView(R.id.tv_all)
    TextView  tvAll;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    private ArrayList<String>        images;


    /**
     * 待展示的图片
     */
    private Bitmap bitmap;
    private int imagePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams
                .FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams
                .FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_specialty_one_image_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        images = (ArrayList<String>)intent
                .getSerializableExtra
                        (GlobalConstants.key_info);

        imagePosition = intent.getIntExtra(GlobalConstants.key_position, 0);

        LogUtils.e(TAG,"点击图片的位置"+imagePosition);

        initView();
        initData();
        initListener();

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




    }

    private void initData() {
        tvAll.setText(String.valueOf(images.size()));

        vp.setAdapter(new DetailImageAdapter());

        vp.setCurrentItem(imagePosition);

    }

    private void initListener() {
        vp.setOnPageChangeListener(this);
        ivShare.setOnClickListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int
            positionOffsetPixels) {
        tvOne.setText(String.valueOf(position + 1));
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        MyShowDialog.showShare(this);

    }


    class DetailImageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public DetailImageAdapter() {
            super();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final int position1 = position;
            final PhotoView view = new PhotoView(SpecialtyOneImageDetailActivity.this);
            view.enable();
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);

            Glide.with(UiUtils.getContext()).load(images.get(position))
                    .asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<?
                        super Bitmap> glideAnimation) {

                   view.setImageBitmap(resource);
                }
            });


            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object
                object) {
            container.removeView((View) object);
        }
    }
}
