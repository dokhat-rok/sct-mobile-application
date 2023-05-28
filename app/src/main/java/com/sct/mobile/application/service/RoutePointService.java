package com.sct.mobile.application.service;

import androidx.annotation.NonNull;

import com.sct.mobile.application.client.RoutePointApi;
import com.sct.mobile.application.model.dto.RoutePointDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutePointService {

    private final RoutePointApi routePointApi = NetworkService.getInstance().getRoutePointApi();

    public void savePoint(RoutePointDto routePoint) {
        routePointApi.savePoint(TokenService.getJwt().getToken(), routePoint)
                .enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {}
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {}
        });
    }
}
