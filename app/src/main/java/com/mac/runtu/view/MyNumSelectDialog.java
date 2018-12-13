package com.mac.runtu.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.utils.UiUtils;

/**
 * Description: 购买领养树量选择
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/17 0017 下午 6:50
 */
public class MyNumSelectDialog extends Dialog {

    private final ImageView ivClose;
    private final Button    btnAdd;
    private final Button    btnCut;
    private final Button    btnOk;
    private final TextView  etGoodNum;
    private int num = 1;

    public MyNumSelectDialog(Context context) {

        super(context, R.style.BuyNumSelectDialogStyle);
        //给弹窗设置布局
        setContentView(R.layout.dialog_buy_num_select);

        ivClose = (ImageView) findViewById(R.id.iv_close);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnCut = (Button) findViewById(R.id.btn_cut);
        btnOk = (Button) findViewById(R.id.btn_ok);
        etGoodNum = (TextView) findViewById(R.id.tv_goodnum);

        int screenWidth = UiUtils.getScreenWidth();



        Window window = getWindow();//当前弹窗所在的窗口对象

        WindowManager.LayoutParams attributes = window.getAttributes();
        //获取当前弹窗属性(布局参数)
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        //修改重心位置为底端水平居中
        //重新给窗口设置最新属性
        attributes.width = screenWidth;

        window.setAttributes(attributes);

        initListener();
    }

    private void initListener() {

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = Integer.valueOf(etGoodNum.getText()
                        .toString());

                num++;
                etGoodNum.setText(num + "");
            }
        });

        btnCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = Integer.valueOf(etGoodNum.getText()
                        .toString());

                if (num > 1) {
                    num--;
                    etGoodNum.setText(num + "");
                }

            }
        });
    }

    public void setIvCloseOnClickListener(View.OnClickListener listener) {
        ivClose.setOnClickListener(listener);
    }


    public void setBtnOkOnClickListener(View.OnClickListener listener) {
        btnOk.setOnClickListener(listener);
    }

    public int getTextCount() {
        return num;
    }
}
