package com.sct.mobile.application.component.observed.impl;

import androidx.annotation.NonNull;

import com.sct.mobile.application.client.AuthApi;
import com.sct.mobile.application.component.observed.RegistrationObserved;
import com.sct.mobile.application.component.subscriber.RegistrationSubscriber;
import com.sct.mobile.application.config.NetworkService;
import com.sct.mobile.application.model.dto.UserDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationObservedImpl implements RegistrationObserved {

    private final AuthApi authApi = NetworkService.getInstance().getAuthApi();

    private RegistrationSubscriber subscriber;

    @Override
    public void subscribeRegistration(RegistrationSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void unSubscribeRegistration() {
        this.subscriber = null;
    }

    public void registration(String login, String pass, String confPass){
        authApi.registration(login, pass, confPass).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<UserDto> call,
                    @NonNull Response<UserDto> response) {
                UserDto user = response.body();
                if(user == null) subscriber.errorRegistration("Ошибка регистрации");
                else subscriber.acceptRegistration(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<UserDto> call, @NonNull Throwable t) {
                t.printStackTrace();
                subscriber.errorRegistration(t.getMessage());
            }
        });
    }
}
