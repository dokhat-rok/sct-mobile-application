package com.sct.mobile.application.client;

import com.sct.mobile.application.model.dto.TransportDto;
import com.sct.mobile.application.model.enums.TransportStatus;
import com.sct.mobile.application.model.enums.TransportType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TransportApi {

    @GET("/api/v1/transport/all/filter")
    Call<List<TransportDto>> getAllTransports(@Header("Authorization") String token,
                                              @Query("type")TransportType type,
                                              @Query("status")TransportStatus status);
}
