package com.sct.mobile.application.service.subscriber;

public interface TokenSubscriber {

    void acceptToken();

    void errorToken(String error);
}
