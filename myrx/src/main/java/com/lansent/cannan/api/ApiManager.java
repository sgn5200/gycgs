package com.lansent.cannan.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lansent.cannan.util.Log;
import com.lansent.cannan.util.SimpleUtil;

import org.reactivestreams.Publisher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * Description    : api 请求的封装
 *                        定制请求和返回response的处理方式
 * CreateAuthor: Cannan
 * Create time   : 2017/7/26 0026.
 */
public class ApiManager {

	private static String TAG = "API_TAG";

	/**
	 * retrofit 请求service
	 */
	private ApiService service;

	/**
	 * client 请求参数配置
	 */
	private static RxHttpClient.Builder config;

	/**
	 * retrofit 请求service
	 *
	 * @param client 由Retrofit re
	 */
	private ApiManager(Retrofit client) {
		this.service = client.create(ApiService.class);
	}
	
	private static ApiManager instance;

	/**
	 *  http请求详细配置获取
	 * @return
	 */
	 public static RxHttpClient.Builder getConfig(){
		  if(config==null){
		  	config = new RxHttpClient.Builder();
		  }
		  return config;
	 }

	/**
	 * 请求配置设置 eg：超时，缓存，重连。。
	 * @param builder
	 */
	 public static void setConfig(RxHttpClient.Builder builder){
	 	config = builder;
	 }

	/**
	 * 自定义配置
	 * @param config 不可为空
	 * @return
	 */
	public static ApiManager getInstance(RxHttpClient.Builder config){
	 	if(instance == null) {
	 		ApiManager.config = config;
			instance = new ApiManager(RxHttpClient.getClient(config));
		}
		return instance;
	}

	/**
	 * 自定义配置
	 * @return
	 */
	public static ApiManager getInstance(){
		if(config == null){
			Log.e(TAG,"还没有配置Build信息");
			return null;
		}

		if(instance == null) {
			instance = new ApiManager(RxHttpClient.getClient(config));
		}
		return instance;
	}

	public static void clear(){
		config =null;
		instance=null;
	}


	/**
	 *
	 * @param param  {@link URLParam p} 设置入参，url地址
	 * @param context 可能为空，如果不为空将检查网络情况
	 * @param runIo 是否运行在io  订阅在主线程, param 为false则需自己指定线程
	 * @return  Flowable<T>
	 */
	public Flowable<ResponseBody> requestDefault(URLParam param, Context context, boolean runIo) {
		Flowable<ResponseBody> netErrorFb ;
		boolean networkEnable = SimpleUtil.isNetworkAvailable(context);
		if (!networkEnable) {
			 netErrorFb = Flowable.create(new FlowableOnSubscribe<ResponseBody>() {
				@Override
				public void subscribe(FlowableEmitter<ResponseBody> e) throws Exception {
					e.onError(new Throwable("网络不可用，请检查后重试"));
					e.onComplete();
				}
			}, BackpressureStrategy.ERROR);
			return netErrorFb;
		}


		Flowable<ResponseBody> base = null;
		switch (param.getMethod()) {
			case Method.GET:
				if (param.getParam() == null || param.getParam().isEmpty()) {
					base = service.get(param.getUrl());
				} else {
					base = service.get(param.getUrl(), param.getParam());
				}
				break;
			case Method.POST:
				base = service.post(param.getUrl(), param.getParam());
				break;
			case Method.PUT:
				base = service.put(param.getUrl(), param.getParam());
				break;
			case Method.DELETE:
				base = service.delete(param.getUrl(), param.getParam());
				break;
			default:
				Log.e(TAG, " method is null");
				break;
		}
		if(runIo){
			return   base.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
		}
		return base;
	}




