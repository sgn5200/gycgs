package com.lansent.cannan.api;

import android.text.TextUtils;

import com.lansent.cannan.api.cookie.AddCookiesInterceptor;
import com.lansent.cannan.api.cookie.ReceivedCookiesInterceptor;
import com.lansent.cannan.data.MemoryCache;
import com.lansent.cannan.util.Log;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RxHttpClient {


	public static class Builder{
		private int connectTimeout = 10;
		private int writeTimeout = 30;
		private int readTimeout = 30;
		private boolean retry = false;
		private SSLSocketFactory sslSocketFactory;
		private X509TrustManager trustManager;
		private boolean useCookie = false;
		private String baseUrl;
		private String cookiedId;

		public String getCookiedId() {
			return this.cookiedId;
		}

		public String getBaseUrl() {
			return this.baseUrl;
		}

		public void setCookiedId(String cookiedId) {
			this.cookiedId = cookiedId;
			MemoryCache.getInstance().put("Cookie",cookiedId);
		}

		public void setLogEnable(boolean enable){
			Log.enable = enable;
		}

		public  Builder setConnectTimeout(int connectTimeout) {
			this.connectTimeout = connectTimeout;
			return this;
		}

		public  Builder setWriteTimeout(int writeTimeout) {
			this.writeTimeout = writeTimeout;
			return this;
		}

		public  Builder setReadTimeout(int readTimeout) {
			this.readTimeout = readTimeout;
			return this;
		}

		public  Builder setRetry(boolean retry) {
			this.retry = retry;
			return this;
		}

		public  Builder setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
			this.sslSocketFactory = sslSocketFactory;
			return this;
		}

		public  Builder setTrustManager(X509TrustManager trustManager) {
			this.trustManager = trustManager;
			return this;
		}

		public Builder setUseCookie(boolean useCookie) {
			this.useCookie = useCookie;
			return this;
		}

		public Builder setBaseUrl(String baseUrl) {
			this.baseUrl = baseUrl;
			return this;
		}
	}

	protected static Retrofit getClient(Builder config) {
		Retrofit.Builder reBuilder = new Retrofit.Builder();
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(config.connectTimeout, TimeUnit.SECONDS)
				.writeTimeout(config.writeTimeout,TimeUnit.SECONDS)
				.readTimeout(config.readTimeout,TimeUnit.SECONDS)
				.retryOnConnectionFailure(config.retry);
		if(config.sslSocketFactory!=null){
			builder.sslSocketFactory(config.sslSocketFactory,config.trustManager);
		}
		if(config.useCookie){
			builder.addInterceptor(new AddCookiesInterceptor());
			builder.addInterceptor(new ReceivedCookiesInterceptor());
		}

		if(!TextUtils.isEmpty(config.baseUrl)){
			reBuilder.baseUrl(config.baseUrl);
		}else{
			reBuilder.baseUrl("http://192.168.41.6");
		}
		reBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create()); //添加RX和Retrofit结合的adapter
		//.addConverterFactory(GsonConverterFactory.create(new Gson()));  //该结构不需要实现Gson，已经自己处理
		return reBuilder.client(builder.build()).build();
	}
}
