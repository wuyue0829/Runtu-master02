package com.mac.runtu.business;

import android.app.Activity;
import android.content.Intent;

import com.mac.runtu.activity.personcentre.AgreementTextActivity;
import com.mac.runtu.activity.personcentre.LoginActivity;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnBooleanListener;
import com.mac.runtu.interfaceif.OnGetData2BooleanListener;
import com.mac.runtu.utils.UiUtils;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2017/1/10 0010 下午 3:19
 */
public class ReallyNameBiz {


    private Activity activity;


    public ReallyNameBiz(Activity activity) {
        this.activity = activity;

    }

    /**
     * 发布前情况判断
     */
    public void status() {


        if (!UiUtils.isLogin()) {
            Intent intent = new Intent(activity,
                    LoginActivity
                            .class);
            activity.startActivity(intent);
            return;
        }

        int atestationStuts = UiUtils.getAttestationStuts();

        switch (atestationStuts) {
            case GlobalConstants.notReaNemAttestation:

                showAttestation("您还没有进行实名认证," +
                        "\n不能发布信息");

                break;
            case GlobalConstants.reaNemAttestationSucc:


                break;
            case GlobalConstants.reaNemAttestationIng:

                showAttestationIng();
                break;
            case GlobalConstants.reaNemAttestationFail://失败

                showAttestation("您的实名认证失败\n请重新认证");

                break;

        }

    }

    /**
     * 正在实名认证中
     */
    private void showAttestationIng() {
        MyShowDialog.showDialogSelectImage("提示信息:", "身份信息正在审核中....",
                activity, new OnBooleanListener() {


                    @Override
                    public void onResultIsTrue() {

                    }
                });
    }


    /**
     * 提示实名认证
     */
    private void showAttestation(String content) {
        MyShowDialog.showDialogInfo(activity, "提示信息 :", content, "认证", new
                OnGetData2BooleanListener() {


                    @Override
                    public void onSuccess(Boolean result) {
                        //跳转认证界面
                        Intent intent = new Intent(activity,
                                AgreementTextActivity.class);
                        intent.putExtra(GlobalConstants.key_kind, 2);
                        activity.startActivity(intent);

                    }

                    @Override
                    public void onFail() {

                    }
                });
    }


    public static String getResultName(){
       String result ="";

        switch (UiUtils.getAttestationStuts()) {

            case GlobalConstants.notReaNemAttestation:
                result ="未认证";
                break;
            case GlobalConstants.reaNemAttestationSucc:

                 result = "已通过";
                break;
            case GlobalConstants.reaNemAttestationFail://失败

                result = "未通过审核";

                break;
            case GlobalConstants.reaNemAttestationIng://失败

                result = "审核中";

                break;


        }

        return result;
    }

}
