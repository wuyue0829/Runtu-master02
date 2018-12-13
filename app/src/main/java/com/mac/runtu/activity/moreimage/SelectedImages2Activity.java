package com.mac.runtu.activity.moreimage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.PhotoUpImageItem;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.SPUtils;
import com.mac.runtu.utils.ToastUtils;

import java.util.ArrayList;

import okhttp3.MediaType;

/**
 * 相册选择以后的页面
 *
 * @author Administrator
 */
public class SelectedImages2Activity extends AppCompatActivity implements
        OnClickListener {

    //参数类型
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse
            ("image/png");

    private GridView gridView;
    private TextView back, ok;
    private ArrayList<PhotoUpImageItem> arrayList;
    private SelectedImagesAdapter       adapter;
    private ImageView                   ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.selected_images_grid2);
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

        gridView = (GridView) findViewById(R.id.selected_images_gridv);

        ok = (TextView) findViewById(R.id.sure);
        arrayList = (ArrayList<PhotoUpImageItem>) getIntent()
                .getSerializableExtra("selectIma");
        adapter = new SelectedImagesAdapter(SelectedImages2Activity.this,
                arrayList);
        gridView.setAdapter(adapter);
    }

    private void setclickListener() {
        ivBack.setOnClickListener(this);
        ok.setOnClickListener(this);
        gridView.setOnItemClickListener(new OnItemClickListener() {
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


                    String path = arrayList.get(i).getImagePath();

                    if (i == 0) {
                        //让用户看到一张图片
                        MarkerConstants.value_more_image_url = path;
                    }

                    if (i == 1) {
                        //让用户看到一张图片
                        MarkerConstants.value_more_image_url1 = path;
                    }
                    //添加图片主题
                    pictureConentPath.add(path);

                    String imageFormat = path.substring(path.lastIndexOf("."));
                    String imageName = System.currentTimeMillis() + i +
                            SPUtils.getString(this, GlobalConstants
                                    .SP_USERNUM, "") +
                            imageFormat;

                    //天剑图片名称
                    pictureName.add(imageName);
                }

                if (MarkerConstants.isFromRelName) {

                    if (pictureName.size() == 2) {

                        MarkerConstants.value_more_image_name = pictureName;
                        MarkerConstants.value_more_image_content_path =
                                pictureConentPath;

                        setResult(RESULT_OK,new Intent());

                        finish();

                        MarkerConstants.isFromRelName = false;
                    } else {
                        ToastUtils.makeTextShow(SelectedImages2Activity.this,
                                "身份证正反面都需要");
                    }

                    return;
                }

                if (pictureName.size() <= 5) {

                    MarkerConstants.value_more_image_name = pictureName;
                    MarkerConstants.value_more_image_content_path =
                            pictureConentPath;

                    setResult(RESULT_OK,new Intent());

                    finish();

                    MarkerConstants.isBussFarm = false;
                } else {
                    ToastUtils.makeTextShow(SelectedImages2Activity.this,
                            "选择图片大于5张");
                }

                break;
        }
    }

    class SelectedImagesAdapter extends BaseAdapter {

        private ArrayList<PhotoUpImageItem> arrayList;
        private LayoutInflater              layoutInflater;
        private Context                     context;

        public SelectedImagesAdapter(Context context, ArrayList<PhotoUpImageItem>
                arrayList) {
            this.arrayList = arrayList;
            layoutInflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout
                        .selected_images_adapter_item2, parent, false);
                holder = new Holder();
                holder.imageView = (ImageView) convertView.findViewById(R.id
                        .selected_image_item);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            String path = "file://" + arrayList.get(position).getImagePath();
            //展示图片
            ImageLoadUtils.rectangleImageNoCache((Activity) context, path, holder.imageView);
            return convertView;
        }

        class Holder {
            ImageView imageView;
        }
    }

}
