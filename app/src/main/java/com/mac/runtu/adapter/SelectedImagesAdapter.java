package com.mac.runtu.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.mac.runtu.R;
import com.mac.runtu.javabean.PhotoUpImageItem;
import com.mac.runtu.utils.ImageLoadUtils;

import java.util.ArrayList;


/**
 * 照片呗选择的adapter
 *
 * @author Administrator
 */
public class SelectedImagesAdapter extends BaseAdapter {

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
					.selected_images_adapter_item, parent, false);
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
