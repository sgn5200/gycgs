package com.shang.cannan.car;

import com.lansent.cannan.api.ApiManager;
import com.lansent.cannan.api.RxHttpClient;
import com.lansent.cannan.app.App;
import com.lansent.cannan.data.DBHelper;
import com.lansent.cannan.util.Log;
import com.lansent.cannan.util.Utils;
import com.shang.cannan.car.dao.SqlConstant;
import com.shang.cannan.car.util.UrlConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/8     12:49
 * Project          : Car
 * PackageName :  com.shang.cannan.car;
 */

public class MyApp extends App {

	public static DBHelper helper;
	public static String[] CarServiceNo,NumberType; //业务类型，车辆类型
	public static String TOKEN;


	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		List<String> listTable = new ArrayList<>();
		listTable.add(SqlConstant.SQL_OWNER_CREATE);
		helper= new DBHelper(this,"cannan",listTable);
		CarServiceNo = getResources().getStringArray(R.array.CarServiceNo);
		NumberType = getResources().getStringArray(R.array.NumberType);
	}

	public static  void initHttp(boolean isWx) {
		Log.i("MyApp","isWx"+isWx);
		UrlConstant.setMyUrl(isWx);
		Utils.init(mContext);
		RxHttpClient.Builder builder =ApiManager.getConfig();
		builder.setBaseUrl(isWx?UrlConstant.BASE_URL_WX:UrlConstant.BASE_URL_WEB)
				.setUseCookie(true)
				.setRetry(true)
				.setLogEnable(true);
		ApiManager.setConfig(builder);
	}



}
