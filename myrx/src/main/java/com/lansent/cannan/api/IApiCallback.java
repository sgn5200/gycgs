package com.lansent.cannan.api;

public interface IApiCallback<T> {

	/**
	 * 开始回调
	 */
	public void callStart();

	/**
	 * 正常执行时回调，判断请求结果code是否为约定的成功码，并将结果通知前端UI更新
	 * 约定成功码在response 中覆盖
	 * T response extend BaseResponse
	 * @param response BaseResponse包含code，message  ，泛型data等请求结果实体
	 */
	public void callSuccess(T response);

	
	/**
	 * 正常执行时回调，请求失败
	 * @param response
	 */
	public void CallFailure(T response);
	
	/**
	 * 出错时回调
	 * @param e
	 */
	public void callError(String e);

	/**
	 *  完成时回调
	 */
	public void callComplete();
	
}