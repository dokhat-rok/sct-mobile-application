package com.sct.mobile.application.component.observed.impl;


import androidx.annotation.NonNull;

import com.sct.mobile.application.component.observed.AuthObserved;
import com.sct.mobile.application.component.subscriber.AuthSubscriber;
import com.sct.mobile.application.client.AuthApi;
import com.sct.mobile.application.config.NetworkService;
import com.sct.mobile.application.model.dto.JwtDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthObservedImpl implements AuthObserved {

    private final AuthApi authApi = NetworkService.getInstance().getAuthApi();

    private AuthSubscriber subscriber;

    @Override
    public void subscribeAuth(AuthSubscriber subscriber){
         this.subscriber = subscriber;
    }

    @Override
    public void unSubscribeAuth(){
        this.subscriber = null;
    }

    public void auth(String login, String password){
        authApi.authorization(login, password).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JwtDto> call, @NonNull Response<JwtDto> response) {
                JwtDto jwt = response.body();
                if (jwt == null){
                    subscriber.error("Ошибка авторизации");
                }
                else if(jwt.getToken() == null){
                    subscriber.error("Ошибка авторизации");
                }
                else {
                    subscriber.accept(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JwtDto> call, @NonNull Throwable t) {
                t.printStackTrace();
                subscriber.error(t.getMessage());
            }
        });
    }
}
