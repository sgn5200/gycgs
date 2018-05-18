package com.shang.cannan.car.query;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lansent.cannan.base.AbsBaseActivity;
import com.lansent.cannan.util.Log;
import com.shang.cannan.car.R;

public class BreakQueryActivity extends AbsBaseActivity {

	private WebView webView;
	private String urlHtml;
	private String infoUrl;

	@Override
	public int getLayout() {
		return R.layout.activity_break_query;
	}

	@Override
	public void initViews() {
		webView = getView(R.id.webView);
		Bundle bundle =getIntent().getExtras();
		if(bundle!=null){
			urlHtml=bundle.getString("urlHtml");
			if(TextUtils.isEmpty(urlHtml)){
				infoUrl=bundle.getString("infoUrl");
			}else {
				webView.setOnTouchListener(new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						finish();
						return true;
					}
				});
			}
		}

		if(bundle!=null ){

		}

		 if(!TextUtils.isEmpty(urlHtml)){
			 initWeb(urlHtml,false);

		 }else if(TextUtils.isEmpty(infoUrl)){
		 	initWeb("http://gycgs.gzbxd.com/CarAPP/CarRecord/Query",true);
		 }else {
		 	initWeb(infoUrl,true);
		 }

	}


	private void initWeb(String urlHtml,boolean flag) {

		WebSettings settings = webView.getSettings();
		//默认是false 设置true允许和js交互
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		//  WebSettings.LOAD_DEFAULT 如果本地缓存可用且没有过期则使用本地缓存，否加载网络数据 默认值
		//  WebSettings.LOAD_CACHE_ELSE_NETWORK 优先加载本地缓存数据，无论缓存是否过期
		//  WebSettings.LOAD_NO_CACHE  只加载网络数据，不加载本地缓存
		//  WebSettings.LOAD_CACHE_ONLY 只加载缓存数据，不加载网络数据
		//Tips:有网络可以使用LOAD_DEFAULT 没有网时用LOAD_CACHE_ELSE_NETWORK
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		//开启 DOM storage API 功能 较大存储空间，使用简单
		settings.setDomStorageEnabled(true);
		//设置数据库缓存路径 存储管理复杂数据 方便对数据进行增加、删除、修改、查询 不推荐使用
		settings.setDatabaseEnabled(true);
		final String dbPath = getApplicationContext().getDir("db", Context.MODE_PRIVATE).getPath();
		settings.setDatabasePath(dbPath);
		//开启 Application Caches 功能 方便构建离线APP 不推荐使用
		settings.setAppCacheEnabled(true);
		final String cachePath = getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
		settings.setAppCachePath(cachePath);
		settings.setAppCacheMaxSize(5 * 1024 * 1024);
		webView.setWebViewClient(new MyWebViewClient());
		if(flag){
			webView.loadUrl(urlHtml);
		}else{
			webView.loadData(urlHtml, "text/html; charset=UTF-8", null);//这种写法可以正确解码
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	// 监听 所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i(TAG,"----------------------shouldOverrideUrlLoading 。。 " + url);
			return false;
		}


		@Override
		public void onPageFinished(WebView view, String url) {
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageFinished(view, url);
		}
	}

	// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
		// webview.goBack();// 返回前一个页面
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
