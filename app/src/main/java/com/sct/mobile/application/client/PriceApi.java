package com.sct.mobile.application.client;

import com.sct.mobile.application.model.dto.PriceDto;
import com.sct.mobile.application.model.enums.TransportType;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface PriceApi {

    @GET("/api/v1/price/{type}")
    Call<PriceDto> getPrice(@Header("Authorization") String token,
                            @Path("type") TransportType type);
}
