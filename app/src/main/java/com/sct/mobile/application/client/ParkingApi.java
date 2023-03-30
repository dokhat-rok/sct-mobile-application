package com.sct.mobile.application.client;

import com.sct.mobile.application.model.dto.ParkingDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ParkingApi {

    @GET("/api/v1/parking/all")
    Call<List<ParkingDto>> getAllParking(@Header("Authorization") String token);
}
