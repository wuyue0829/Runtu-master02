package com.mac.runtu.activity.moreimage;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.mac.runtu.R;
import com.mac.runtu.adapter.AlbumsAdapter;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.PhotoUpImageBucket;
import com.mac.runtu.utils.PhotoUpAlbumHelper;

import java.util.List;


/**
 * 相册的页面
 *
 * @author Administrator
 */
public class AlbumsActivity extends AppCompatActivity {

    private GridView           gridView;
    private AlbumsAdapter      adapter;
    private PhotoUpAlbumHelper photoUpAlbumHelper;

    private List<PhotoUpImageBucket> list;
    private ImageView                ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //存储次activity
        MarkerConstants.activity = this;

        setContentView(R.layout.albums_gridview);
        init();
        loadData();
        onItemClick();
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
        gridView = (GridView) findViewById(R.id.album_gridv);
        adapter = new AlbumsAdapter(AlbumsActivity.this);
        gridView.setAdapter(adapter);
    }

    private void loadData() {
        photoUpAlbumHelper = PhotoUpAlbumHelper.getHelper();
        photoUpAlbumHelper.init(AlbumsActivity.this);
        photoUpAlbumHelper.setGetAlbumList(
                new PhotoUpAlbumHelper.GetAlbumList() {

                    @Override
                    public void getAlbumList(List<PhotoUpImageBucket> list) {
                        //将加载得到的图片集合  交给Adapter展示(此方法自己定义)
                        adapter.setArrayList(list);
                        adapter.notifyDataSetChanged();
                        AlbumsActivity.this.list = list;
                    }
                });
        photoUpAlbumHelper.execute(false);
    }

    private void onItemClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击这回退说明他没选图片
                MarkerConstants.value_more_image_url = "";
                MarkerConstants.value_more_image_url1 = "";
                MarkerConstants.isBussFarm = false;
                MarkerConstants.isFromRelName = false;
                finish();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {

                //判断跳转哪个相册
                if (MarkerConstants.isBussFarm||MarkerConstants.isFromRelName) {
                    Intent intent = new Intent(AlbumsActivity.this,
                            AlbumItem2Activity.class);
                    intent.putExtra("imagelist", list.get(position));
                    startActivityForResult(intent,0);
                } else {
                    Intent intent2 = new Intent(AlbumsActivity.this,
                            AlbumItemActivity.class);
                    intent2.putExtra("imagelist", list.get(position));
                    startActivityForResult(intent2,0);
                }


            }
        });
    }

    @Override
    protected void onDestroy() {
        MarkerConstants.isBussFarm = false;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            finish();
        }
    }
}
