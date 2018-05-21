package com.shang.cannan.car.login;

import android.os.Handler;
import android.text.TextUtils;

import com.lansent.cannan.base.AbsBaseActivity;
import com.lansent.cannan.util.SharePreUtils;
import com.shang.cannan.car.MainActivity;
import com.shang.cannan.car.MyApp;
import com.shang.cannan.car.R;
import com.shang.cannan.car.util.CustomProgress;

public class LoadingActivity extends AbsBaseActivity implements LoginPresent.LoginView {

	LoginPresent present;

	@Override
	public int getLayout() {
		return R.layout.activity_loading;
	}

	@Override
	public void initViews() {
		boolean isWx=SharePreUtils.getBoolConfig(this,"loginType");
		MyApp.initHttp(isWx);
		if (login()) return;
		new Handler().postDelayed(new Runnable() {
			public void run() {
				lunchActivity(LoginActivity.class, null, true);
			}
		}, 1500);
	}

	private boolean login() {
		String phone = SharePreUtils.getStrConfig(this, "phone");
		String sms = SharePreUtils.getStrConfig(this, "sms");
		if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(sms)) {
			present = new LoginPresent(this, this);
			present.smsLogin(phone, sms);
			return true;
		}
		return false;
	}

	CustomProgress customProgress;

	@Override
	public void showDialog(boolean isShow) {
		if (isShow) {
			customProgress = CustomProgress.show(this, "加载中...", false, null);
		} else if (customProgress != null) {
			customProgress.dismiss();
		}
	}

	@Override
	public void smsLoginResult(boolean isOk, String msg) {
		if (isOk) {
			lunchActivity(MainActivity.class, null, true);
		} else {
			showToast(msg);
			lunchActivity(LoginActivity.class, null, true);
		}
	}

	@Override
	public void smsRequestResult(boolean isOk, String msg) {

	}
}
