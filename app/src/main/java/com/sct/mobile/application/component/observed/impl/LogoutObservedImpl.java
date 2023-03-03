package com.sct.mobile.application.component.observed.impl;

import androidx.annotation.NonNull;

import com.sct.mobile.application.client.AuthApi;
import com.sct.mobile.application.component.observed.LogoutObserved;
import com.sct.mobile.application.component.subscriber.LogoutSubscriber;
import com.sct.mobile.application.service.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogoutObservedImpl implements LogoutObserved {

    private LogoutSubscriber subscriber;

    private final AuthApi authApi = NetworkService.getInstance().getAuthApi();

    public void logout(){
        authApi.logout().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                subscriber.acceptLogout();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                t.printStackTrace();
                subscriber.errorLogout(t.getMessage());
            }
        });
    }

    @Override
    public void subscribeLogout(LogoutSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void unSubscribeLogout() {
        this.subscriber = null;
    }
}
