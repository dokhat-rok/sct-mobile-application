package com.sct.mobile.application.client;

import com.sct.mobile.application.model.dto.UserDto;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserApi {

    @GET("/api/v1/customer/current")
    Call<UserDto> getCurrent(@Header("Authorization") String token);

    @PUT("/api/v1/customer/current/additional")
    Call<UserDto> additionalBalance(
            @Header("Authorization") String token,
            @Query("amount") Long amount
    );

    @DELETE("/api/v1/customer/current")
    Call<Void> deleteCurrent(@Header("Authorization") String token);
}
