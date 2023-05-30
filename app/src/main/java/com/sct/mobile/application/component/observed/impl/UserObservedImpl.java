package com.sct.mobile.application.component.observed.impl;

import androidx.annotation.NonNull;

import com.sct.mobile.application.client.UserApi;
import com.sct.mobile.application.component.observed.Observed;
import com.sct.mobile.application.component.subscriber.UserSubscriber;
import com.sct.mobile.application.model.dto.UserDto;
import com.sct.mobile.application.service.NetworkService;
import com.sct.mobile.application.service.TokenService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserObservedImpl implements Observed<UserSubscriber> {

    private UserSubscriber userSubscriber;

    private final UserApi userApi = NetworkService.getInstance().getUserApi();

    public void getCurrent() {
        userApi.getCurrent(TokenService.getJwt().getToken()).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<UserDto> call,
                    @NonNull Response<UserDto> response
            ) {
                if (response.code() != 200) userSubscriber.errorUser(response.message());
                else userSubscriber.acceptCurrent(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<UserDto> call, @NonNull Throwable t) {
                t.printStackTrace();
                userSubscriber.errorUser(t.getMessage());
            }
        });
    }

    public void additionalBalance(Long amount) {
        userApi.additionalBalance(TokenService.getJwt().getToken(), amount)
                .enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<UserDto> call,
                    @NonNull Response<UserDto> response
            ) {
                if(response.code() != 200) userSubscriber.errorAdditionalBalance(response.message());
                else userSubscriber.acceptAdditionalBalance(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<UserDto> call, @NonNull Throwable t) {
                t.printStackTrace();
                userSubscriber.errorAdditionalBalance(t.getMessage());
            }
        });
    }

    public void deleteCurrent() {
        userApi.deleteCurrent(TokenService.getJwt().getToken()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() != 200) userSubscriber.errorDelete(response.message());
                else userSubscriber.acceptDelete();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                t.printStackTrace();
                userSubscriber.errorDelete(t.getMessage());
            }
        });
    }

    @Override
    public void subscribe(UserSubscriber userSubscriber) {
        this.userSubscriber = userSubscriber;
    }

    @Override
    public void unSubscribe() {
        this.userSubscriber = null;
    }
}
