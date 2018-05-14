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

	public BreakPresent(BreakView breakView, Context context){
		this.context =context;
		this.breakView = breakView;
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
	}


	/**
	 * 填报
	 * @param vo
	 */
	public void subInfo(final OwnerVo vo, final String siteNo, final String date, final String timePart){
		breakView.showDialog(true);
		URLParam param = new URLParam("/CarAPP/CarRecord/Record ");
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

		ApiManager.getInstance().requestDefault(param,context,true)
				.subscribe(new Consumer<ResponseBody>() {
					@Override
					public void accept(ResponseBody responseBody) throws Exception {
						String rs = responseBody.string();
						String error = paseHtmlSessionError(rs);
						if(TextUtils.isEmpty(error)){
							affirm(vo,siteNo,date,timePart);
						}else{
							breakView.showDialog(false);
							breakView.initUser(false,error);
						}

					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						throwable.printStackTrace();
						Log.e(TAG,throwable.getMessage());
					}
				});

	}

	/**
	 * 确认填报
	 */
	private  void affirm(final  OwnerVo vo, final String sitNo, final String date, final String timePart){
		URLParam	 param = new URLParam("/CarAPP/CarRecord/Affirm");
		param.setMethod(Method.POST);
		param.addParam("isAffirmIdentCode","on") ;
		param.addParam("isAffirmInfo","on") ;
		param.addParam("btnSubmit","确认，提交填报") ;
		ApiManager.getInstance().requestDefault(param,context,true)
				.subscribe(new Consumer<ResponseBody>() {
					@Override
					public void accept(ResponseBody responseBody) throws Exception {
						String rs = responseBody.string();
						if(!TextUtils.isEmpty(rs) && rs.contains("预约时间地点")){
							breakView.initUser(true,"填报成功");
							subUser(sitNo,date,timePart,vo.getCarServiceNo()+"",vo.getNumberType()+"");
						}else {
							breakView.showDialog(false);
							breakView.initUser(false,"填报失败了");
						}
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {

					}
				});
	}


	/**
	 *   提交预约
	 * @param sitNo 服务站
	 * @param date 预约时间 2008-04-11
	 * @param timePart    1上午，2下午
	 * @param carNo        业务类型
	 * @param numType    车辆类型
	 */
	public void subUser(String sitNo,String date,String timePart,String carNo,String numType){
		URLParam param = new URLParam("/CarAPP/CarRecord/Bespeak");
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
						breakView.showDialog(false);
						String rs = responseBody.string();
						breakView.submitUser(true,rs);
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						breakView.submitUser(false,"预约出错了");
						breakView.showDialog(false);
					}
				});
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
		  public void showDialog(boolean show);
		  public void submitUser(boolean success,String msg);
		  public void login(boolean success,String msg);
		  public void initUser(boolean success,String msg);
	}







	String htmlFinish ="\n" +
			"\n" +
			"<!DOCTYPE html>\n" +
			"\n" +
			"<html>\n" +
			"<head>   \n" +
			"    <meta charset=\"utf-8\">\n" +
			"\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
			"    <title>预约成功</title>   \n" +
			"    <link href=\"/CarAPP/Content/mobileCss?v=IhD5fzGF6xKwwBCnFkGJGJHTq3_G7MAxafykXcBpjWY1\" rel=\"stylesheet\"/>\n" +
			"<link href=\"/CarAPP/Content/css?v=fflyC6wbnEnxqDrJc7BMeUfMF2IDDeoPiz2blYNgGNI1\" rel=\"stylesheet\"/>\n" +
			"\n" +
			"    <script src=\"/CarAPP/bundles/modernizr?v=rGcoDow97GYrNMSwHq7xCCjlcB3UIY4_OhPRc6BBSQA1\"></script>\n" +
			" \n" +
			"    <script src=\"/CarAPP/bundles/jquery?v=1A_Qqa6eu1hIFc9O--lfxRqvbqGj9Zd6uAr93zLdrWM1\"></script>\n" +
			"<script src=\"/CarAPP/bundles/jquerymobile?v=j8uudNNdLQQsh05CG2aZ3TvZB0NSp3FgGS7SaC_6YZQ1\"></script>\n" +
			"  \n" +
			"    <script src=\"/CarAPP/bundles/jqueryval?v=-tc2QZUKsI5XsBJSyox6jU38dSPE468EEX0oQlQTeSE1\"></script>\n" +
			"\n" +
			"</head>\n" +
			"<body>\n" +
			"   <!-- Home -->\n" +
			"<div data-role=\"page\" id=\"page1\">\n" +
			"    <div data-theme=\"c\" data-role=\"header\">\n" +
			"        <a data-role=\"button\" href=\"/CarAPP/CarRecord/Record\" data-icon=\"back\" data-iconpos=\"left\"\n" +
			"        class=\"ui-btn-left\" data-ajax=\"false\">\n" +
			"            返回\n" +
			"        </a>\n" +
			"        <h3>\n" +
			"             第5步：预约成功\n" +
			"        </h3>\n" +
			"        <a data-role=\"button\" href=\"/CarAPP/\" data-icon=\"home\" data-iconpos=\"right\"\n" +
			"        class=\"ui-btn-right\" data-ajax=\"false\" >\n" +
			"            首页\n" +
			"        </a>\n" +
			"           </div>\n" +
			"      <div data-role=\"content\">  \n" +
			"           <form action=\"Finish\" data-ajax=\"false\" method=\"post\"> \n" +
			"    <div class=\"validation-summary-valid\" data-valmsg-summary=\"true\"><ul><li style=\"display:none\"></li>\n" +
			"</ul></div>   \n" +
			"         <div data-role=\"fieldcontain\">\n" +
			"            <label for=\"labelCarServiceName\">\n" +
			"                业务类型:\n" +
			"            </label>\n" +
			"            <label id=\"labelCarServiceName\">\n" +
			"                注册登记（非专段号牌、新能源）\n" +
			"            </label>\n" +
			"        </div>\n" +
			"        <div data-role=\"fieldcontain\">\n" +
			"            <label for=\"NumberTypeName\">\n" +
			"                车辆类型:\n" +
			"            </label>\n" +
			"             <label id=\"NumberTypeName\">\n" +
			"               国产小型客车\n" +
			"            </label></div>\n" +
			"         <div data-role=\"fieldcontain\">\n" +
			"            <label for=\"BespeakServiceRemark\">\n" +
			"                办理所需资料清单:\n" +
			"            </label>\n" +
			"             <label id=\"BespeakServiceRemark\">\n" +
			"               1、机动车所有人身份证明【个人：身份证原件及复印件，单位：组织机构代码证原件及复印件（复印件盖公章）】；\n" +
			"2、车辆合格证；\n" +
			"3、车辆购车发票（第四联）；\n" +
			"4、交强险副本；\n" +
			"5、车辆购置税；\n" +
			"6、车辆照片2张/车；\n" +
			"7、车架号拓印膜\n" +
			"若委托他人办理还需提供：\n" +
			"1、委托书；\n" +
			"2、经办人身份证原件及复印件；\n" +
			"将以上资料准备齐后交验机动车！\n" +
			"            </label></div>\n" +
			"        <div data-role=\"fieldcontain\">            \n" +
			"             <label>车辆识别代号:</label>LBVKY1109JSR43111\n" +
			"                    \n" +
			"        </div>  \n" +
			"               \n" +
			"                \n" +
			"\n" +
			"               \n" +
			"                  \n" +
			"        <div data-role=\"fieldcontain\">\n" +
			"             车主类型:个人\n" +
			"            \n" +
			"        </div>\n" +
			"        <div data-role=\"fieldcontain\">\n" +
			"            <label>\n" +
			"               车主名称:李莹云\n" +
			"            </label>\n" +
			"          </div>         \n" +
			"        <div data-role=\"fieldcontain\">\n" +
			"              证件类型:居民身份证号码      \n" +
			"        </div>\n" +
			"        <div data-role=\"fieldcontain\">\n" +
			"                 <label>证件号码:</label>52212419930517082X            \n" +
			"            </div>\n" +
			"        <div data-role=\"fieldcontain\">          \n" +
			"             <label>\n" +
			"               预约次数: 1<label>次</label>\n" +
			"            </label>           \n" +
			"        </div>\n" +
			"          <div data-role=\"fieldcontain\">\n" +
			"                 <label>预约日期:</label>2018-05-28 下午            \n" +
			"            </div>\n" +
			"         <div data-role=\"fieldcontain\">          \n" +
			"             <label>\n" +
			"               预约地点: 花溪办证大厅（孟关汽贸城内）\n" +
			"            </label>           \n" +
			"        </div>\n" +
			"         <div data-role=\"fieldcontain\">          \n" +
			"             <label>\n" +
			"               预约地点地址: 贵阳市花溪区孟关乡汽车城内（请注意“孟关机动车登记服务站”和“花溪办证大厅”的区别，都在孟关汽贸城内，其各自分开在孟关汽贸城内不同的位置！）\n" +
			"            </label>           \n" +
			"        </div>        \n" +
			"       \n" +
			"        <div data-role=\"fieldcontain\">\n" +
			"            <label>\n" +
			"                是否代办: 否   \n" +
			"            </label>           \n" +
			"        </div>               \n" +
			"          <div style=\"text-align:center\">\n" +
			"           <div data-role=\"controlgroup\" data-type=\"horizontal\">\n" +
			"               <a data-role=\"button\" href=\"/CarAPP/CarRecord/RecordView\" data-icon=\"arrow-l\" data-iconpos=\"left\" data-ajax=\"false\">返回</a>\n" +
			"               <a data-role=\"button\"  href=\"/CarAPP/CarRecord/Logout\" data-theme=\"a\" data-ajax=\"false\">退出系统</a> \n" +
			"              </div> \n" +
			"            </div>  \n" +
			"     </form>\n" +
			"    </div>\n" +
			"  \n" +
			"</div>\n" +
			"</body>\n" +
			"</html>\n";
}
