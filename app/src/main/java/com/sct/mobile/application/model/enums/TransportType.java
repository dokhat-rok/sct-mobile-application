package com.sct.mobile.application.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TransportType {

    BICYCLE("Велосипед"),

    SCOOTER("Самокат");

    @Getter
    private final String label;
}
