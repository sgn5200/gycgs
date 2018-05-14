package com.shang.cannan.car.login;

import android.content.Context;
import android.text.TextUtils;

import com.lansent.cannan.api.ApiManager;
import com.lansent.cannan.api.Method;
import com.lansent.cannan.api.RxHttpClient;
import com.lansent.cannan.api.URLParam;
import com.lansent.cannan.util.Log;
import com.lansent.cannan.util.SharePreUtils;
import com.lansent.cannan.util.Utils;
import com.shang.cannan.car.util.ParsHtmlUtils;
import com.shang.cannan.car.util.UrlConstant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/11     14:10
 * Project          : Car
 * PackageName :  com.shang.cannan.car.person;
 */

public class LoginPresent {
	private String TAG = getClass().getSimpleName();

	private LoginView loginView;
	private Context context;

	private List<Disposable> disList = new ArrayList<>();

	public LoginPresent(LoginView loginView, Context context) {
		this.loginView = loginView;
		this.context = context;
	}


	public void smsLogin(final String phone, final String sms) {
		loginView.showDialog(true);
		String sessionId = SharePreUtils.getStrConfig(Utils.getApp(), "SessionId");
		final RxHttpClient.Builder config = ApiManager.getConfig();
		config.setCookiedId(sessionId);
		Log.i(TAG, "----------------2--------------");
		URLParam param = new URLParam(UrlConstant.URL_SMS_VERIFY);
		param.setMethod(Method.POST);
		param.addParam("MobileTel", phone);
		param.addParam("SmsSecurityCode", sms);
		Disposable dis = ApiManager.getInstance().requestDefault(param, context, true)
				.subscribe(new Consumer<ResponseBody>() {
					@Override
					public void accept(ResponseBody responseBody) throws Exception {
						String errorInfo = parseHtml(responseBody.string());
						if (TextUtils.isEmpty(errorInfo)) {
							loginView.smsLoginResult(true,"成功");
						}else{
							loginView.smsLoginResult(false,errorInfo);
						}
						loginView.showDialog(false);
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						Log.e(TAG, throwable.getMessage());
						throwable.printStackTrace();
						loginView.showDialog(false);
						loginView.smsLoginResult(false,throwable.getMessage());
					}
				});
		disList.add(dis);
	}


	public void smsRequest(String phone) {
		loginView.showDialog(true);
		String sessionId = SharePreUtils.getStrConfig(Utils.getApp(), "SessionId");
		RxHttpClient.Builder config = ApiManager.getConfig();
		config.setCookiedId(sessionId);
		URLParam param = new URLParam(UrlConstant.URL_SMS_GET);
		param.setMethod(Method.POST);
		param.addParam("mobileTel", phone);
		Disposable dis = ApiManager.getInstance().requestDefault(param, context, true).subscribe(
				new Consumer<ResponseBody>() {
					@Override
					public void accept(ResponseBody responseBody) throws Exception {
						Log.i(TAG, responseBody.string());
						loginView.showDialog(false);
						String rs = responseBody.string();
						String msg = ParsHtmlUtils.getErrorInfo(rs);
						loginView.smsRequestResult(true, msg);
					}
				},
				new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						Log.e(TAG, throwable.getMessage());
						throwable.printStackTrace();
						loginView.showDialog(false);
						String error = throwable.getMessage();
						if ("connect timed out".equals(throwable.getLocalizedMessage())) {
							error = "网络请求超时";
						}
						loginView.smsRequestResult(false, error);
					}
				}
		);
		disList.add(dis);
	}


	/**
	 * 解析html
	 */
	private String parseHtml(final String html) throws Exception {
		//将html转为Document对象
		Document doc = Jsoup.parse(html);
		Elements er = doc.select("div.validation-summary-errors");
		Elements error = er.select("ul li");
		Elements inputs = doc.body().select("div form input");
		Element item = inputs.get(0);
		System.out.println(item.attr("name"));
		System.out.print(item.attr("value"));
		SharePreUtils.saveStrConfig(context, "myToken", item.attr("value"));
		if (error != null) {
			Log.i(TAG, "error ");
			if (error.size() != 0) {
				Log.i(TAG, "error msg " + error.get(0).text());
				return error.get(0).text();
			}
		}
		return null;
	}

	/**
	 * 释放回收
	 */
	public void onDetach() {
		for (Disposable disposable : disList) {
			if (disposable != null) {
				disposable.dispose();
			}
		}
		if (context != null) {
			context = null;
		}

		if (loginView != null) {
			loginView = null;
		}
	}

	static interface LoginView {
		void showDialog(boolean isShow);

		void smsLoginResult(boolean isOk,String msg);

		void smsRequestResult(boolean isOk, String msg);

	}
}
