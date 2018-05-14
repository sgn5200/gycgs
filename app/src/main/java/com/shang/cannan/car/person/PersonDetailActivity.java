package com.shang.cannan.car.person;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lansent.cannan.base.AbsBaseActivity;
import com.lansent.cannan.util.Log;
import com.lansent.cannan.util.SharePreUtils;
import com.shang.cannan.car.MyApp;
import com.shang.cannan.car.R;
import com.shang.cannan.car.query.BreakQueryActivity;
import com.shang.cannan.car.util.CustomProgress;
import com.shang.cannan.car.vo.OwnerVo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PersonDetailActivity extends AbsBaseActivity implements View.OnClickListener, PersonView {
	OwnerVo vo;
	private TextView tvUsername, tvCode, tvCard, tvPersonType, tvOpType, tvCarType, tvUpdateStatus, tvOkStatus, tvTime;
	private Button btRevertUpdate, btUpdate;
	private PersonPresent present;
	private String token;

	Button btQuery;
	private ImageView ivCode;
	private EditText etCode;

	@Override
	public int getLayout() {
		return R.layout.activity_person_detail;
	}

	@Override
	public void initViews() {
		vo = getIntent().getExtras().getParcelable("item");
		token = SharePreUtils.getStrConfig(this, "myToken");
		Log.i(TAG, "token     " + token);

		if (vo == null) {
			showToast("没有数据啊");
			return;
		}

		ivCode = getView(R.id.ivCode);
		etCode = getView(R.id.etCode);

		tvUsername = getView(R.id.tvUsername);
		View topLeft = getView(R.id.topLeftIv);
		topLeft.setVisibility(View.VISIBLE);
		topLeft.setOnClickListener(this);
		TextView tv = getView(R.id.topTitleTv);
		tv.setText("填报/预约");

		btRevertUpdate = getView(R.id.btRevertUpdate);
		btUpdate = getView(R.id.btUpdate);
		btUpdate.setVisibility(View.GONE);
		btRevertUpdate.setVisibility(View.GONE);
		btQuery = getView(R.id.btQuery);
		initListener(this, btQuery, ivCode);
		tvUsername.setText(vo.getOwnerName());
		tvCode = getView(R.id.tvCode);
		tvCard = getView(R.id.tvCard);
		tvPersonType = getView(R.id.tvPersonType);
		tvOpType = getView(R.id.tvOpType);
		tvCarType = getView(R.id.tvCarType);
		tvUpdateStatus = getView(R.id.tvUpdateStatus);
		tvOkStatus = getView(R.id.tvOkStatus);
		tvTime = getView(R.id.tvTime);
		present = new PersonPresent(this, this, token);

		setData(vo);
	}

	private void setData(OwnerVo vo) {
		//tvCode,tvCard,tvPersonType,tvOpType,tvCarType,tvUpdateStatus,tvOkStatus,tvTime;
		tvCode.setText(vo.getIdentCode());
		tvCard.setText(vo.getCardCode());
		tvPersonType.setText(vo.getOwnerType() == 1 ? "个人" : "单位");
		tvOpType.setText(MyApp.CarServiceNo[vo.getCarServiceNo()]);
		tvCarType.setText(MyApp.NumberType[vo.getNumberType()]);
		tvUpdateStatus.setText(vo.getUpdateStatus() == 1 ? "已填报" : "未填报");
		tvOkStatus.setText(vo.getOkStatus() == 1 ? "已预约" : "未预约");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

		tvTime.setText(sdf.format(new Date(Long.valueOf(vo.getCreateTime()))));
		present.loadCode();
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.topLeftIv:
				finish();
				break;
			case R.id.ivCode:
				present.loadCode();
				break;
			case R.id.btQuery:
				String code =etCode.getText().toString();
				if(TextUtils.isEmpty(code)){
					showToast("宝宝，你的验证码呢？");
					return;
				}
				present.query(vo,code);  //http://gycgs.gzbxd.com/CarAPP/CarRecord/Query
				break;
		}
	}


	CustomProgress customProgress;
	@Override
	public void showDialog(boolean show) {
		if (show) {
			customProgress = CustomProgress.show(this, "加载中...", false, null);
		} else if (customProgress != null) {
			customProgress.dismiss();
		}
	}

	@Override
	public void loadCode(boolean success,byte[] bytes)  {
		if (success) {
			Glide.with(PersonDetailActivity.this)
					.load(bytes)
					.diskCacheStrategy(DiskCacheStrategy.NONE)
					.skipMemoryCache(true)
					.into(ivCode);
		} else {
			showToast("验证码获取失败");
		}
	}


	@Override
	public void query(boolean rs,final String  msg) {
		Bundle bundle = new Bundle();
		bundle.putString("urlHtml",msg);
		lunchActivity(BreakQueryActivity.class,bundle,false);
	}
}
