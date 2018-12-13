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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.PhotoUpImageBucket;
import com.mac.runtu.javabean.PhotoUpImageItem;
import com.mac.runtu.utils.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 进入相册条目的页面
 *
 * @author Administrator
 */
public class AlbumItem2Activity extends AppCompatActivity implements View
        .OnClickListener {

    private GridView gridView;
    private TextView back, ok;
    private PhotoUpImageBucket          photoUpImageBucket;
    private ArrayList<PhotoUpImageItem> selectImages;
    private AlbumItemAdapter            adapter;
    private ImageView                   ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MarkerConstants.activity2 = this;

        setContentView(R.layout.album_item_images2);
        init();
        setListener();
    }

    private void init() {
        ivBack = (ImageView) findViewById(R.id.back_Iv);
        gridView = (GridView) findViewById(R.id.album_item_gridv);

        ok = (TextView) findViewById(R.id.sure);
        selectImages = new ArrayList<PhotoUpImageItem>();

        Intent intent = getIntent();
        photoUpImageBucket = (PhotoUpImageBucket) intent.getSerializableExtra
                ("imagelist");
        adapter = new AlbumItemAdapter(photoUpImageBucket.getImageList(),
                AlbumItem2Activity.this);
        gridView.setAdapter(adapter);
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

    private void setListener() {
        ivBack.setOnClickListener(this);
        ok.setOnClickListener(this);

        //条目点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                Intent intent = new Intent(AlbumItem2Activity.this,
                        SelectedImages2Activity.class);
                intent.putExtra("selectIma", selectImages);
                startActivityForResult(intent,0);

                break;

            default:
                break;
        }
    }

    class AlbumItemAdapter extends BaseAdapter {

        private List<PhotoUpImageItem> list;

        private LayoutInflater layoutInflater;
        private Context        context;

        public AlbumItemAdapter(List<PhotoUpImageItem> list,Context context){
            this.list = list;
            layoutInflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.album_item_images_item_view2, parent, false);
                holder = new Holder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.image_item);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.check);
                convertView.setTag(holder);
            }else {
                holder = (Holder) convertView.getTag();
            }
            //图片加载器的使用代码，就这一句代码即可实现图片的加载。请注意
            //这里的uri地址，因为我们现在实现的是获取本地图片，所以使
            //用"file://"+图片的存储地址。如果要获取网络图片，
            //这里的uri就是图片的网络地址。

            holder.checkBox.setChecked(list.get(position).isSelected());

            String path = "file://"+list.get(position).getImagePath();
            //展示图片
            ImageLoadUtils.rectangleImageNoCache((Activity) context, path, holder.imageView);
            return convertView;
        }


    }

    class Holder{
        ImageView imageView;
        CheckBox checkBox;
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
