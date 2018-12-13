package com.mac.runtu.business;

import android.content.Context;
import android.widget.ImageView;

import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.UiUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/28 0028 下午 8:02
 */
public class TopImageBannerBiz {
    public Banner banner;


    public TopImageBannerBiz(Banner banner) {
        this.banner = banner;


        //设置图片加载器
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView
                    imageView) {
                //Picasso.with(context).load(path.toString()).into(imageView);

                if (UiUtils.isRunOnUiThread()) {
                    if (path instanceof Integer) {
                        ImageLoadUtils.rectangleImageId(UiUtils.getContext(),
                                (Integer) path, imageView);

                    } else if (path instanceof String) {
                        ImageLoadUtils.rectangleImage(UiUtils.getContext(),
                                (String) path, imageView);
                    }
                }


            }
        });
    }


    public void autoShowImage(List<?> images) {
        autoShowImage(images, null);
    }

    public void autoShowImage(List<?> images, final OnBannerClickeListener
            listener) {

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        //        banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);

        if (listener != null) {
            banner.setOnBannerClickListener(new OnBannerClickListener() {
                @Override
                public void OnBannerClick(int position) {
                    listener.onBannerClickeListener(position);
                }
            });
        }

        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }


    public interface OnBannerClickeListener {
        void onBannerClickeListener(int paramInt);
    }
}
