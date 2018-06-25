package com.shang.cannan.car.maybreak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lansent.cannan.util.Log;
import com.shang.cannan.car.R;
import com.shang.cannan.car.vo.OwnerVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/12     14:44
 * Project          : Car
 * PackageName :  com.shang.cannan.car.maybreak;
 */

public class BreakAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<OwnerVo> mListData;
	private List<OwnerVo> checkList;
	private SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

	public BreakAdapter(Context context){
		inflater = LayoutInflater.from(context);
	}

	public void setDaata(List<OwnerVo> listVo){
		this.mListData = listVo;
		checkList = new ArrayList<>();
		notifyDataSetChanged();
	}

	public void updateData(OwnerVo vo){
		OwnerVo removeItem=null;
		for(OwnerVo item:mListData){
			if(item.getCardCode().equals(vo.getCardCode())){
				removeItem = item;
				break;
			}
		}
		if(removeItem!=null){
			mListData.remove(removeItem);
		}
		vo.setChecked(false);
		mListData.add(vo);
		notifyDataSetChanged();
	}

	public void setCheckAll(boolean isChecked) {
		if(isChecked){
			for(OwnerVo ownerVo:mListData){
				if(ownerVo.getOkStatus()!=1 && ownerVo.getUpdateStatus()!=1){
					ownerVo.setChecked(true);
				}
			}
		}else{
			for(OwnerVo ownerVo:mListData){
				ownerVo.setChecked(false);
			}
		}
		notifyDataSetChanged();
	}

	public List<OwnerVo> getCheckList(){
		List<OwnerVo> list= new ArrayList<>();
		for(OwnerVo vo:mListData){
			if(vo.isChecked()){
				list.add(vo);
			}
		}
		return  list;
	}

	@Override
	public int getCount() {
		return mListData==null?0:mListData.size();
	}

	@Override
	public OwnerVo getItem(int position) {
		return mListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.layout_break_person_item,null);
			holder = new Holder();
			holder.tvName =(TextView) convertView.findViewById(R.id.tvPlat) ;
			holder.tvCard =(TextView) convertView.findViewById(R.id.tvCard) ;
			holder.tvCode =(TextView) convertView.findViewById(R.id.tvCode) ;
			holder.tvTime =(TextView) convertView.findViewById(R.id.tvTime) ;
			holder.tvCardType =(TextView) convertView.findViewById(R.id.tvCardType) ;
			holder.tvStatus =(TextView) convertView.findViewById(R.id.tvStatus) ;
			holder.checkBox = convertView.findViewById(R.id.checkbox);
			holder.tvStatus2 = convertView.findViewById(R.id.tvStatus2);
			convertView.setTag(holder);
		}
		holder = (Holder) convertView.getTag() ;

		initData(holder,mListData.get(position));
		return convertView;
	}

	private void initData(Holder holder, final OwnerVo ownerVo) {
		holder.tvName.setText(ownerVo.getOwnerName());
		holder.tvCard.setText(ownerVo.getCardCode());
		holder.tvCode.setText(ownerVo.getIdentCode());

		holder.tvTime.setText(sdf.format(new Date(Long.valueOf(ownerVo.getCreateTime()))));
		if(ownerVo.getOkStatus()==1){
			holder.tvStatus.setBackgroundResource(R.color.click_on);
			holder.tvStatus.setText("已预约");
			holder.checkBox.setClickable(false);
			holder.checkBox.setVisibility(View.GONE);
		}else{
			holder.checkBox.setClickable(true);
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.tvStatus.setBackgroundResource(R.color.click_off);
			holder.tvStatus.setText("未预约");
		}

		if(ownerVo.getUpdateStatus()==1){
			holder.tvStatus2.setBackgroundResource(R.color.btnBgOn);
			holder.tvStatus2.setText("已填报");
			holder.checkBox.setVisibility(View.GONE);
		}else{
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.tvStatus2.setBackgroundResource(R.color.click_off);
			holder.tvStatus2.setText("未填报");
		}

		if(ownerVo.getCardType()==1){
			holder.tvCardType.setText("身份证");
		}else if(ownerVo.getCardType()==2){
			holder.tvCardType.setText("机构代码");
		}else if(ownerVo.getCardType()==3){
			holder.tvCardType.setText("其他类型");
		}

		holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.i("BreakAdapter","check "+isChecked);
				ownerVo.setChecked(isChecked);
				if(isChecked){
					checkList.add(ownerVo);
				}else{
					checkList.remove(ownerVo);
				}
				Log.i("BreakAdapter","check "+checkList.size());

			}
		});

		holder.checkBox.setChecked(ownerVo.isChecked());

	}



	private class Holder{
		private TextView tvName,tvCard,tvCode,tvTime,tvStatus,tvCardType,tvStatus2;
		private CheckBox checkBox;
	}
}
