package com.shang.cannan.car.person;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lansent.cannan.base.AbsBaseActivity;
import com.lansent.cannan.util.Log;
import com.lansent.cannan.util.SharePreUtils;
import com.shang.cannan.car.MyApp;
import com.shang.cannan.car.R;
import com.shang.cannan.car.dao.OwnerDao;
import com.shang.cannan.car.util.CustomProgress;
import com.shang.cannan.car.util.ParsHtmlUtils;
import com.shang.cannan.car.vo.OwnerVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PersonDetailActivity extends AbsBaseActivity implements View.OnClickListener, PersonView {
	OwnerVo vo;
	private TextView tvUsername, tvCode, tvCard, tvPersonType, tvOpType, tvCarType, tvTime;
	private PersonPresent present;
	private String token;

	Button btQuery;
	private ImageView ivCode;
	private EditText etCode;
	private RadioGroup rgSub, rgBreak;

	private ListView lvResult;

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

		lvResult = getView(R.id.lvReview);
		ivCode = getView(R.id.ivCode);
		etCode = getView(R.id.etCode);
		rgSub = getView(R.id.rgSub);
		rgBreak = getView(R.id.rgBreak);

		tvUsername = getView(R.id.tvUsername);
		View topLeft = getView(R.id.topLeftIv);
		topLeft.setVisibility(View.VISIBLE);
		topLeft.setOnClickListener(this);
		TextView tv = getView(R.id.topTitleTv);
		tv.setText("填报/预约");

		btQuery = getView(R.id.btQuery);
		initListener(this, btQuery, ivCode);
		tvUsername.setText(vo.getOwnerName());
		tvCode = getView(R.id.tvCode);
		tvCard = getView(R.id.tvCard);
		tvPersonType = getView(R.id.tvPersonType);
		tvOpType = getView(R.id.tvOpType);
		tvCarType = getView(R.id.tvCarType);
		tvTime = getView(R.id.tvTime);
		present = new PersonPresent(this, this, token);

		rgSub.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int sub = checkedId == R.id.rbSubOn ? 1 : 0;
				vo.setUpdateStatus(sub);
				OwnerDao.getInstance(MyApp.helper).update(vo);
			}
		});

		rgBreak.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				vo.setOkStatus(checkedId == R.id.rbBreadOn ? 1 : 0);
				OwnerDao.getInstance(MyApp.helper).update(vo);
			}
		});

		rgSub.check(vo.getUpdateStatus() == 1 ? R.id.rbSubOn : R.id.rbSubOff);
		rgBreak.check(vo.getOkStatus() == 1 ? R.id.rbBreadOn : R.id.rbBreakOff);
		setData(vo);
	}

	private void setData(OwnerVo vo) {
		//tvCode,tvCard,tvPersonType,tvOpType,tvCarType,tvUpdateStatus,tvOkStatus,tvTime;
		tvCode.setText(vo.getIdentCode());
		tvCard.setText(vo.getCardCode());
		tvPersonType.setText(vo.getOwnerType() == 1 ? "个人" : "单位");
		tvOpType.setText(MyApp.CarServiceNo[vo.getCarServiceNo()]);
		tvCarType.setText(MyApp.NumberType[vo.getNumberType()]);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

		tvTime.setText(sdf.format(new Date(Long.valueOf(vo.getCreateTime()))));
		present.loadCode();
	}

	@Override
	protected void onResume() {
		super.onResume();
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
				String code = etCode.getText().toString();
				if (TextUtils.isEmpty(code)) {
					showToast("宝宝，你的验证码呢？");
					return;
				}
				present.query(vo, code);
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
	public void loadCode(boolean success, byte[] bytes) {
		Log.i(TAG, "获取验证码成功");
		final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		ivCode.setImageBitmap(bitmap);
	}


	@Override
	public void query(boolean rs, final String msg) {
		String error = ParsHtmlUtils.getErrorInfo(msg);
		Log.i(TAG, error);
		if (!TextUtils.isEmpty(error)) {
			showToast(error);
		} else {
			if(SharePreUtils.getBoolConfig(PersonDetailActivity.this,"loginType")) {
				ArrayList<String> listResult = ParsHtmlUtils.getInfo(msg);
				ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listResult);
				lvResult.setAdapter(arrayAdapter);
			}else{
//				Bundle bundle = new Bundle();
//				bundle.putString("urlHtml",msg);
//				lunchActivity(BreakQueryActivity.class,bundle,false);



				ArrayList<String> listResult = ParsHtmlUtils.getWebInfo(msg);
				ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listResult);
				lvResult.setAdapter(arrayAdapter);
			}
		}
	}
}
