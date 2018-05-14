package com.lansent.cannan.api.cookie;


import com.lansent.cannan.util.SharePreUtils;
import com.lansent.cannan.util.Utils;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/16 0016.
 *
 * okhttp请求时 保存cookie信息
 */

public class ReceivedCookiesInterceptor implements Interceptor {
    public ReceivedCookiesInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        //set-cookie可能为多个
        if (!response.headers("set-cookie").isEmpty()) {
            List<String> cookies = response.headers("set-cookie");
            String cookie = cookies.get(0);
            SharePreUtils.saveStrConfig(Utils.getApp(),"cookie",cookie);
            if(cookie.contains("SessionId")){
                SharePreUtils.saveStrConfig(Utils.getApp(),"SessionId",cookie);
            }
        }
        return response;
    }
}
