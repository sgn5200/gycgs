package com.shang.cannan.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shang.cannan.car.vo.SiteVo;

import java.util.List;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/8     18:35
 * Project          : Car
 * PackageName :  com.shang.cannan.car;
 */

public class MySpAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater inflater;

	private List<SiteVo >  mList;
	public MySpAdapter(Context context){
		mContext =context;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<SiteVo> list){
		mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList==null?0:mList.size();
	}

	@Override
	public SiteVo getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tvItem;
		if(convertView == null){
		 	 convertView = inflater.inflate(android.R.layout.simple_list_item_1,null);
		 	 tvItem = convertView.findViewById(android.R.id.text1);
		 	 convertView.setTag(tvItem);
		 }

		 tvItem = (TextView) convertView.getTag();

		 tvItem.setText(mList.get(position).getBespeakSiteName());
		return convertView;
	}
}
