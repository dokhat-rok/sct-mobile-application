package com.sct.mobile.application.component.observed;

import com.sct.mobile.application.component.subscriber.Subscriber;

public interface Observed<S extends Subscriber> {

    void subscribe(S subscriber);

    void unSubscribe();
}
