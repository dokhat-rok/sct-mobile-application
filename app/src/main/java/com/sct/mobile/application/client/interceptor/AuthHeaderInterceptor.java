package com.sct.mobile.application.client.interceptor;

import com.sct.mobile.application.data.SharedDataUtil;
import com.sct.mobile.application.model.enums.SharedName;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthHeaderInterceptor implements Interceptor {

    private static String cookie = "";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        if(cookie.equals("")){
            cookie = SharedDataUtil.getString(SharedName.COOKIE.getLabel());
        }
        if(!cookie.equals("")){
            builder.addHeader("Cookie", cookie);
        }

        return chain.proceed(builder.build());
    }
}
