package com.shang.cannan.car.util;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/8     13:12
 * Project          : Car
 * PackageName :  com.shang.cannan.car;
 */

public class UrlConstant {
	/***********   x-www-form-urlencoded  ********************/
	public static final String BASE_URL_WX = "http://gycgs.gzbxd.com";
	public static final String BASE_URL_WEB = "http://58.42.244.73";

	public static void setMyUrl(boolean isApp) {
		String myUrl;
		if (isApp) {
			myUrl = BASE_URL_WX + "/CarAPP";
		} else {
			myUrl = BASE_URL_WEB + "/car";
		}

		URL_SMS_GET = myUrl + "/CarRecord/SmsSecurityCode";
		URL_SMS_VERIFY = myUrl + "/CarRecord/Sms";
		URL_GETSiteList = myUrl + "/CarRecord/BespeakSiteList";
		URL_MaySiteList = myUrl + "/CarRecord/MayBespeakList";
		URL_Query = myUrl + "/CarRecord/Query";
		URL_CODE = myUrl + "/CheckCode/Index";
		URL_RECORD = myUrl + "/CarRecord/Record";
		URL_Affirm = myUrl + "/CarRecord/Affirm";
		URL_Bespeak = myUrl + "/CarRecord/Bespeak";
		URL_Read = myUrl + "/CarRecord/Read";
	}

	public static String URL_SMS_GET;
	public static String URL_SMS_VERIFY  ;
	public static String URL_GETSiteList;
	public static String URL_MaySiteList;
	public static String URL_Query;
	public static String URL_CODE;
	public static String URL_RECORD;
	public static String URL_Affirm;
	public static String URL_Bespeak;
	public static String URL_Read;

}
