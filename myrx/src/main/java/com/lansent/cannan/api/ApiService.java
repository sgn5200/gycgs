package com.lansent.cannan.api;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Cannan on 2017/7/26 0026.
 *
 * 真正请求入口，url动态传入，目前请求方法有get post put delete.
 */

public interface ApiService {

    /**
     * get 请求，动态传入url，无参数
     * @param url
     * @return
     */
    @GET()
    Flowable<ResponseBody> get(@Url String url);

    /**
     * get请求， 动态传入url，参数为 Map
     * @param url
     * @param maps
     * @return
     */
    @GET()
    Flowable<ResponseBody> get(@Url String url, @QueryMap Map<String, String> maps);

    /**
     * post 请求，动态url，参数 linkMap<String,String>  ，表单形式传入
     * @param url
     * @param map
     * @return Flowable<ResponseBody> 用于订阅
     */
    @FormUrlEncoded
    @POST()
    Flowable<ResponseBody> post(@Url String url, @FieldMap Map<String, String> map);

     @PUT
     Flowable<ResponseBody> put(@Url String url, @FieldMap Map<String, String> map);

    @DELETE
    Flowable<ResponseBody> delete(@Url String url, @FieldMap Map<String, String> map);



    @Streaming
    @GET
    Flowable<ResponseBody> download(@Url String fileUrl);


    @Multipart
    @POST()
    Flowable<ResponseBody> requestUploadWork(@Url String ur, @FieldMap Map<String, String> map, @Part List<MultipartBody.Part> parts);

    @POST()
    @Multipart
    Flowable<ResponseBody> uploadFile(@Url String ulr, @Part("file\"; filename=\"aaa.apk\"") RequestBody file);
}
