package com.sct.mobile.application.client;

import com.sct.mobile.application.model.dto.RoutePointDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RoutePointApi {

    @POST("/api/v1/route/point")
    Call<Void> savePoint(@Header("Authorization") String token, @Body RoutePointDto routePoint);
}
