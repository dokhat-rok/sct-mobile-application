package com.sct.mobile.application.component.subscriber;

public interface LogoutSubscriber {

    void acceptLogout();

    void errorLogout(String error);
}
