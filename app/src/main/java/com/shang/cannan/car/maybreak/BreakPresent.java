package com.shang.cannan.car.maybreak;

import android.content.Context;
import android.text.TextUtils;

import com.lansent.cannan.api.ApiManager;
import com.lansent.cannan.api.Method;
import com.lansent.cannan.api.RxHttpClient;
import com.lansent.cannan.api.URLParam;
import com.lansent.cannan.util.Log;
import com.lansent.cannan.util.SharePreUtils;
import com.lansent.cannan.util.Utils;
import com.shang.cannan.car.util.UrlConstant;
import com.shang.cannan.car.vo.OwnerVo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/12     15:32
 * Project          : Car
 * PackageName :  com.shang.cannan.car.maybreak;
 */

public class BreakPresent {
	private  Context context;
	private BreakView breakView;
	private String TAG = getClass().getSimpleName();
	private ArrayList<Disposable> listDis ;

	public BreakPresent(BreakView breakView, Context context){
		this.context =context;
		this.breakView = breakView;
		this.listDis = new ArrayList<>();
	}

	public void onDetch(){
		if(listDis!=null && listDis.size()>0){
			for(Disposable disposable:listDis){
				if(!disposable.isDisposed()){
					disposable.dispose();
				}
			}
		}
	}

	public void smsLogin(final String phone, final String sms) {
		String sessionId = SharePreUtils.getStrConfig(Utils.getApp(),"SessionId");
		final RxHttpClient.Builder config = ApiManager.getConfig();
		config.setCookiedId(sessionId);
		Log.i(TAG,"----------------id--------------"+sessionId);
		Log.i(TAG,"----------------token--------------"+SharePreUtils.getStrConfig(context,"myToken"));
		final URLParam param = new URLParam(UrlConstant.URL_SMS_VERIFY);
		param.setMethod(Method.POST);
		param.addParam("MobileTel",phone);
		param.addParam("SmsSecurityCode",sms);
		Disposable dis = ApiManager.getInstance().requestDefault(param,context,true)
				.subscribe(new Consumer<ResponseBody>() {
					@Override
					public void accept(ResponseBody responseBody) throws Exception {
						String error=parseHtml(responseBody.string());
						if(TextUtils.isEmpty(error)){
							breakView.login(true,"成功");
						}else {
							breakView.login(false,error);
							Log.i(TAG,error);
						}
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						Log.e(TAG,throwable.getMessage());
						throwable.printStackTrace();
					}
				});
		listDis.add(dis);
	}


	/**
	 * 填报
	 * @param vo
	 */
	public void subInfo(final OwnerVo vo, final String siteNo, final String date, final String timePart){
		breakView.showDialog(true,vo.getOwnerName()+"的业务正在填报中...");
		URLParam param = new URLParam(UrlConstant.URL_RECORD);
		param.setMethod(Method.POST);
		param.addParam("CarServiceNo",String.valueOf(vo.getCarServiceNo()));
		param.addParam("NumberType",String.valueOf(vo.getNumberType()));
		param.addParam("IdentCode",vo.getIdentCode());
		param.addParam("OwnerName",vo.getOwnerName());
		param.addParam("OwnerType",String.valueOf(vo.getOwnerType()));
		param.addParam("CardType",String.valueOf(vo.getCardType()));
		param.addParam("CardCode",vo.getCardCode());
		param.addParam("IsAgent","0");
		param.addParam("__RequestVerificationToken",SharePreUtils.getStrConfig(context,"myToken"));

		Disposable dis = ApiManager.getInstance().requestDefault(param, context, true)
				.subscribe(new Consumer<ResponseBody>() {
					@Override
					public void accept(ResponseBody responseBody) throws Exception {
						String rs = responseBody.string();
						String error = paseHtmlSessionError(rs);
						breakView.showDialog(false, "");
						if (TextUtils.isEmpty(error)) {
							affirm(vo, siteNo, date, timePart);
						} else {
							breakView.initUser(false, error);
						}

					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						throwable.printStackTrace();
						Log.e(TAG, throwable.getMessage());
						breakView.initUser(false, throwable.getMessage());
					}
				});
		   listDis.add(dis);
	}

	/**
	 * 确认填报
	 */
	private  void affirm(final  OwnerVo vo, final String sitNo, final String date, final String timePart){
		URLParam	 param = new URLParam(UrlConstant.URL_Affirm);
		param.setMethod(Method.POST);
		param.addParam("isAffirmIdentCode","on") ;
		param.addParam("isAffirmInfo","on") ;
		param.addParam("btnSubmit","确认，提交填报") ;
		Disposable dis = ApiManager.getInstance().requestDefault(param, context, true)
				.subscribe(new Consumer<ResponseBody>() {
					@Override
					public void accept(ResponseBody responseBody) throws Exception {
						String rs = responseBody.string();
						if (!TextUtils.isEmpty(rs) && rs.contains("预约时间地点")) {
							breakView.initUser(true, "填报成功");
							subUser(sitNo, date, timePart, vo.getCarServiceNo() + "", vo.getNumberType() + "");
						} else {
							breakView.showDialog(false, "");
							breakView.initUser(false, "填报失败了");
						}
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						breakView.initUser(false, throwable.getMessage());
					}
				});
		listDis.add(dis);
	}


	/**
	 *   提交预约
	 * @param sitNo 服务站
	 * @param date 预约时间 2008-04-11
	 * @param timePart    1上午，2下午
	 * @param carNo        业务类型
	 * @param numType    车辆类型
	 */
	public void subUser(String sitNo, String date, final String timePart, String carNo, String numType){
		breakView.showDialog(true,"预约处理中...");
		URLParam param = new URLParam(UrlConstant.URL_Bespeak);
		param.setMethod(Method.POST);
		param.addParam("BespeakSiteNo",sitNo);
		param.addParam("BespeakDate",date);
		param.addParam("BespeakTimePart",timePart);
		param.addParam("CarRecord.CarServiceNo",carNo);
		param.addParam("CarRecord.NumberType",numType);
		Disposable dis = ApiManager.getInstance().requestDefault(param, context, true)
				.subscribe(new Consumer<ResponseBody>() {
					@Override
					public void accept(ResponseBody responseBody) throws Exception {
						breakView.showDialog(false,"");
						String rs = responseBody.string();
						String title = Jsoup.parse(rs).title();
						Log.i(TAG,title);
						if("预约成功".equals(title)  || rs.contains("预约成功")){
							breakView.submitUser(true,rs);
						}else{
							breakView.submitUser(false,rs);
						}
						breakView.showDialog(false,"");
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						breakView.submitUser(false,"预约出错了");
						breakView.showDialog(false,"");
					}
				});
		listDis.add(dis);
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
	 * 解析会话失效
	 * @param html
	 * @return
	 * @throws Exception
	 */
	private String paseHtmlSessionError(String html) throws Exception{
		Document document= Jsoup.parse(html);
		Elements er = document.body().select("div.validation-summary-errors");
		Elements error = er.select("ul li");
		return error.text();
	}


	static interface BreakView{
		  public void showDialog(boolean show,String title);
		  public void submitUser(boolean success,String msg);
		  public void login(boolean success,String msg);
		  public void initUser(boolean success,String msg);
	}
}
