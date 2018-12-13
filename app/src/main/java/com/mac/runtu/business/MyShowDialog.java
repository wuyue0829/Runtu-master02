package com.mac.runtu.business;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.ShoppingCartActivity;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnBooleanListener;
import com.mac.runtu.interfaceif.OnDialogListener;
import com.mac.runtu.interfaceif.OnGetData2BooleanListener;
import com.mac.runtu.utils.LogUtils;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/15 0015 下午 5:25
 */
public class MyShowDialog {

    private static String sfSign;

    /**
     * 提示跳转购物车
     *
     * @param activity
     * @param model
     */
    public static void showDialogGoShopCar(final Activity activity, final
    String model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogView = View.inflate(activity, R.layout
                        .dialg_show_go_shop_car,
                null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();//获取创建的弹窗对象


        Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);

        //设置点击事件
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //密码验证
                Intent intent = new Intent(activity,
                        ShoppingCartActivity.class);
                intent.putExtra(GlobalConstants.key_model, model);
                activity.startActivity(intent);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

       /* Window window = dialog.getWindow();
        WindowManager.LayoutParams params =
                window.getAttributes();

        params.width = 400;
        window.setAttributes(params);*/
    }

    /**
     * 提示选择图片的格式
     *
     * @param activity
     * @param listener
     */
    public static void showDialogSelectImage(final Activity activity, final
    OnBooleanListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogView = View.inflate(activity, R.layout
                        .dialg_show_hint_layout,
                null);


        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();//获取创建的弹窗对象

        TextView tvTitle = (TextView) dialogView.findViewById(R.id
                .tv_title_name);
        Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);


        //设置点击事件
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //密码验证
                if (listener != null) {
                    listener.onResultIsTrue();
                }

