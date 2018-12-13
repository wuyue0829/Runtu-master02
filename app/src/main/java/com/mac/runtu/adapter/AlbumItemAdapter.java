package com.mac.runtu.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.mac.runtu.R;
import com.mac.runtu.javabean.PhotoUpImageItem;
import com.mac.runtu.utils.ImageLoadUtils;

import java.util.List;


/**
 * 相册条目的adapter
 * @author Administrator
 *
 */
public class AlbumItemAdapter extends BaseAdapter {

	private List<PhotoUpImageItem> list;

	private LayoutInflater layoutInflater;
	private Context                  context;

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
			convertView = layoutInflater.inflate(R.layout.album_item_images_item_view, parent, false);
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
	
	class Holder{
		ImageView imageView;
		CheckBox checkBox;
	}
}
