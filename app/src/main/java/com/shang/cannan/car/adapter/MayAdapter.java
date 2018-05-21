package com.shang.cannan.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shang.cannan.car.R;
import com.shang.cannan.car.vo.MayInfoVo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/9     11:00
 * Project          : Car
 * PackageName :  com.shang.cannan.car.adapter;
 */

public class MayAdapter extends BaseAdapter {

	private List<MayInfoVo> listData;
	private Context context;
	private LayoutInflater inflater;
	private MayBreakListener listener;

	public MayAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void setMayClickListener(MayBreakListener listener) {
		this.listener = listener;
	}

	public void setData(List<MayInfoVo> list) {


		listData = list;
		if (listData == null || listData.size() == 0) {
			return;
		}
		Collections.sort(listData, new Comparator<MayInfoVo>() {
			@Override
			public int compare(MayInfoVo o1, MayInfoVo o2) {
				return o2.getSerialNumber() - o1.getSerialNumber();
			}
		});

		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		return listData == null ? 0 : listData.size();
	}

	@Override
	public MayInfoVo getItem(int position) {
		return listData==null?null:listData.get(position);
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
			convertView = inflater.inflate(R.layout.layout_may_item, null);
			holder.tab1 = (TextView) convertView.findViewById(R.id.tvTab1);
			holder.tab2 = (TextView) convertView.findViewById(R.id.tvTab2);
			holder.tab3 = (TextView) convertView.findViewById(R.id.tvTab3);
			convertView.setTag(holder);
		}

		holder = (Holder) convertView.getTag();
		initData(listData.get(position), holder);
		return convertView;
	}

	private void initData(MayInfoVo mayInfoVo, Holder holder) {
		holder.tab1.setText(mayInfoVo.getBespeakDateHtml());

		setMayBreak(holder, mayInfoVo);


		holder.tab2.setText(mayInfoVo.getAmMayBespeakNumber());

		holder.tab3.setText(mayInfoVo.getPmMayBespeakNumber());
	}

	private void setMayBreak(Holder holder, final MayInfoVo mayInfoVo) {
		String am = mayInfoVo.getAmMayBespeakNumber();
		String pm = mayInfoVo.getPmMayBespeakNumber();

		if ("已满".equals(am) || "不可预约".equals(am)) {
			holder.tab2.setClickable(false);
			holder.tab2.setBackgroundResource(R.color.click_off);
		} else {
			if (listener != null) {
				holder.tab2.setClickable(true);
				holder.tab2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						listener.mayBreak(mayInfoVo, true);
					}
				});
			}
			holder.tab2.setBackgroundResource(R.color.click_on);
		}

		if ("已满".equals(pm) || "不可预约".equals(pm)) {
			holder.tab3.setBackgroundResource(R.color.click_off);
			holder.tab3.setClickable(false);
		} else {
			if (listener != null) {
				holder.tab3.setClickable(true);
				holder.tab3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						listener.mayBreak(mayInfoVo, false);
					}
				});
			}
			holder.tab3.setBackgroundResource(R.color.click_on);
		}
	}


	private class Holder {
		private TextView tab1, tab2, tab3;
	}

	public static interface MayBreakListener {
		void mayBreak(MayInfoVo vo, boolean am);
	}

}
