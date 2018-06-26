package com.shang.cannan.car.maybreak;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.lansent.cannan.base.AbsBaseActivity;
import com.lansent.cannan.util.SharePreUtils;
import com.shang.cannan.car.MyApp;
import com.shang.cannan.car.R;
import com.shang.cannan.car.dao.OwnerDao;
import com.shang.cannan.car.login.LoginActivity;
import com.shang.cannan.car.query.BreakQueryActivity;
import com.shang.cannan.car.util.CustomProgress;
import com.shang.cannan.car.vo.OwnerVo;

import java.util.List;

public class MayBreakActivity extends AbsBaseActivity implements View.OnClickListener, BreakPresent.BreakView {

	private ListView lvPerson;
	private TextView tvSiteName, tvTimepart, tvDate;
	private Button btBreak;//预约
	private CheckBox checkBox,checkboxAll;

	private List<OwnerVo> list;


	/**
	 * data
	 */
	private String siteNo;
	private boolean isAm;
	private BreakAdapter adapter;
	private BreakPresent present;
	private OwnerVo curItem;
	private String phone;
	private String sms;

	@Override
	public int getLayout() {
		return R.layout.activity_may_break;
	}

	@Override
	public void initViews() {
		lvPerson = getView(R.id.lvPerson);
		tvDate = getView(R.id.tvDate);
		tvTimepart = getView(R.id.tvTimePart);
		tvSiteName = getView(R.id.tvSiteName);
		btBreak = getView(R.id.btBreak);
		btBreak.setOnClickListener(this);

		TextView tvtitle = getView(R.id.topTitleTv);
		tvtitle.setText("预约");
		checkBox = getView(R.id.checkbox);
		checkboxAll = getView(R.id.checkboxAll);
		View viewBreak = getView(R.id.topLeftIv);
		viewBreak.setVisibility(View.VISIBLE);
		viewBreak.setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			return;
		}
		isAm = bundle.getBoolean("isAm");
		siteNo = bundle.getString("siteNo");
		tvSiteName.setText(bundle.getString("siteName"));
		tvDate.setText(bundle.getString("date"));
		tvTimepart.setText(isAm ? "上午" : "下午");

		adapter = new BreakAdapter(this);
		List<OwnerVo> personList = OwnerDao.getInstance(MyApp.helper).getAll("0");
		adapter.setDaata(personList);
		lvPerson.setAdapter(adapter);
		present = new BreakPresent(this, this);
		 phone = SharePreUtils.getStrConfig(this, "phone");
		 sms = SharePreUtils.getStrConfig(this, "sms");
		if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(sms)) {
			showToast("请登录");
			finish();
			return;
		}

		checkboxAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharePreUtils.saveBoolConfig(MayBreakActivity.this,"all_check",isChecked);
				adapter.setCheckAll(isChecked);
			}
		});
		checkboxAll.setChecked(SharePreUtils.getBoolConfig(this,"all_check"));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btBreak:
				list = adapter.getCheckList();
				if (list == null || list.size() == 0) {
					showToast("请选择客户");
					return;
				}
				present.smsLogin(phone, sms);
				break;
			case R.id.topLeftIv:
				finish();
				break;
		}
	}
	
	@Override
	protected void onDestroy() {
		if(present!=null){
			present.onDetch();
		}
		if(customProgress!=null && customProgress.isShowing()){
			customProgress.dismiss();
		}
		super.onDestroy();
	}

	CustomProgress customProgress;
	@Override
	public void showDialog(boolean show,String title) {
		if (show) {
			customProgress = CustomProgress.show(this, title, false, null);
		} else if (customProgress != null) {
			customProgress.dismiss();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==88){
			if(list!=null && list.size()>0){ //当遇到填报失败时继续轮询
				present.smsLogin(phone, sms);
			}
		}
	}

	@Override
	public void submitUser(boolean success, String msg) {   //预约
		if(checkBox.isChecked()){      //显示预约结果
			if(success) {
				showToast("预约成功:"+curItem.getOwnerName());
				curItem.setOkStatus(1);
				adapter.updateData(curItem);
				OwnerDao.getInstance(MyApp.helper).update(curItem);
			}

			Bundle bundle = new Bundle();
			bundle.putString("html",msg);
			bundle.putInt("type",2);
			lunchActivityForResult(BreakQueryActivity.class,88,bundle);
		}else{
			if (success) {
				showToast("预约成功:"+curItem.getOwnerName());
				curItem.setOkStatus(1);
				adapter.updateData(curItem);
				OwnerDao.getInstance(MyApp.helper).update(curItem);
			} else {
				showToast(curItem.getOwnerName()+" 预约失败\n，"+msg);
			}

			if(list!=null && list.size()>0){ //如果有 继续轮询
				present.smsLogin(phone, sms);
			}
		}
	}

	@Override
	public void login(boolean success, String msg) {
		if (!success) {
			showToast(msg);
			lunchActivity(LoginActivity.class, null, true);
		}else{//登录成功  检查集合
			if(null!=list && list.size()>0) {
				showToast(list.size()+" 个客户准备处理");
				curItem = list.remove(0);
				if (curItem != null) {
					present.subInfo(curItem , siteNo, 	tvDate.getText().toString() , isAm ? "1" : "2");
				}
			}else{
				showToast("处理完成");
			}
		}
	}

	@Override
	public void initUser(boolean success, String msg) {
		if (success) {
			curItem.setUpdateStatus(1);
			OwnerDao.getInstance(MyApp.helper).update(curItem);
		} else {
			curItem.setUpdateStatus(0);
			OwnerDao.getInstance(MyApp.helper).update(curItem);
			showToast(msg);

			if(list!=null && list.size()>0){ //当遇到填报失败时继续轮询
				present.smsLogin(phone, sms);
			}
		}
		curItem.setChecked(false);
		adapter.notifyDataSetChanged();
	}
}
