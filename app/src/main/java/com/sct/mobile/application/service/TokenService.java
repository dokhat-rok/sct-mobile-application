package com.sct.mobile.application.service;

import com.sct.mobile.application.component.observed.impl.AuthObservedImpl;
import com.sct.mobile.application.component.subscriber.AuthSubscriber;
import com.sct.mobile.application.model.dto.JwtDto;
import com.sct.mobile.application.service.observed.TokenObserved;
import com.sct.mobile.application.service.subscriber.TokenSubscriber;

public class TokenService implements AuthSubscriber, TokenObserved {

    private final AuthObservedImpl authObserved;

    private TokenSubscriber subscriber;

    private static TokenService instance;

    private static JwtDto jwt;

    private TokenService(){
        authObserved = new AuthObservedImpl();
        authObserved.subscribeAuth(this);
    }

    public static TokenService getInstance(){
        if(instance == null) instance = new TokenService();
        return instance;
    }

    public static JwtDto getJwt(){
        return jwt;
    }

    public static void setJwt(JwtDto jwtDto) {
        jwt = jwtDto;
    }

    public static void deleteJwt(){
        jwt = null;
    }

    public void authentication(){
        authObserved.authentication();
    }

    @Override
    public void acceptAuth(JwtDto token) {
        jwt = token;
        subscriber.acceptToken();
    }

    @Override
    public void errorAuth(String error) {
        subscriber.errorToken(error);
    }

    @Override
    public void subscribeToken(TokenSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void unSubscribeToken() {
        this.subscriber = null;
    }
}
