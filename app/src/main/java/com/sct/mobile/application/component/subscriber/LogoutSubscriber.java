package com.sct.mobile.application.component.subscriber;

public interface LogoutSubscriber extends Subscriber {

    void acceptLogout();

    void errorLogout(String error);
}
