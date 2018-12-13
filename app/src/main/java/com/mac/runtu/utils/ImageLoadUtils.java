package com.mac.runtu.utils;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/9/23 0023 下午 4:21
 */
public class ImageLoadUtils {


    /**
     * 加载矩形图片(从路径)
     *
     * @param activity
     * @param url
     * @param imageView
     */
    public static void rectangleImage(Activity activity, String url, ImageView
            imageView
    ) {
        Glide
                .with(activity) // 指定Context
                .load(url)// 指定图片的URL
                .into(imageView);//指定显示图片的ImageView

    }

    /**
     * 加载矩形图片(从路径)
     *
     *
     * @param url
     * @param imageView
     */
    public static void rectangleImage(Context context, String url, ImageView
            imageView
    ) {
        Glide
                .with(context) // 指定Context
                .load(url)// 指定图片的URL
                .into(imageView);//指定显示图片的ImageView

    }


    /**
     * 加载矩形图片(从路径)
     *
     *
     * @param url
     * @param imageView
     */
    public static void rectangleImage(String url, ImageView
            imageView
    ) {
        Glide
                .with(UiUtils.getContext()) // 指定Context
                .load(url)// 指定图片的URL
                .into(imageView);//指定显示图片的ImageView

    }

    /**
     * 加载矩形图片(从路径)
     *
     * @param activity
     *
     * @param imageView
     */
    public static void rectangleImage(Activity activity,int id, ImageView
            imageView
    ) {
        Glide
                .with(activity) // 指定Context
                .load(id)// 指定图片的URL
                .into(imageView);//指定显示图片的ImageView

    }

    /**
     * 加载矩形图片(从路径)
     *
     *
     * @param imageView
     */
    public static void rectangleImage(int id, ImageView
            imageView
    ) {
        Glide
                .with(UiUtils.getContext()) // 指定Context
                .load(id)// 指定图片的URL
                .into(imageView);//指定显示图片的ImageView

    }

    /**
     * 加载矩形图片(从路径)
     *
     * @param imageView
     */
    public static void rectangleImageId(Context context,int id, ImageView
            imageView
    ) {
        Glide
                .with(context) // 指定Context
                .load(id)// 指定图片的URL
                .into(imageView);//指定显示图片的ImageView

    }






    /**
     * 加载矩形图片 无缓存
     *
     * @param activity
     * @param url
     * @param imageView
     */
    public static void rectangleImageNoCache(Context activity, String url,
                                             ImageView
                                                     imageView
    ) {
        Glide
                .with(activity) // 指定Context
                .load(url)// 指定图片的URL
                .crossFade()
                .skipMemoryCache(true)// 跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
                .into(imageView);//指定显示图片的ImageView
    }

    /**
     * 加载矩形图片 无缓存
     *
     * @param url
     * @param imageView
     */
    public static void rectangleImageNoCache(String url,
                                             ImageView
                                                     imageView
    ) {
        Glide
                .with(UiUtils.getContext()) // 指定Context
                .load(url)// 指定图片的URL
                .crossFade()
                .skipMemoryCache(true)// 跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
                .into(imageView);//指定显示图片的ImageView
    }


    /**
     * 加载圆形图片
     *
     * @param activity
     * @param url
     * @param imageView
     */
    public static void roundImage(Context activity, String url, ImageView
            imageView
    ) {
        Glide
                .with(activity) // 指定Context
                .load(url + "")// 指定图片的URL
                .transform(new GlideCircleTransform(activity)) // 指定自定义图片样式
                .crossFade()
                .into(imageView);//指定显示图片的ImageView
    }

    /**
     * 加载圆形图片
     *
     * @param url
     * @param imageView
     */
    public static void roundImage(String url, ImageView
            imageView
    ) {
        Glide
                .with(UiUtils.getContext()) // 指定Context
                .load(url)// 指定图片的URL
                .transform(new GlideCircleTransform(UiUtils.getContext())) // 指定自定义图片样式
                .crossFade()
                .into(imageView);//指定显示图片的ImageView
    }



    /**
     * 加载圆形图片 无缓存
     *
     * @param activity
     * @param url
     * @param imageView
     */
    public static void roundImageNoCache(Context activity, String url,
                                         ImageView
                                                 imageView
    ) {
        Glide
                .with(activity) // 指定Context
                .load(url)// 指定图片的URL
                .transform(new GlideCircleTransform(activity)) // 指定自定义图片样式
                .crossFade()
                .skipMemoryCache(true)// 跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
                .into(imageView);//指定显示图片的ImageView
    }

    /**
     * 加载圆形图片 无缓存
     *
     * @param url
     * @param imageView
     */
    public static void roundImageNoCache(String url,
                                         ImageView
                                                 imageView
    ) {
        Glide
                .with(UiUtils.getContext()) // 指定Context
                .load(url)// 指定图片的URL
                .transform(new GlideCircleTransform(UiUtils.getContext())) // 指定自定义图片样式
                .crossFade()
                .skipMemoryCache(true)// 跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
                .into(imageView);//指定显示图片的ImageView
    }



    /**
     * 加载圆形图片
     *
     * @param activity
     * @param url
     * @param imageView
     */
    public static void roundImage(Context activity, Uri url, ImageView
            imageView
    ) {
        Glide
                .with(activity) // 指定Context
                .load(url)// 指定图片的URL
                .transform(new GlideCircleTransform(activity)) // 指定自定义图片样式
                .crossFade(1500)  //淡入淡出 时间
                .into(imageView);//指定显示图片的ImageView
    }

    /**
     * 加载圆形图片
     *
     * @param url
     * @param imageView
     */
    public static void roundImage(Uri url, ImageView
            imageView
    ) {
        Glide
                .with(UiUtils.getContext()) // 指定Context
                .load(url)// 指定图片的URL
                .transform(new GlideCircleTransform(UiUtils.getContext())) // 指定自定义图片样式
                .crossFade(1500)  //淡入淡出 时间
                .into(imageView);//指定显示图片的ImageView
    }

    /**
     * 加载圆角图片
     * @param activity
     * @param url
     * @param imageView
     */
    public static void rectangleImageRadius(Activity activity, String url, ImageView
            imageView
    ) {
        Glide
                .with(activity) // 指定Context
                .load(url)// 指定图片的URL
                .transform(new GlideRoundTransform(activity,10)) // 指定自定义图片样式
                .crossFade(1500)  //淡入淡出 时间
                .into(imageView);//指定显示图片的ImageView
    }

    /**
     * 加载圆角图片
     * @param url
     * @param imageView
     */
    public static void rectangleImageRadius(String url, ImageView
            imageView
    ) {
        Glide
                .with(UiUtils.getContext()) // 指定Context
                .load(url)// 指定图片的URL
                .transform(new GlideRoundTransform(UiUtils.getContext(),10)) // 指定自定义图片样式
                .crossFade(1500)  //淡入淡出 时间
                .into(imageView);//指定显示图片的ImageView
    }


    /**
     * 圆形图片
     */
    public static class GlideCircleTransform extends BitmapTransformation {
        private float radius = 0f;

        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int
                outWidth, int outHeight) {


            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null)
                return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config
                        .ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode
                    .CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }

    /**
     * 加载圆角图片
     */
    public static class GlideRoundTransform extends BitmapTransformation {

        private static float radius = 0f;

        public GlideRoundTransform(Context context) {
            this(context, 4);
        }

        public GlideRoundTransform(Context context, int dp) {
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density *
                    dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int
                outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null)
                return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(),
                    Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source
                        .getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode
                    .CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source
                    .getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }

}
