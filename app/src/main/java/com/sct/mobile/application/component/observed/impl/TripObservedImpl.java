package com.sct.mobile.application.component.observed.impl;

import androidx.annotation.NonNull;

import com.sct.mobile.application.client.TripApi;
import com.sct.mobile.application.component.observed.Observed;
import com.sct.mobile.application.component.subscriber.TripSubscriber;
import com.sct.mobile.application.model.dto.RentDto;
import com.sct.mobile.application.model.dto.TripBeginDto;
import com.sct.mobile.application.model.dto.TripEndDto;
import com.sct.mobile.application.service.NetworkService;
import com.sct.mobile.application.service.TokenService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripObservedImpl implements Observed<TripSubscriber> {

    private TripSubscriber tripSubscriber;

    private final TripApi tripApi = NetworkService.getInstance().getTripApi();

    public void beginTrip(TripBeginDto tripBegin){
        tripApi.begin(TokenService.getJwt().getToken(), tripBegin).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RentDto> call,
                                   @NonNull Response<RentDto> response) {
                if(response.code() != 200) tripSubscriber.errorBeginTrip(response.message());
                else tripSubscriber.acceptBeginTrip(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RentDto> call, @NonNull Throwable t) {
                t.printStackTrace();
                tripSubscriber.errorBeginTrip(t.getMessage());
            }
        });
    }

    public void endTrip(TripEndDto tripEnd){
        tripApi.end(TokenService.getJwt().getToken(), tripEnd).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RentDto> call,
                                   @NonNull Response<RentDto> response) {
                if(response.code() != 200) tripSubscriber.errorEndTrip(response.message());
                else tripSubscriber.acceptEndTrip(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RentDto> call, @NonNull Throwable t) {
                t.printStackTrace();
                tripSubscriber.errorEndTrip(t.getMessage());
            }
        });
    }

    @Override
    public void subscribe(TripSubscriber subscriber) {
        this.tripSubscriber = subscriber;
    }

    @Override
    public void unSubscribe() {
        this.tripSubscriber = null;
    }
}