                dialog.dismiss();
            }
        });


        dialog.show();

    }

    /**
     * 提示选择图片的格式
     *
     * @param activity
     * @param listener
     */
    public static void showDialogSelectImage(final String title, final String
            content, final
    Activity activity, final
                                             OnBooleanListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogView = View.inflate(activity, R.layout
                        .dialg_show_hint_layout,
                null);


        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();//获取创建的弹窗对象

        TextView tvTitle = (TextView) dialogView.findViewById(R.id
                .tv_title_name);
        tvTitle.setText(title);

        TextView tvContent = (TextView) dialogView.findViewById(R.id
                .et_name);
        tvContent.setText(content);
        Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);


        //设置点击事件
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //密码验证
                if (listener != null) {
                    listener.onResultIsTrue();
                }

                dialog.dismiss();
            }
        });


        dialog.show();

    }


    /**
     * 提示选择默认地址
     *
     * @param activity
     * @param listener
     */
    public static void showDialogAddressIsDefalut(final Activity activity,
                                                  final
                                                  OnGetData2BooleanListener
                                                          listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogView = View.inflate(activity, R.layout
                        .dialg_show_go_shop_car,
                null);

        TextView tvTitle = (TextView) dialogView.findViewById(R.id
                .tv_title_name);
        tvTitle.setText("默认地址选择 : ");
        TextView tvContent = (TextView) dialogView.findViewById(R.id.et_name);
        tvContent.setText("是否设为默认地址");
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();//获取创建的弹窗对象
        dialog.setCancelable(false);//不可以取消(点击弹窗外侧,返回键,弹窗不消失)

        Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);


        //设置点击事件
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //密码验证
                if (listener != null) {
                    listener.onSuccess(true);
                }

                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //密码验证
                if (listener != null) {
                    listener.onFail();
                }

                dialog.dismiss();
            }
        });
        dialog.show();
       /* Window window = dialog.getWindow();
        WindowManager.LayoutParams params =
                window.getAttributes();

        params.width = 500;
        window.setAttributes(params);*/
    }


    /**
     * 提示实名认证
     *
     * @param activity
     * @param listener
     */
    public static void showDialogInfo(final Activity activity, String tilte,
                                      String conent, String okText,
                                      final
                                      OnGetData2BooleanListener
                                              listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogView = View.inflate(activity, R.layout
                        .dialg_show_go_shop_car,
                null);

        TextView tvTitle = (TextView) dialogView.findViewById(R.id
                .tv_title_name);

        tvTitle.setText(tilte);

        TextView tvContent = (TextView) dialogView.findViewById(R.id.et_name);

        tvContent.setText(conent);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();//获取创建的弹窗对象
        dialog.setCancelable(false);//不可以取消(点击弹窗外侧,返回键,弹窗不消失)

        Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);
        btnOk.setText(okText);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);


        //设置点击事件
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //密码验证
                if (listener != null) {
                    listener.onSuccess(true);
                }

                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //密码验证
                if (listener != null) {
                    listener.onFail();
                }

                dialog.dismiss();
            }
        });
        dialog.show();

    }

    /**
     * 登录提示框
     * @param activity
     * @param listener
     */
    public static void showDialogInfo(final Activity activity,final
                                      OnGetData2BooleanListener
                                              listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogView = View.inflate(activity, R.layout
                        .dialg_show_go_shop_car,
                null);

        TextView tvTitle = (TextView) dialogView.findViewById(R.id
                .tv_title_name);

        tvTitle.setText("提示信息 :");

        TextView tvContent = (TextView) dialogView.findViewById(R.id.et_name);

        tvContent.setText("您还没有登录");

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();//获取创建的弹窗对象
        dialog.setCancelable(false);//不可以取消(点击弹窗外侧,返回键,弹窗不消失)

        Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);

        btnOk.setText("登录");
        //设置点击事件
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //密码验证
                if (listener != null) {
                    listener.onSuccess(true);
                }

                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //密码验证
                if (listener != null) {
                    listener.onFail();
                }

                dialog.dismiss();
            }
        });
        dialog.show();

    }


    /**
     * 提示选择身份信息
     *
     * @param activity
     */
    public static void showDialogSelect(final Activity activity, String
            tilte, final OnDialogListener
            listener) {
        sfSign = "1";

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogView = View.inflate(activity, R.layout
                        .dialg_show_select_shenfeninfo,
                null);

        TextView tvTitle = (TextView) dialogView.findViewById(R.id
                .tv_title_name);
        tvTitle.setText(tilte);

        //客商
        LinearLayout llKs = (LinearLayout) dialogView.findViewById(R.id.ll_ks);
        final RadioButton rbKs = (RadioButton) dialogView.findViewById(R.id.rb_ks);
        rbKs.setChecked(true);

        //农户
        LinearLayout llNh = (LinearLayout) dialogView.findViewById(R.id.ll_nh);
        final RadioButton rbNh = (RadioButton) dialogView.findViewById(R.id.rb_nh);

        //普通用户
        LinearLayout llPtyh = (LinearLayout) dialogView.findViewById(R.id
                .ll_ptyh);
        final RadioButton rbPtyh = (RadioButton) dialogView.findViewById(R.id
                .rb_ptyh);

        llKs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfSign = "1";
                rbKs.setChecked(true);

                rbNh.setChecked(false);
                rbPtyh.setChecked(false);
            }
        });

        llNh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfSign = "2";

                rbKs.setChecked(false);
                rbNh.setChecked(true);
                rbPtyh.setChecked(false);
            }
        });

        llPtyh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfSign = "3";

                rbKs.setChecked(false);

                rbNh.setChecked(false);
                rbPtyh.setChecked(true);
            }
        });


        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();//获取创建的弹窗对象
        //dialog.setCancelable(false);//不可以取消(点击弹窗外侧,返回键,弹窗不消失)

        Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);


        //设置点击事件
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //密码验证
                if (listener != null) {
                    listener.onSuccess(sfSign);
                }

                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //密码验证
                if (listener != null) {
                    listener.onFail();
                }

                dialog.dismiss();
            }
        });
        dialog.show();

    }


    //打电话
    public static void ShowCallPhone(final Activity activity, final String
            number,String name) {

        if (number != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("拨打电话!").setMessage("是否要电话联系"+name);

            builder.setNegativeButton("取消", null);

            builder.setPositiveButton("确定", new DialogInterface
                    .OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int
                        which) {
                    Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                    intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intentPhone);


                }
            });

            builder.show();
        }
    }


    /**
     * 删除订单
     *
     * @param activity
     * @param listener
     */
    public static void showDialogDelete(final Activity activity, final
    OnBooleanListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogView = View.inflate(activity, R.layout
                .layout_dialg_order_delete, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();//获取创建的弹窗对象

        dialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onResultIsTrue();
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    /**
     * 得到用户输入的信息
     * @param activity
     * @param title
     * @param listener
     */
    public static void showDialogGetName(final Activity activity,String title,final OnDialogListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogView = View.inflate(activity, R.layout.dialg_set_name,
                null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();//获取创建的弹窗对象
        final EditText etName = (EditText) dialogView.findViewById(R.id
                .et_name);
        TextView tvTitalview = (TextView) dialogView.findViewById(R.id
                .tv_title_name);
        tvTitalview.setText(title);
        Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);

        //设置点击事件
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //密码验证
                String getName = etName.getText().toString().trim();
                if (!TextUtils.isEmpty(getName)) {
                    listener.onSuccess(getName);
                    dialog.dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }


    //分享
    public static void showShare(Activity activity) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("闰土农业");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(GlobalConstants.apk_down_load_url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("闰土app,不一样的电商,不一样的情怀!");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博

        LogUtils.e("图片网路路径=", GlobalConstants.apk_down_load_image_url);
        LogUtils.e("apk下载路径=", GlobalConstants.apk_down_load_url);

        oks.setImageUrl(GlobalConstants.apk_down_load_image_url);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(GlobalConstants.apk_down_load_url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("闰土农业");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(GlobalConstants.apk_down_load_url);

        // 启动分享GUI
        oks.show(activity);
    }

}
