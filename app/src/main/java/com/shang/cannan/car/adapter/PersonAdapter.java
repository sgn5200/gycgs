package com.shang.cannan.car.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.shang.cannan.car.MyApp;
import com.shang.cannan.car.R;
import com.shang.cannan.car.dao.OwnerDao;
import com.shang.cannan.car.person.PersonDetailActivity;
import com.shang.cannan.car.vo.OwnerVo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/9     16:24
 * Project          : Car
 * PackageName :  com.shang.cannan.car.adapter;
 */

public class PersonAdapter extends BaseAdapter {
	private final ClipboardManager mClipboardManager;
	private LayoutInflater inflater;
	private List<OwnerVo> mListData;
	private SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	private Context context;

	public PersonAdapter(Context context){
		inflater = LayoutInflater.from(context);
		this.context =context;
		mClipboardManager =(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);

	}

	public void setDaata(List<OwnerVo> listVo){
		this.mListData = listVo;
		notifyDataSetChanged();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.layout_person_item,null);
			holder = new Holder();
			holder.tvName =(TextView) convertView.findViewById(R.id.tvPlat) ;
			holder.tvCard =(TextView) convertView.findViewById(R.id.tvCard) ;
			holder.tvCode =(TextView) convertView.findViewById(R.id.tvCode) ;
			holder.tvTime =(TextView) convertView.findViewById(R.id.tvTime) ;
			holder.tvCardType =(TextView) convertView.findViewById(R.id.tvCardType) ;
			holder.tvUpdateStatus =(TextView) convertView.findViewById(R.id.tvStatus) ;
			holder.tvOkStatus =(TextView) convertView.findViewById(R.id.tvok) ;
			holder.tvPosition =(TextView) convertView.findViewById(R.id.tvPosition) ;
			holder.tvCopy =(TextView) convertView.findViewById(R.id.tv_top) ;
			holder.tvDelete =(TextView) convertView.findViewById(R.id.tv_delete) ;
			holder.itemView = convertView.findViewById(R.id.itemView) ;
			convertView.setTag(holder);
		}
		holder = (Holder) convertView.getTag() ;
		holder.tvPosition.setText(String.valueOf(position+1));
		initData(holder,mListData.get(position));

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				OwnerVo item = mListData.get(position);
				bundle.putParcelable("item",item);
				Intent intent = new Intent(context,PersonDetailActivity.class);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	private void initData(Holder holder, final OwnerVo ownerVo) {
		holder.tvName.setText(ownerVo.getOwnerName());
		holder.tvCard.setText(ownerVo.getCardCode());
		holder.tvCode.setText(ownerVo.getIdentCode());


		holder.tvCopy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String mData = ownerVo.getOwnerName()+"\n"+ownerVo.getIdentCode()+"\n"+ownerVo.getCardCode();
				ClipData mClipData =ClipData.newPlainText("user",mData);
				mClipboardManager.setPrimaryClip(mClipData);
				Toast.makeText(context,"已复制:\n"+mData,Toast.LENGTH_SHORT).show();
			}
		});

		holder.tvDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				OwnerDao.getInstance(MyApp.helper).delete(ownerVo);
				mListData.remove(ownerVo);
				notifyDataSetChanged();
			}
		});

		holder.tvTime.setText(sdf.format(new Date(Long.valueOf(ownerVo.getCreateTime()))));
		holder.tvUpdateStatus.setText(ownerVo.getUpdateStatus()==1?"已填报":"未填报");
		holder.tvOkStatus.setText(ownerVo.getOkStatus()==1?"已预约":"未预约");
		if(ownerVo.getOkStatus()==1){
			holder.tvOkStatus.setBackgroundColor(0xff2e9741);
		}else{
			holder.tvOkStatus.setBackgroundColor(0xffd0d0d0);
		}

		if(ownerVo.getCardType()==1){
			holder.tvCardType.setText("身份证");
		}else if(ownerVo.getCardType()==2){
			holder.tvCardType.setText("机构代码");
		}else if(ownerVo.getCardType()==3){
			holder.tvCardType.setText("其他类型");
		}

	}

	private class Holder{
		private View itemView;
		private TextView tvName,tvCard,tvCode,tvTime,tvUpdateStatus,tvOkStatus,tvCardType,tvPosition,tvCopy,tvDelete;
	}

}
