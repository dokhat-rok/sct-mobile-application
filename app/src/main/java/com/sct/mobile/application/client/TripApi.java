package com.sct.mobile.application.client;

import com.sct.mobile.application.model.dto.RentDto;
import com.sct.mobile.application.model.dto.TripBeginDto;
import com.sct.mobile.application.model.dto.TripEndDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface TripApi {

    @POST("/api/v1/trip/begin")
    Call<RentDto> begin(@Header("Authorization") String token, @Body TripBeginDto tripBegin);

    @PUT("/api/v1/trip/end")
    Call<RentDto> end(@Header("Authorization") String token, @Body TripEndDto tripEnd);
}
