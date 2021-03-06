package com.shang.cannan.car.login;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.lansent.cannan.api.ApiManager;
import com.lansent.cannan.base.AbsBaseActivity;
import com.lansent.cannan.util.Log;
import com.lansent.cannan.util.RegexUtils;
import com.lansent.cannan.util.RxEvent;
import com.lansent.cannan.util.SharePreUtils;
import com.shang.cannan.car.MainActivity;
import com.shang.cannan.car.MyApp;
import com.shang.cannan.car.R;
import com.shang.cannan.car.query.BreakQueryActivity;
import com.shang.cannan.car.reciver.SmsContent;
import com.shang.cannan.car.util.CustomProgress;
import com.shang.cannan.car.util.UrlConstant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Locale;

import io.reactivex.functions.Consumer;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_SMS;

public class LoginActivity extends AbsBaseActivity implements View.OnClickListener,LoginPresent.LoginView {

	// UI references.
	private EditText etPhone;
	private EditText etSms;
	private ProgressBar mProgressView;
	private SmsContent content;
	private Button btLogn,btPhone;

	private LoginPresent loginPresent;
	private TextView tvInfo,tvNext;
	private Switch aSwitchLoginType;
	private String phone,sms;//手机号/验证码

	@Override
	public int getLayout() {
		return R.layout.activity_login;
	}

	@Override
	public void initViews() {
		RxEvent.getInstance().register(String.class, new Consumer<String>() {
			@Override
			public void accept(String s) throws Exception {
				if(RegexUtils.isCode(s)){
					etSms.setText(s);
					login();
				}
			}
		}, new Consumer<Throwable>() {
			@Override
			public void accept(Throwable throwable) throws Exception {
				Log.e(TAG,throwable.getMessage());
			}
		});

		aSwitchLoginType = getView(R.id.swLogin);
		tvNext = getView(R.id.tvNext);
		etPhone = getView(R.id.phone);
		etSms = getView(R.id.sms_code);
		btPhone = getView(R.id.btPhone);
		btLogn = getView(R.id.btLogin);
		tvInfo = getView(R.id.tvInfo);
		tvInfo.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
		tvInfo.getPaint().setAntiAlias(true);//抗锯齿
		tvInfo.setOnClickListener(this);
		tvNext.setOnClickListener(this);
		btLogn.setEnabled(false);
		btPhone.setEnabled(false);
		btLogn.setOnClickListener(this);
		btPhone.setOnClickListener(this);
		mProgressView = getView(R.id.login_progress);
		mProgressView.setVisibility(View.GONE);
		if (getSmsPermission()) {
			content = new SmsContent(new Handler(), this);
			//注册短信变化监听
			this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);
		}

