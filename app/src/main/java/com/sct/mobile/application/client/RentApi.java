package com.sct.mobile.application.client;

import com.sct.mobile.application.model.dto.RentDto;
import com.sct.mobile.application.model.enums.RentStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface RentApi {

    @GET("/api/v1/rent/current/all")
    Call<List<RentDto>> getAllRent(@Header("Authorization") String token,
                                   @Query("status") RentStatus status);
}
