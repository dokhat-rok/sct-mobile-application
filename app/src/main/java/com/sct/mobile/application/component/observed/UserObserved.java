package com.sct.mobile.application.component.observed;

import com.sct.mobile.application.component.subscriber.UserSubscriber;

public interface UserObserved {

    void subscribeUser(UserSubscriber userSubscriber);

    void unSubscribeUser();
}
