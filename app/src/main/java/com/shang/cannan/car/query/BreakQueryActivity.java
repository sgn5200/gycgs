package com.shang.cannan.car.query;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lansent.cannan.base.AbsBaseActivity;
import com.lansent.cannan.util.Log;
import com.shang.cannan.car.R;

public class BreakQueryActivity extends AbsBaseActivity {

	private WebView webView;
	private int type;//1 传url地址，2传html/text

	@Override
	public int getLayout() {
		return R.layout.activity_break_query;
	}

	@Override
	public void initViews() {
		webView = getView(R.id.webView);
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			showToast("网页启动需要参数");
			finish();
			return;
		}
		type = bundle.getInt("type");
		if (type == 1) {
			String url = bundle.getString("url");
			initWeb(url, true);
		} else if (type == 2) {
			String html = bundle.getString("html");
			initWeb(html, false);
		}
	}


	private void initWeb(String urlHtml, boolean isUrl) {
		Log.i(TAG, urlHtml);
		WebSettings settings = webView.getSettings();
		//默认是false 设置true允许和js交互
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);

		//Tips:有网络可以使用LOAD_DEFAULT 没有网时用LOAD_CACHE_ELSE_NETWORK
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		//开启 DOM storage API 功能 较大存储空间，使用简单
		settings.setDomStorageEnabled(true);
		//设置数据库缓存路径 存储管理复杂数据 方便对数据进行增加、删除、修改、查询 不推荐使用
		settings.setDatabaseEnabled(false);
		//开启 Application Caches 功能 方便构建离线APP 不推荐使用
		settings.setAppCacheEnabled(false);
		final String cachePath = getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
		settings.setAppCachePath(cachePath);
		settings.setAppCacheMaxSize(5 * 1024 * 1024);
		webView.setWebViewClient(new MyWebViewClient());
// 设置可以支持缩放
		webView.getSettings().setSupportZoom(true);
// 设置出现缩放工具
		webView.getSettings().setBuiltInZoomControls(true);
//扩大比例的缩放
		webView.getSettings().setUseWideViewPort(true);
//自适应屏幕
		webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLoadWithOverviewMode(true);
		if (isUrl) {
			webView.loadUrl(urlHtml);
		} else {
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
			Log.i(TAG, "----------------------shouldOverrideUrlLoading 。。 " + url);
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
			webView.goBack();// 返回前一个页面
//			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
