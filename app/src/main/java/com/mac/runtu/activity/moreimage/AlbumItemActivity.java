package com.mac.runtu.activity.moreimage;


import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.adapter.AlbumItemAdapter;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.PhotoUpImageBucket;
import com.mac.runtu.javabean.PhotoUpImageItem;

import java.util.ArrayList;

/**
 * 进入相册条目的页面
 *
 * @author Administrator
 */
public class AlbumItemActivity extends AppCompatActivity implements View
        .OnClickListener {

    private ListView listView;
    private TextView back, ok;
    private PhotoUpImageBucket          photoUpImageBucket;
    private ArrayList<PhotoUpImageItem> selectImages;
    private AlbumItemAdapter            adapter;
    private ImageView                   ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //存储次activity
        MarkerConstants.activity2 = this;

        setContentView(R.layout.album_item_images);

        init();
        setListener();
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

    private void init() {
        ivBack = (ImageView) findViewById(R.id.back_Iv);
        listView = (ListView) findViewById(R.id.album_item_gridv);

        ok = (TextView) findViewById(R.id.sure);
        selectImages = new ArrayList<PhotoUpImageItem>();

        Intent intent = getIntent();
        photoUpImageBucket = (PhotoUpImageBucket) intent.getSerializableExtra
                ("imagelist");
        adapter = new AlbumItemAdapter(photoUpImageBucket.getImageList(),
                AlbumItemActivity.this);
        listView.setAdapter(adapter);
    }

    private void setListener() {
        ivBack.setOnClickListener(this);
        ok.setOnClickListener(this);

        //条目点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                CheckBox checkBox = (CheckBox) view.findViewById(R.id.check);

                PhotoUpImageItem imageInfo = (PhotoUpImageItem) adapter
                        .getItem(position);
                //将对象中的值取反
                imageInfo.setSelected(!imageInfo.isSelected());
                //设置checkBox的选中状态

                checkBox.setChecked(imageInfo.isSelected());

                //	adapter.notifyDataSetChanged();
                //吐丝 显示位置
                //	Toast.makeText(AlbumItemActivity.this, "postion="+position,
                //	Toast.LENGTH_SHORT).show();

                //				photoUpImageBucket.getImageList().get
                // (position).setSelected(
                //						!photoUpImageBucket.getImageList().get
                // (position).isSelected());
                //				adapter.notifyDataSetChanged();
                if (imageInfo.isSelected()) {
                    //集合中已经拥有
                    if (selectImages.contains(imageInfo)) {


                    } else {

                        String imagePath = imageInfo.getImagePath();
                        if (imagePath.endsWith(".jpg") || imagePath.endsWith
                                (".png") || imagePath.endsWith(".jpeg") ||
                                imagePath.endsWith(".bmp") || imagePath
                                .endsWith(".RAW")) {
                            //把选中的图片 加入集合
                            selectImages.add(imageInfo);
                        }
                    }
                } else {
                    if (selectImages.contains(photoUpImageBucket.getImageList
                            ().get(position))) {
                        selectImages.remove(photoUpImageBucket.getImageList()
                                .get(position));
                    } else {

                    }
                }
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
                Intent intent = new Intent(AlbumItemActivity.this,
                        SelectedImagesActivity.class);
                intent.putExtra("selectIma", selectImages);
                startActivityForResult(intent,0);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK,new Intent());
            finish();
        }
    }

}
