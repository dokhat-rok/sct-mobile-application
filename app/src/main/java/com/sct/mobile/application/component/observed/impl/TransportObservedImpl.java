package com.sct.mobile.application.component.observed.impl;

import androidx.annotation.NonNull;

import com.sct.mobile.application.client.TransportApi;
import com.sct.mobile.application.component.observed.Observed;
import com.sct.mobile.application.component.subscriber.TransportSubscriber;
import com.sct.mobile.application.model.dto.TransportDto;
import com.sct.mobile.application.model.enums.TransportStatus;
import com.sct.mobile.application.model.enums.TransportType;
import com.sct.mobile.application.service.NetworkService;
import com.sct.mobile.application.service.TokenService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportObservedImpl implements Observed<TransportSubscriber> {

    private TransportSubscriber transportSubscriber;

    private final TransportApi transportApi = NetworkService.getInstance().getTransportApi();

    public void getAllTransport(TransportType type){
        transportApi.getAllTransports(TokenService.getJwt().getToken(), type, TransportStatus.FREE)
                .enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<TransportDto>> call,
                                   @NonNull Response<List<TransportDto>> response) {
                if(response.code() != 200)
                    transportSubscriber.errorGetAllTransport(response.message());
                else transportSubscriber.acceptGetAllTransport(type, response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<TransportDto>> call, @NonNull Throwable t) {
                t.printStackTrace();
                transportSubscriber.errorGetAllTransport(t.getMessage());
            }
        });
    }

    @Override
    public void subscribe(TransportSubscriber subscriber) {
        this.transportSubscriber = subscriber;
    }

    @Override
    public void unSubscribe() {
        this.transportSubscriber = null;
    }
}
