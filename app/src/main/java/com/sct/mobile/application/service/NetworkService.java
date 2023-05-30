package com.sct.mobile.application.service;

import com.sct.mobile.application.client.AuthApi;
import com.sct.mobile.application.client.ParkingApi;
import com.sct.mobile.application.client.PriceApi;
import com.sct.mobile.application.client.RentApi;
import com.sct.mobile.application.client.RoutePointApi;
import com.sct.mobile.application.client.TransportApi;
import com.sct.mobile.application.client.TripApi;
import com.sct.mobile.application.client.UserApi;
import com.sct.mobile.application.client.interceptor.AuthHeaderInterceptor;
import com.sct.mobile.application.client.interceptor.SctApiHeaderInterceptor;
import com.sct.mobile.application.config.ApiConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private static NetworkService instance;

    private final Retrofit authRetrofit;
    private final Retrofit apiRetrofit;

    private NetworkService() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        AuthHeaderInterceptor authHeaderInterceptor = new AuthHeaderInterceptor();
        SctApiHeaderInterceptor sctApiHeaderInterceptor = new SctApiHeaderInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(authHeaderInterceptor);

        authRetrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.SCT_AUTH_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        client.interceptors().clear();
        client.addInterceptor(interceptor).addInterceptor(sctApiHeaderInterceptor);
        apiRetrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.SCT_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static NetworkService getInstance() {
        if (instance == null) {
            instance = new NetworkService();
        }
        return instance;
    }

    public AuthApi getAuthApi() {
        return authRetrofit.create(AuthApi.class);
    }

    public UserApi getUserApi() {
        return apiRetrofit.create(UserApi.class);
    }

    public ParkingApi getParkingApi() {
        return apiRetrofit.create(ParkingApi.class);
    }

    public TransportApi getTransportApi() {
        return apiRetrofit.create(TransportApi.class);
    }

    public RentApi getRentApi() {
        return apiRetrofit.create(RentApi.class);
    }

    public TripApi getTripApi() {
        return apiRetrofit.create(TripApi.class);
    }

    public PriceApi getPriceApi() {
        return apiRetrofit.create(PriceApi.class);
    }

    public RoutePointApi getRoutePointApi() {
        return apiRetrofit.create(RoutePointApi.class);
    }
}