	/**
	 *
	 * @param param  {@link URLParam p} 设置入参，url地址
	 * @param token    {@link TypeToken token}  gson封装处理解析，T为返回类型
	 * @param context 可能为空，如果不为空将检查网络情况
	 * @param runIo 是否运行在io  订阅在主线程, param 为false则需自己指定线程
	 * @return  Flowable<T>
	 */
	public <T extends BaseResponse<?>> Flowable<T> request(URLParam param, TypeToken<T> token, Context context,boolean runIo) {
		Flowable<T> netErrorFb ;
		if (context != null && (netErrorFb = netError(context))!=null){
			return netErrorFb;
		}

		Flowable<ResponseBody> base = null;
		switch (param.getMethod()) {
			case Method.GET:
				if (param.getParam() == null || param.getParam().isEmpty()) {
					base = service.get(param.getUrl());
				} else {
					base = service.get(param.getUrl(), param.getParam());
				}
				break;
			case Method.POST:
				base = service.post(param.getUrl(), param.getParam());
				break;
			case Method.PUT:
				base = service.put(param.getUrl(), param.getParam());
				break;
			case Method.DELETE:
				base = service.delete(param.getUrl(), param.getParam());
				break;
			default:
				Log.e(TAG, " method is null");
				break;
		}
		if(runIo){
			return   flatMapOb(param,base,token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
		}
		return flatMapOb(param,base,token);
	}


	/**
	 * 检查网络情况
	 * @return true 有网络
	 */
	private <T extends BaseResponse<?>> Flowable<T> netError(Context context) {

		boolean networkEnable = SimpleUtil.isNetworkAvailable(context);
		if (!networkEnable) {
			Flowable<T> netErrorFb = Flowable.create(new FlowableOnSubscribe<T>() {
				@Override
				public void subscribe(FlowableEmitter<T> e) throws Exception {
					e.onError(new Throwable("网络不可用，请检查后重试"));
					e.onComplete();
				}
			}, BackpressureStrategy.ERROR);
			return netErrorFb;
		}
		return null;
	}


	/**
	 * 转换为泛型中的解析对象
	 *
	 * @param base OKHttp 3 和Retrofit Flowable<ResponseBody>
	 * @param <T>  解析的泛型
	 * @return Flowable<BaseResponse<T>
	 */
	private <T extends BaseResponse<?>> Flowable<T> flatMapOb(final URLParam param,Flowable<ResponseBody> base,final TypeToken<T> token) {
		return base.flatMap(new Function<ResponseBody, Publisher<T>>() {
			@Override
			public Publisher<T> apply(ResponseBody responseBody) throws Exception {
				T response = null;
				String dataStr = responseBody.string();
				Log.i(TAG, param.getUrl());
				Log.i(TAG, dataStr);
				Gson gson = new Gson();
				try{
					response =  gson.fromJson(dataStr,token.getType());
				}catch(Exception e){
					Log.e(TAG, "解析异常：检查json格式和接收泛型");
				}
				return getReturnFlowable(response);
			}

			private Flowable<T> getReturnFlowable(final T t) {
				return Flowable.create(new FlowableOnSubscribe<T>() {
					@Override
					public void subscribe(FlowableEmitter<T> e) throws Exception {
						if (t == null) {
							e.onError(new Throwable("解析异常"));
						} else {
							e.onNext(t);
						}
						e.onComplete();
					}
				}, BackpressureStrategy.ERROR);
			}
		});
	}

	/**
	 * post 表单上传
	 * @param url
	 * @param body
	 * @param map
	 * @return
	 */
	public  Flowable<ResponseBody> uploadFile(String url, RequestBody body, HashMap<String,String> map){
		return service.uploadFile(url,body);
	}

	/**
	 * post 表单参数上传
	 * @param url
	 * @param parts
	 * @param map
	 * @return
	 */
	public Flowable<ResponseBody> requestUploadWork(String url, List<MultipartBody.Part> parts, Map<String, String> map){
		return service.requestUploadWork(url,map,parts);
	}

	/**
	 * GET 下载
	 * @param url
	 * @return
	 */
	public Flowable<ResponseBody> download(String url){
		return  service.download(url);
	}

}
