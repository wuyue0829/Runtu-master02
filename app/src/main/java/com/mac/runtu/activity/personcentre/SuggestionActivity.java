package com.mac.runtu.activity.personcentre;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.UserSuggestionInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.PhoneNetWordUitls;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuggestionActivity extends AppCompatActivity {


    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.editText1)
    EditText  editText1;
    @BindView(R.id.et_phone_qq)
    EditText  etPhoneQq;
    @BindView(R.id.iv_btnConfirm)
    ImageView ivBtnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_settings_suggestion);
        ButterKnife.bind(this);


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


    @OnClick({R.id.back_Iv, R.id.iv_btnConfirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.iv_btnConfirm:

                String suggestInfo = editText1.getText().toString().trim();
                String phoneQQ = etPhoneQq.getText().toString().trim();
                if (TextUtils.isEmpty(suggestInfo)) {
                    ToastUtils.makeTextShow(SuggestionActivity.this,
                            "内容不能为空!");
                    return;
                }

                if (TextUtils.isEmpty(phoneQQ)) {
                    ToastUtils.makeTextShow(SuggestionActivity.this,
                            "联系方式不能为空!");
                    return;
                }

                if (phoneQQ.length() < 5 || phoneQQ.length() > 11) {
                    ToastUtils.makeTextShow(SuggestionActivity.this,
                            "联系方式格式不正确 !");
                    return;
                }

                if (!PhoneNetWordUitls.isNetworkConnected(this)) {
                    ToastUtils.makeTextShow(this,"请打开网络!");
                    return;
                }


                ivBtnConfirm.setEnabled(false);

                UserSuggestionInfo info = new
                        UserSuggestionInfo();

                info.contact = phoneQQ;
                info.content = suggestInfo;
                info.userUuid = UiUtils.getUserID();
                String infoJson = GSonUtil.obj2json(info);



                MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                        .user_suggest_upload_url, null, GlobalConstants
                        .key_info, infoJson, new
                        MyHttpUtils.OnNewWorkRequestListener2Boolean() {


                            @Override
                            public void onNewWorkSuccess(Boolean result) {
                                if (result) {
                                    ToastUtils.makeTextShow
                                            (SuggestionActivity.this,
                                                    "您的意见我们已收到!");

                                    finish();
                                } else {
                                    ToastUtils.makeTextShow
                                            (SuggestionActivity.this,
                                                    "意见提交失败!");
                                }

                                ivBtnConfirm.setEnabled(true);
                            }

                            @Override
                            public void onNewWorkError(String msg) {
                                ToastUtils.makeTextShowNoNet
                                        (SuggestionActivity.this);
                                ivBtnConfirm.setEnabled(true);
                            }
                        });

                break;
        }
    }
}
