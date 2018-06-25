package com.shang.cannan.car.input;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.lansent.cannan.base.AbsBaseActivity;
import com.lansent.cannan.util.Log;
import com.lansent.cannan.util.RegexUtils;
import com.shang.cannan.car.MyApp;
import com.shang.cannan.car.R;
import com.shang.cannan.car.dao.OwnerDao;
import com.shang.cannan.car.util.SystemStatusManager;
import com.shang.cannan.car.vo.OwnerVo;

public class InputActivity extends AbsBaseActivity implements View.OnClickListener {

	/**** ui****/
	private Spinner spOp, spCar, spCard;
	private EditText etCode, etUsername, etCard;    //识别代号  用户名  身份证号码
	private RadioGroup rg;
	private Button btSave;

	private ClipboardManager mClipboardManager;


	@Override
	public int getLayout() {
		return R.layout.activity_input;
	}

	@Override
	public void initViews() {
		initWindow();
		mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		etCard = getView(R.id.etCard);
		etUsername = getView(R.id.etUsername);
		spCard = getView(R.id.spCard);
		etCode = getView(R.id.etCode);
		rg = getView(R.id.rg);
		spOp = getView(R.id.spOp);
		spCar = getView(R.id.spCar);
		btSave = getView(R.id.btSave);
		btSave.setOnClickListener(this);
		TextView tv = getView(R.id.topTitleTv);
		tv.setText("信息填报");
		View view = getView(R.id.topLeftIv);
		view.setVisibility(View.VISIBLE);
		view.setOnClickListener(this);
		View right = getView(R.id.topRightIv);
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);

		spOp.setSelection(1);
		spCar.setSelection(1);
		spCard.setSelection(0);
		rg.check(R.id.rbUserSign);
		btSave.setEnabled(false);
		etCode.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (RegexUtils.isAgentCode(s)) {
					String card = etCard.getText().toString();
					if (RegexUtils.isIDCard15(card) || RegexUtils.isIDCard18(card) ||  RegexUtils.isCompanyAgentCode(card)) {
						btSave.setEnabled(true);
					} else {
						btSave.setEnabled(false);
					}
				} else {
					btSave.setEnabled(false);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		etCard.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (RegexUtils.isIDCard15(s) || RegexUtils.isIDCard18(s)) {
					String agent = etCode.getText().toString();
					if (RegexUtils.isAgentCode(agent)) {
						btSave.setEnabled(true);
					} else {
						btSave.setEnabled(false);
					}
				} else if (RegexUtils.isCompanyAgentCode(s)) {
					rg.check(R.id.rbUserAll);
					spCard.setSelection(1);
					btSave.setEnabled(true);
				} else {
					btSave.setEnabled(false);
				}
		}

		@Override
		public void afterTextChanged (Editable s){

		}
	});
		spCard.setSelection(0);
}

	private void initWindow() {//初始化窗口属性，让状态栏和导航栏透明
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
		}
		SystemStatusManager tintManager = new SystemStatusManager(this);
		tintManager.setTintColor(getResources().getColor(R.color.colorAccent));
		tintManager.setStatusBarTintEnabled(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.topLeftIv:
				finish();
				break;
			case R.id.topRightIv:
				btSave.setEnabled(true);
				showToast("自行小心检查");
				break;
			case R.id.btSave:
				OwnerVo voSave = getVo();

				Log.i(TAG, voSave.toString());

				if (voSave != null) {
					try {
						OwnerDao.getInstance(MyApp.helper).insert(voSave);
						showToast("保存成功");
						etCard.setText("");
						etCode.setText("");
						etUsername.setText("");
					} catch (Exception e) {
						showToast("保存失败");
					}
				}
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		ClipData clipData = mClipboardManager.getPrimaryClip();

		if (clipData == null)
			return;
		ClipData.Item curItem = clipData.getItemAt(0);
		if (curItem == null || curItem.getText() == null) {
			return;
		}
		String text = curItem.getText().toString();
		Log.i(TAG, text);
		String[] user = text.split("\n");
		if (user.length != 3) {
			return;
		}

		etUsername.setText(user[0]);
		etCode.setText(user[1]);
		if(user[1].startsWith("L")){
			spCar.setSelection(1);
		}else{
			spCar.setSelection(2);
		}

		etCard.setText(user[2]);
		if(RegexUtils.isCompanyAgentCode(user[2])){
			rg.check(R.id.rbUserAll);
			spCard.setSelection(1);
		}else{
			rg.check(R.id.rbUserSign);
			spCard.setSelection(0);
		}

	}

	public OwnerVo getVo() {
		OwnerVo vo = null;

		int opPosition = spOp.getSelectedItemPosition();
		int carPosition = spCar.getSelectedItemPosition();
		int cardPostion = spCard.getSelectedItemPosition() + 1;
		int OwnerType = 0;
		if (rg.getCheckedRadioButtonId() == R.id.rbUserSign) {
			OwnerType = 1;
		} else if (rg.getCheckedRadioButtonId() == R.id.rbUserAll) {
			OwnerType = 2;
		}

		if (opPosition == 0 || carPosition == 0 || OwnerType == 0) {
			showToast("业务类型/车辆类型未选择");
			return vo;
		}
		String authorCode = etCode.getText().toString();
		String cardCode = etCard.getText().toString();
		String userName = etUsername.getText().toString();
		if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(cardCode) || TextUtils.isEmpty(authorCode)) {
			showToast("名字/识别代码/证件 没有正确填写");
			return vo;
		}

		vo = new OwnerVo();
		vo.setCarServiceNo(opPosition);  //业务类型
		vo.setNumberType(carPosition);//车辆类型
		vo.setIdentCode(authorCode);    //识别代码
		vo.setCardCode(cardCode);       //证件号
		vo.setCardType(cardPostion);    //证件类型
		vo.setOwnerType(OwnerType);  //个人还是单位
		vo.setOwnerName(userName);    //用户名
		return vo;
	}
}
