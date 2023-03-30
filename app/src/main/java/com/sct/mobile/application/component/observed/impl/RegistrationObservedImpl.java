package com.sct.mobile.application.component.observed.impl;

import androidx.annotation.NonNull;

import com.sct.mobile.application.client.AuthApi;
import com.sct.mobile.application.component.observed.Observed;
import com.sct.mobile.application.component.subscriber.RegistrationSubscriber;
import com.sct.mobile.application.model.dto.UserDto;
import com.sct.mobile.application.service.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationObservedImpl implements Observed<RegistrationSubscriber> {

    private final AuthApi authApi = NetworkService.getInstance().getAuthApi();

    private RegistrationSubscriber subscriber;

    @Override
    public void subscribe(RegistrationSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void unSubscribe() {
        this.subscriber = null;
    }

    public void registration(String login, String pass, String confPass) {
        authApi.registration(login, pass, confPass).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<UserDto> call,
                    @NonNull Response<UserDto> response) {
                UserDto user = response.body();
                if (response.code() != 200) subscriber.errorRegistration(response.message());
                else if (user == null) subscriber.errorRegistration("Ошибка регистрации");
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
