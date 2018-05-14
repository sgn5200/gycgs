package com.lansent.cannan.api;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.lansent.cannan.util.Log;

import org.json.JSONException;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import retrofit2.HttpException;

/**
 * Description    :RxAndroid 网络请求结果中转处理
 * CreateAuthor: Cannan
 * Create time   : 2017/9/30 0030     上午 10:41
 */

public abstract class BaseSub<T extends BaseResponse<?>>
			implements Subscriber<T>,IApiCallback<T> {

	
	/**
	 * 日志打印标签
	 */
	private String TAG = getClass().getSimpleName();

	/**
	 * 结果处理回调接口
	 */
	IApiCallback<T> iApiBack;

	/**
	 * 构造方法，同时启动onStart方法，用于通知UI更新，表示请求已经发起
	 *    回调接口，需指定泛型参数，解析时以传入的泛型为参考依据
	 */
	public BaseSub() {
		iApiBack =this;
		iApiBack.callStart();
	}
	
	@Override
	public void onSubscribe(Subscription s) {
		Log.i(TAG);
		s.request(Integer.MAX_VALUE);
	}
	

	@Override
	public void onNext(T response) {
		Log.i(TAG,"res=="+response.getData());
		
		if(response.isSuccess()){
			this.iApiBack.callSuccess(response);
		}else{
			this.iApiBack.CallFailure(response);
		}
	}
	
	


	@Override
	public void onError(Throwable t) {
		Log.e(TAG,t.getMessage());
		if (t.getMessage().endsWith("No address associated with hostname")) {
			iApiBack.callError("服务器地址错误");
		}else {
			iApiBack.callError(getErrorMsg(t));
		}
		this.iApiBack.callComplete();
	}

	@Override
	public void onComplete() {
		this.iApiBack.callComplete();
	}

	//对应HTTP的状态码
	private  final int UNAUTHORIZED = 401;
	private  final int FORBIDDEN = 403;
	private  final int NOT_FOUND = 404;
	private  final int REQUEST_TIMEOUT = 408;
	private  final int INTERNAL_SERVER_ERROR = 500;
	private  final int BAD_GATEWAY = 502;
	private  final int SERVICE_UNAVAILABLE = 503;
	private  final int GATEWAY_TIMEOUT = 504;

	/**
	 * 获取请求错误处理后的信息，展示给UI
	 * @param throwable
	 * @return
	 */
	private String getErrorMsg(Throwable throwable) {
		if (throwable instanceof SocketTimeoutException ||
				throwable instanceof TimeoutException) {
			return "网络请求超时，请稍后重试";
		}
		if (throwable instanceof HttpException) {             //HTTP错误
			HttpException httpException = (HttpException) throwable;
			Log.e(TAG,"error code = "+httpException.code());
			switch (httpException.code()) {
				case UNAUTHORIZED:
					return "证书认证错误";
				case FORBIDDEN:
					return "网页禁止访问";
				case NOT_FOUND:
					return "网页找不到";
				case REQUEST_TIMEOUT:
					return "请求超时";
				case GATEWAY_TIMEOUT:
					return "请求超时";
				case INTERNAL_SERVER_ERROR:
					return "服务器异常";
				case BAD_GATEWAY:
					return "网关错误";
				case SERVICE_UNAVAILABLE:
					return "服务不可用";
				default:
					return "网络错误";  //均视为网络错误
			}
		}

		if (throwable instanceof JsonParseException
				|| throwable instanceof JSONException
				|| throwable instanceof ParseException) {
			return "解析错误";            //均视为解析错误
		}

		if (throwable instanceof ConnectException) {
			return "连接失败";  //均视为网络错误
		}
		return "未知错误";          //未知错误
	}

	@Override
	public void callStart() {
		Log.i(TAG);
	}


	@Override
	public void callComplete() {
		Log.i(TAG);
	}

}
