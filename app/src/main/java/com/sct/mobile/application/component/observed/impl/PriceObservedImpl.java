package com.sct.mobile.application.component.observed.impl;

import androidx.annotation.NonNull;

import com.sct.mobile.application.client.PriceApi;
import com.sct.mobile.application.component.observed.Observed;
import com.sct.mobile.application.component.subscriber.PriceSubscriber;
import com.sct.mobile.application.model.dto.PriceDto;
import com.sct.mobile.application.model.enums.TransportType;
import com.sct.mobile.application.service.NetworkService;
import com.sct.mobile.application.service.TokenService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriceObservedImpl implements Observed<PriceSubscriber> {

    private PriceSubscriber priceSubscriber;

    private final PriceApi priceApi = NetworkService.getInstance().getPriceApe();

    public void getPrice(TransportType type) {
        priceApi.getPrice(TokenService.getJwt().getToken(), type).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PriceDto> call,
                                   @NonNull Response<PriceDto> response) {
                if(response.code() != 200) priceSubscriber.errorPrice(response.message());
                priceSubscriber.acceptPrice(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<PriceDto> call, @NonNull Throwable t) {
                t.printStackTrace();
                priceSubscriber.errorPrice(t.getMessage());
            }
        });
    }

    @Override
    public void subscribe(PriceSubscriber subscriber) {
        this.priceSubscriber = subscriber;
    }

    @Override
    public void unSubscribe() {
        this.priceSubscriber = null;
    }
}
