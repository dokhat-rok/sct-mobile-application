package com.sct.mobile.application.client;

import com.sct.mobile.application.model.dto.JwtDto;
import com.sct.mobile.application.model.dto.UserDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {

    @POST("/sct_front_auth_war/mobile/auth")
    Call<UserDto> registration(
            @Query("login") String login,
            @Query("password") String password,
            @Query("confirmPassword") String confirmPass);

    @GET("/sct_front_auth_war/mobile/auth")
    Call<JwtDto> authorization(@Query("login") String login, @Query("password") String password);
}