		aSwitchLoginType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				buttonView.setText(isChecked?"微信登录":"浏览器登录");
				ApiManager.clear();
				MyApp.initHttp(isChecked);
				SharePreUtils.saveBoolConfig(LoginActivity.this,"loginType",isChecked);
			}
		});

		boolean isWx = SharePreUtils.getBoolConfig(LoginActivity.this,"loginType");
		aSwitchLoginType.setText(isWx?"微信登录":"浏览器登录");
		aSwitchLoginType.setChecked(isWx);
		etPhone.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				 if(RegexUtils.isMobileSimple(s)){
				 	btPhone.setEnabled(true);
				 }else{
				 	btPhone.setEnabled(false);
				 }
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		etSms.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				 if(RegexUtils.isCode(s)){
				 	btLogn.setEnabled(true);
				 }else {
				 	btLogn.setEnabled(false);
				 }
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		etPhone.setText(SharePreUtils.getStrConfig(this,"phone"));

		loginPresent = new LoginPresent(this,this);
	}

	@Override
	public void onBackPressed() {
		if(mProgressView!=null && mProgressView.isShown()){
			return;
		}
		super.onBackPressed();

	}

	@Override
	protected void onDestroy() {
		if (content != null) {
			this.getContentResolver().unregisterContentObserver(content);
			loginPresent.onDetach();
			timer.cancel();
		}
		super.onDestroy();
	}

	/**
	 * 获取验证码权限
	 * @return
	 */
	private boolean getSmsPermission() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}
		if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
			return true;
		}
		if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
			Snackbar.make(etPhone, "请授予短信权限", Snackbar.LENGTH_INDEFINITE)
					.setAction(android.R.string.ok, new View.OnClickListener() {
						@Override
						@TargetApi(Build.VERSION_CODES.M)
						public void onClick(View v) {
							requestPermissions(new String[]{READ_CONTACTS}, 1);
						}
					});
		} else {
			requestPermissions(new String[]{READ_SMS}, 1);
		}
		return false;
	}

	/**
	 * Callback received when a permissions request has been completed.
	 * 权限请求结果
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		if (requestCode == 1) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				content = new SmsContent(new Handler(), this);
				//注册短信变化监听
				this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);
			}
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btPhone:
				 phone =etPhone.getText().toString();
				if(!RegexUtils.isMobileSimple(phone)){
					showToast("手机号格式不正确");
					return;
				}
				loginPresent.smsRequest(phone);
				break;
			case R.id.btLogin:
				login();
				break;
			case R.id.tvInfo:
				Bundle bundle = new Bundle();
				bundle.putString("url",UrlConstant.URL_Read);
				bundle.putInt("type",1);
				Log.i(TAG,UrlConstant.URL_Read);
				lunchActivity(BreakQueryActivity.class,bundle,false);
				break;
			case R.id.tvNext:
				lunchActivity(MainActivity.class,null,true);
				break;
		}
	}


	private void login() {
		phone = etPhone.getText().toString();
		sms = etSms.getText().toString();
		if(!RegexUtils.isMobileSimple(phone)){
			showToast("手机号格式不正确");
			return;
		}
		if(!RegexUtils.isCode(sms)){
			showToast("验证码格式不正确");
			return;
		}
		loginPresent.smsLogin(phone,sms);
	}

	/**
	 * 开启60秒倒计时
	 */
	 int count = 59;
	private void startTime() {
		btPhone.setClickable(false);
		count = 59;
		timer.start();
	}

	CountDownTimer timer = new CountDownTimer(60*1000,1000) {
		@Override
		public void onTick(long millisUntilFinished) {
			btPhone.setText(String.format(Locale.CHINA,"%d后重新获取短信",count));
			count--;
		}

		@Override
		public void onFinish() {
			btPhone.setClickable(true);
			btPhone.setText("获取短信");
		}
	};




	CustomProgress progress;
	@Override
	public void showDialog(boolean isShow) {
		if(isShow){
			progress = CustomProgress.show(this,"加载中...",false,null);
		}else{
			if(progress!=null){
				progress.dismiss();
			}
		}
	}

	@Override
	public void smsLoginResult(boolean isOk,String msg) {
		   if(isOk){
			   SharePreUtils.saveStrConfig(LoginActivity.this,"phone",etPhone.getText().toString());
			   SharePreUtils.saveStrConfig(LoginActivity.this,"sms",etSms.getText().toString());
			   lunchActivity(MainActivity.class,null,true);
		   }else{
		   		showToast(msg);
		   }
	}

	@Override
	public void smsRequestResult(boolean isOk,String msg) {
		    if(isOk){
				startTime();
			}else{
				btPhone.setClickable(true);
				btPhone.setText("获取短信");
		    	showToast("获取验证码请求失败");
			}
	}



	/**
	 * 解析html
	 */
	private void parseHtml(final String html) throws Exception{
		//将html转为Document对象
		final Document doc = Jsoup.parse(html);
		final Elements er = doc.select("div.validation-summary-errors");
		final Elements error = er.select("ul li");
	}

}
