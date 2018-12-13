package com.mac.runtu.activity.specialtyshop;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.utils.ImageLoadUtils;

import java.util.ArrayList;

/**
 * 商品图片详情
 */
public class SpecialtyDetailImagesActivity extends AppCompatActivity {

    private ArrayList<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialty_detail_images);

        LinearLayout llImages = (LinearLayout) findViewById(R.id.ll_images);

        images = (ArrayList<String>) getIntent()
                .getSerializableExtra
                        (GlobalConstants.key_info);


        findViewById(R.id.back_Iv).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                finish();
            }
        });



        for (int i = 0; i < images.size(); i++) {

            final int positon = i;
            ImageView view = new ImageView(this);

            view.setBackgroundResource(R.drawable.bg_icon_transparent);
            view.setScaleType(ImageView.ScaleType.FIT_XY);

            ImageLoadUtils.rectangleImage(this, images.get(i).trim(), view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SpecialtyDetailImagesActivity.this,
                            SpecialtyOneImageDetailActivity.class);
                    intent.putExtra(GlobalConstants.key_info, images);
                    intent.putExtra(GlobalConstants.key_position, positon);
                    startActivity(intent);
                }
            });

            llImages.addView(view);
        }
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
}
