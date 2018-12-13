package com.mac.runtu.activity.personcentre;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.leaking.slideswitch.SlideSwitch;
import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.PublishIsLoginActivity;
import com.mac.runtu.business.LoginBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置中心
 */
public class PersonalCenterSettingActivity extends PublishIsLoginActivity {
    @BindView(R.id.back_Iv)
    ImageView    backIv;
    @BindView(R.id.aboutis_Tr)
    TableRow     aboutisTr;
    @BindView(R.id.news_Ss)
    SlideSwitch  newsSs;
    @BindView(R.id.suggestion_Tr)
    TableRow     suggestionTr;
    @BindView(R.id.help_Tr)
    TableRow     helpTr;
    @BindView(R.id.version_Tr)
    TableRow     versionTr;
    @BindView(R.id.imageView)
    ImageView    imageView;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_settings);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back_Iv, R.id.aboutis_Tr, R.id.suggestion_Tr, R.id
            .help_Tr, R.id.version_Tr, R.id.imageView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.aboutis_Tr:
                //关于我们
                startActivity(new Intent(PersonalCenterSettingActivity.this,
                        AboutUsActivity.class));
                break;
            case R.id.suggestion_Tr:
                //意见反馈
                startActivity(new Intent(PersonalCenterSettingActivity.this,
                        SuggestionActivity.class));
                break;
            case R.id.help_Tr:
                //帮助
                startActivity(new Intent(PersonalCenterSettingActivity.this,
                        HelpActivity.class));
                break;
            case R.id.version_Tr:
                //版本说明
                startActivity(new Intent(PersonalCenterSettingActivity.this,
                        VersionActivity.class));
                break;
            case R.id.imageView:
                //退出

                LoginBiz.getInstance().clearUserlocationData();



                //告诉app他是点击下线
                SPUtils.setBoolean(PersonalCenterSettingActivity.this,
                        GlobalConstants.sp_tuichu_deng_lu,true);

                startActivity(new Intent(this, LoginActivity.class));

                finish();

                break;
        }
    }


}
