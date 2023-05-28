package com.sct.mobile.application.client.interceptor;

import com.sct.mobile.application.service.TokenService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class SctApiHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        return chain.proceed(chain.request().newBuilder()
                .addHeader("Authorization", TokenService.getJwt().getToken())
                .build());
    }
}
