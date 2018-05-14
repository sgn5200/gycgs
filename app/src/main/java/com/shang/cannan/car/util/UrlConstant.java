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
	/**
	 * post
	 * ?mobileTel=18585051719
	 */
	 public static final String URL_SMS_GET  ="http://gycgs.gzbxd.com/CarAPP/CarRecord/SmsSecurityCode";

	/**
	 * post
	 *    MobileTel
	 *    SmsSecurityCode
	 */
	public static final String URL_SMS_VERIFY  ="http://gycgs.gzbxd.com/CarAPP/CarRecord/Sms";


	/**
	 * post
	 * ?carServiceNo=1&numberType=1
	 */
	public static final String URL_BespeakSiteList ="http://gycgs.gzbxd.com/CarAPP/CarRecord/BespeakSiteList";

	/**
	 * post
	 */
	public static final String URL_GETSiteList ="/CarAPP/CarRecord/BespeakSiteList";

	/**
	 * post
	 * ?bespeakSiteNo=05&bespeakServiceNo=1&numberType=1
	 */
	public static final String URL_MaySiteList ="/CarAPP/CarRecord/MayBespeakList";




}
