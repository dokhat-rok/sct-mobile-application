package com.sct.mobile.application.component.observed.impl;

import androidx.annotation.NonNull;

import com.sct.mobile.application.client.UserApi;
import com.sct.mobile.application.component.observed.UserObserved;
import com.sct.mobile.application.component.subscriber.UserSubscriber;
import com.sct.mobile.application.model.dto.UserDto;
import com.sct.mobile.application.service.NetworkService;
import com.sct.mobile.application.service.TokenService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserObservedImpl implements UserObserved {

    private UserSubscriber userSubscriber;

    private final UserApi userApi = NetworkService.getInstance().getUserApi();

    public void getCurrent(){
        userApi.getCurrent(TokenService.getJwt().getToken()).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<UserDto> call,
                    @NonNull Response<UserDto> response) {
                if(response.code() != 200) userSubscriber.errorUser(response.message());
                else userSubscriber.acceptCurrent(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<UserDto> call, @NonNull Throwable t) {
                t.printStackTrace();
                userSubscriber.errorUser(t.getMessage());
            }
        });
    }


    @Override
    public void subscribeUser(UserSubscriber userSubscriber) {
        this.userSubscriber = userSubscriber;
    }

    @Override
    public void unSubscribeUser() {
        this.userSubscriber = null;
    }
}
