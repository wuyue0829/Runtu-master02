package com.mac.runtu.view;


import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mac.runtu.R;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/22 10:52
 */
public class SetNameDialog extends Dialog implements View.OnClickListener {


    private final EditText etName;
    private final TextView tvTitalview;
    private TextView  tvName;

    public SetNameDialog(Context context) {
        super(context);

        setContentView(R.layout.dialg_set_name);
        setCanceledOnTouchOutside(true);

        Window window = getWindow();
        WindowManager.LayoutParams params =
                window. getAttributes();

        params.width = 5000;
        params.height = 400;
       // params.gravity = Gravity.TOP;

       /* window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);*/
        window.setAttributes(params);

        etName = (EditText) findViewById(R.id.et_name);

        tvTitalview = (TextView) findViewById(R.id.tv_title_name);

        Button btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        Button btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
    }



    public void setTitale(String name){
        tvTitalview.setText(name);
    }

    public void  setNameTV(TextView tv){
        tvName = tv;
    }

    public void getSetName(){

        String getName = etName.getText().toString().trim();
        if (!TextUtils.isEmpty(getName)) {
            tvName.setText(getName);
            dismiss();
        }

    }

    @Override
    public void onClick(View v) {
          switch (v.getId()){
              case R.id.btn_ok:
                  getSetName();
                  break;
              case R.id.btn_cancel:
                  dismiss();
                  break;
          }
    }
}
