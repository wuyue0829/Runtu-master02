package com.mac.runtu.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.javabean.PhotoUpImageBucket;
import com.mac.runtu.utils.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 相册的adapter
 *
 * @author Administrator
 */
public class AlbumsAdapter extends BaseAdapter {

    //
    private List<PhotoUpImageBucket> arrayList;
    //加载布局
    private LayoutInflater           layoutInflater;
    private Context                  context;

    private String TAG = AlbumsAdapter.class.getSimpleName();

    public AlbumsAdapter(Context context) {
        //LayoutInflater 是一个抽象类  用来载入布局
        layoutInflater = LayoutInflater.from(context);

        arrayList = new ArrayList<PhotoUpImageBucket>();//初始化集合
        this.context = context;


    }

    ;

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
            holder = new Holder();
            convertView = layoutInflater.inflate(R.layout
                    .ablums_adapter_item, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.count.setText("" + arrayList.get(position).getCount());
        holder.name.setText(arrayList.get(position).getBucketName());

        String path = "file://" + arrayList.get(position).getImageList().get
				(0).getImagePath();
        //展示图片
        ImageLoadUtils.rectangleImageNoCache((Activity) context, path, holder.image);
        return convertView;
    }

    class Holder {
        ImageView image;
        TextView  name;
        TextView  count;
    }

    public void setArrayList(List<PhotoUpImageBucket> arrayList) {
        this.arrayList = arrayList;
    }
}
