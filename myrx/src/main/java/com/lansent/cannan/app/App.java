package com.lansent.cannan.app;

import android.app.Application;

/**
 * Created by Cannan on 2017/9/26 0026.
 * 全局 Application
 * <p>
 * 初始化 bugly 、Stetho
 */

public class App extends Application {

	protected static App mContext;
	private String TAG = getClass().getSimpleName();

	public static App getInstance() {
		return mContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;

//		RxEvent.getDefault().register(BaseResponse.class, new Consumer<BaseResponse>() {
//			@Override
//			public void accept(BaseResponse ba) throws Exception {
//
//			}
//		}, new Consumer<Throwable>() {
//			@Override
//			public void accept(Throwable throwable) throws Exception {
//
//			}
//		});
//
//		RxEvent.getDefault().unRegister(BaseResponse.class);
//
//
//
//		ApiClient.getDefault().request(new URLParam(""), new TypeToken<BaseResponse<String>>() {}, this)
//				.subscribe(new BaseSub<BaseResponse<String>>() {
//					@Override
//					public void callSuccess(BaseResponse<String> response) {
//
//					}
//
//					@Override
//					public void CallFailure(BaseResponse<String> response) {
//
//					}
//
//					@Override
//					public void callError(String e) {
//
//					}
//				});
//
//		BaseResponse baseResponse = new BaseResponse();
//		RxEvent.getDefault().post(baseResponse);
//		git init
//		git add README.md
//		git commit -m "first commit"
//		git remote add origin https://github.com/sgn5200/rxJar.git
//		git push -u origin master
	}

}
