package com.lansent.cannan.api.cookie;

import android.text.TextUtils;

import com.lansent.cannan.util.SharePreUtils;
import com.lansent.cannan.util.Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/16 0016.
 *
 * okhttp请求时 添加设置cookie信息
 */

public class AddCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        String cookie = SharePreUtils.getStrConfig(Utils.getApp(),"cookie");

        if (!TextUtils.isEmpty(cookie)) {
            String SessionId = SharePreUtils.getStrConfig(Utils.getApp(),"SessionId");

            if(!TextUtils.isEmpty(SessionId) && !TextUtils.isEmpty(SessionId)){
                cookie = cookie+";"+SessionId;
            }

            builder.addHeader("Cookie", cookie);
        }
        return chain.proceed(builder.build());
    }
}