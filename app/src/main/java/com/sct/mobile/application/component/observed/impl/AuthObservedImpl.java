package com.sct.mobile.application.component.observed.impl;


import androidx.annotation.NonNull;

import com.sct.mobile.application.client.AuthApi;
import com.sct.mobile.application.component.observed.Observed;
import com.sct.mobile.application.component.subscriber.AuthSubscriber;
import com.sct.mobile.application.data.SharedDataUtil;
import com.sct.mobile.application.model.dto.JwtDto;
import com.sct.mobile.application.model.enums.SharedName;
import com.sct.mobile.application.service.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthObservedImpl implements Observed<AuthSubscriber> {

    private final AuthApi authApi = NetworkService.getInstance().getAuthApi();

    private AuthSubscriber subscriber;

    @Override
    public void subscribe(AuthSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void unSubscribe() {
        this.subscriber = null;
    }

    public void authorization(String login, String password) {
        authApi.authorization(login, password).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JwtDto> call, @NonNull Response<JwtDto> response) {
                if (response.code() != 200) {
                    error(response.message());
                    return;
                }
                SharedDataUtil.setString(SharedName.LOGIN.getLabel(), login);
                SharedDataUtil.setString(SharedName.PASSWORD.getLabel(), password);
                ok(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<JwtDto> call, @NonNull Throwable t) {
                error(t);
            }
        });
    }

    public void authentication() {
        authApi.authentication().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JwtDto> call, @NonNull Response<JwtDto> response) {
                if (SharedDataUtil.getString(SharedName.COOKIE.getLabel()).equals("")) {
                    SharedDataUtil.setString(
                            SharedName.COOKIE.getLabel(),
                            response.headers().get("Set-Cookie"));
                }
                if (response.code() != 200) error(response.message());
                else ok(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<JwtDto> call, @NonNull Throwable t) {
                error(t);
            }
        });
    }

    private void ok(JwtDto jwt) {
        subscriber.acceptAuth(jwt);
    }

    private void error(Throwable t) {
        t.printStackTrace();
        subscriber.errorAuth(t.getMessage());
    }

    private void error(String message) {
        subscriber.errorAuth(message);
    }
}
