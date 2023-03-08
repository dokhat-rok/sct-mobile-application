package com.sct.mobile.application.client;

import com.sct.mobile.application.model.dto.UserDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserApi {

    @GET("/api/v1/customer/current")
    Call<UserDto> getCurrent(@Header("Authorization") String token);
}
