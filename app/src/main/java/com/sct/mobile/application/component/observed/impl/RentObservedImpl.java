package com.sct.mobile.application.component.observed.impl;

import androidx.annotation.NonNull;

import com.sct.mobile.application.client.RentApi;
import com.sct.mobile.application.component.observed.Observed;
import com.sct.mobile.application.component.subscriber.RentSubscriber;
import com.sct.mobile.application.model.dto.RentDto;
import com.sct.mobile.application.model.enums.RentStatus;
import com.sct.mobile.application.service.NetworkService;
import com.sct.mobile.application.service.TokenService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentObservedImpl implements Observed<RentSubscriber> {

    private RentSubscriber rentSubscriber;

    private final RentApi rentApi = NetworkService.getInstance().getRentApi();

    public void getAllRent(RentStatus status){
        rentApi.getAllRent(TokenService.getJwt().getToken(), status).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<RentDto>> call,
                                   @NonNull Response<List<RentDto>> response) {
                if(response.code() != 200) rentSubscriber.errorGetAllRent(response.message());
                else rentSubscriber.acceptGetAllRent(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<RentDto>> call, @NonNull Throwable t) {
                t.printStackTrace();
                throw new RuntimeException(t);
//                rentSubscriber.errorGetAllRent(t.getMessage());
            }
        });
    }

    @Override
    public void subscribe(RentSubscriber subscriber) {
        this.rentSubscriber = subscriber;
    }

    @Override
    public void unSubscribe() {
        this.rentSubscriber = null;
    }
}
