package com.sct.mobile.application.config;

import com.sct.mobile.application.client.AuthApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private static NetworkService instance;

    private final Retrofit authRetrofit;

    private NetworkService() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(interceptor);

        authRetrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.AUTH_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static NetworkService getInstance() {
        if(instance == null) {
            instance = new NetworkService();
        }
        return instance;
    }

    public AuthApi getAuthApi(){
        return authRetrofit.create(AuthApi.class);
    }
}
