package com.sct.mobile.application.component.observed;

import com.sct.mobile.application.component.subscriber.RegistrationSubscriber;

public interface RegistrationObserved {

    void subscribeRegistration(RegistrationSubscriber subscriber);

    void unSubscribeRegistration();
}
