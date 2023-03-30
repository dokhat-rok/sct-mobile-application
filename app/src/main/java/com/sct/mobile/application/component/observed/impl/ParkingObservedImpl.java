package com.sct.mobile.application.component.observed.impl;

import androidx.annotation.NonNull;

import com.sct.mobile.application.client.ParkingApi;
import com.sct.mobile.application.component.observed.Observed;
import com.sct.mobile.application.component.subscriber.ParkingSubscriber;
import com.sct.mobile.application.model.dto.ParkingDto;
import com.sct.mobile.application.service.NetworkService;
import com.sct.mobile.application.service.TokenService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingObservedImpl implements Observed<ParkingSubscriber> {

    private ParkingSubscriber parkingSubscriber;

    private final ParkingApi parkingApi = NetworkService.getInstance().getParkingApi();

    public void getAllParking(){
        parkingApi.getAllParking(TokenService.getJwt().getToken()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<ParkingDto>> call,
                                   @NonNull Response<List<ParkingDto>> response) {
                if(response.code() != 200) parkingSubscriber.errorGetAllParking(response.message());
                else parkingSubscriber.acceptGetAllParking(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<ParkingDto>> call, @NonNull Throwable t) {
                t.printStackTrace();
                parkingSubscriber.errorGetAllParking(t.getMessage());
            }
        });
    }

    @Override
    public void subscribe(ParkingSubscriber subscriber) {
        this.parkingSubscriber = subscriber;
    }

    @Override
    public void unSubscribe() {
        parkingSubscriber = null;
    }
}
