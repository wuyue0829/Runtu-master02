package com.mac.runtu.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.runtu.R;


/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/9/4 17:21
 */
public class ToastUtils {

    public static void makeTextShow(final Activity activity, final String
            text) {


        if (Thread.currentThread().getName().equals("main")) {

            //Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
            customToast(activity, text);
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
                    customToast(activity, text);
                }
            });
        }

    }

    public static void makeTextShowEmpty(Context activity) {
        //Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
        customToast(activity, "内容不能为空");


    }

    public static void makeTextShow(Context activity, String
            text) {
        //Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
        customToast(activity, text);


    }

    public static void makeTextShowNoNet(final Activity activity) {


        if (Thread.currentThread().getName().equals("main")) {

            //Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
            customToast(activity, "系统正在维护中");
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
                    customToast(activity, "系统正在维护中");
                }
            });
        }

    }

    public static void makeTextShowNoData(final Activity activity) {


        if (Thread.currentThread().getName().equals("main")) {

            //Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
            customToast(activity,"暂无数据");
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
                    customToast(activity,"暂无数据");
                }
            });
        }

    }

    public static void makeTextShowContent(final Activity activity) {


        if (Thread.currentThread().getName().equals("main")) {

            //Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
            customToast(activity, "请将内容填写完整");
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
                    customToast(activity, "请将内容填写完整");
                }
            });
        }

    }


    public static void makeTextShowCodeNotEmpty(final Activity activity) {


        if (Thread.currentThread().getName().equals("main")) {

            //Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
            customToast(activity, "请输入验证码!");
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
                    customToast(activity, "请输入验证码!");
                }
            });
        }

    }



    public static void makeTextShowCodeError(final Activity activity) {


        if (Thread.currentThread().getName().equals("main")) {

            //Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
            customToast(activity, "验证码错误!");
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
                    customToast(activity, "验证码错误!");
                }
            });
        }

    }


    public static void makeTextShowIsPhone(final Activity activity) {


        if (Thread.currentThread().getName().equals("main")) {

            //Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
            customToast(activity, "电话号码格式不正确!");
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
                    customToast(activity, "电话号码格式不正确!");
                }
            });
        }

    }

    public static void makeTextShowDataCommitSuccess(final Activity activity) {


        if (Thread.currentThread().getName().equals("main")) {

            //Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
            customToast(activity, "资料提交成功!");
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
                    customToast(activity, "资料提交成功!!");
                }
            });
        }

    }

    public static void makeTextShowDataCommitFail(final Activity activity) {


        if (Thread.currentThread().getName().equals("main")) {

            //Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
            customToast(activity, "资料提交失败!");
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
                    customToast(activity, "资料提交失败!!");
                }
            });
        }

    }


    //吐司的显示样式
    private static void customToast(Context activity, String text) {
        // 创建一个Toast提示信息
        Toast toast = new Toast(activity);
        // 设置Toast的显示位置
        //toast.setGravity(Gravity.CENTER, 0, 0);
        // 创建一个ImageView
        TextView textView = new TextView(activity);
        textView.setText(text);
        // 设置文本框内字号的大小和字体颜色
        textView.setTextSize(UiUtils.px2dip(30));
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.shape_toase_bg);
        toast.setView(textView);
        // 设置Toast的显示时间
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

}
