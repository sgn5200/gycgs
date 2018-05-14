package com.lansent.cannan.api;

import java.util.HashMap;


/**
 * Created by Cannan on 2017/9/27 0027.
 *
 * http 请求的参数

 * {"code":200,"message":"成功","data":xxx} T 只关联data
 *
 * 完整解析为 vo =  { com.cannan.com.cannan.framwork.api.BaseResponse<T>}
 */

public class URLParam {
	private String url;
	private HashMap<String, String> map;
	private int method = Method.GET;

	/**
	 * 网络请求地址
	 * @param url
	 */
	public URLParam(String url){
		this.url = url ;
		map = new HashMap<String,String>();
	}

	/**
	 * 网络请求方法
	 * @return  {@link Method}
	 */
	public int getMethod() {
		return method;
	}

	/**
	 *  {@link Method}
	 * 设置网络请求方法 get or post or put...
	 * @param  method  接收参数限定为 {@link Method}
	 */
	public void setMethod(@Method.METHOD int method) {
		this.method = method;
	}

	/**
	 * 获取URL 网络请求地址
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 添加参数
	 * @param key
	 * @param value
	 * @return  ArrayMap
	 */
	public HashMap<String,String>  addParam(String key,String value){
		this.map.put(key,value);
		return this.map;
	}

	/**
	 * 添加参数 参数为{@link HashMap<String,String> } 小数据使用性能高
	 * 大数据使用{@link HashMap}
	 * @param map
	 * @return  ArrayMap
	 */
	public HashMap<String,String> addParam(HashMap<String,String> map){
		this.map.putAll(map);
		return this.map;
	}

	/**
	 * 返回map参数
	 * @return ArrayMap
	 */
	public HashMap<String,String> getParam(){
		return this.map;
	}

}
