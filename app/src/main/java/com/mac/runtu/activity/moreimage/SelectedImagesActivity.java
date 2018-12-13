package com.mac.runtu.activity.moreimage;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.adapter.SelectedImagesAdapter;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.PhotoUpImageItem;
import com.mac.runtu.utils.SPUtils;
import com.mac.runtu.utils.ToastUtils;

import java.util.ArrayList;

import okhttp3.MediaType;

/**
 * 相册选择以后的页面
 *
 * @author Administrator
 */
public class SelectedImagesActivity extends AppCompatActivity implements
        OnClickListener {

    //参数类型
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse
            ("image/png");

    private ListView listView;
    private TextView back, ok;
    private ArrayList<PhotoUpImageItem> arrayList;
    private SelectedImagesAdapter       adapter;
    private ImageView                   ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.selected_images_grid);
        init();
        setclickListener();
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

    @SuppressWarnings("unchecked")
    private void init() {
        ivBack = (ImageView) findViewById(R.id.back_Iv);
        listView = (ListView) findViewById(R.id.selected_images_gridv);

        ok = (TextView) findViewById(R.id.sure);
        arrayList = (ArrayList<PhotoUpImageItem>) getIntent()
                .getSerializableExtra("selectIma");
        adapter = new SelectedImagesAdapter(SelectedImagesActivity.this,
                arrayList);
        listView.setAdapter(adapter);
    }

    private void setclickListener() {
        ivBack.setOnClickListener(this);
        ok.setOnClickListener(this);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //进行上传

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.sure:

                //上传进度显示
                ArrayList<String> pictureName = new ArrayList<>();
                ArrayList<String> pictureConentPath = new ArrayList<>();


                for (int i = 0; i < arrayList.size(); i++) {

                    //PhotoUpImageItem imageBean : arrayList)
                    PhotoUpImageItem photoUpImageItem = arrayList.get(i);
                    if (photoUpImageItem != null) {
                        String imagePath = photoUpImageItem.getImagePath();

                        if (!TextUtils.isEmpty(imagePath)) {

                            if (i == 0) {
                                //让用户看到一张图片
                                MarkerConstants.value_more_image_url =
                                        imagePath;
                            }


                            //添加图片主题
                            pictureConentPath.add(imagePath);

                            String imageFormat = imagePath.substring
                                    (imagePath.lastIndexOf("."));
                            String imageName = System.currentTimeMillis() + i +
                                    SPUtils.getString(this, GlobalConstants
                                            .SP_USERNUM, "") +
                                    imageFormat;

                            //天剑图片名称
                            pictureName.add(imageName);
                        }

                    }

                }

                if (pictureName.size() <= 5) {

                    MarkerConstants.value_more_image_name = pictureName;
                    MarkerConstants.value_more_image_content_path =
                            pictureConentPath;

                    setResult(RESULT_OK,new Intent());


                    finish();

                    MarkerConstants.isBussFarm = false;
                } else {
                    ToastUtils.makeTextShow(SelectedImagesActivity.this,
                            "选择图片大于5张");
                }

                break;
        }
    }

}
