package com.sct.mobile.application.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SharedName {

    COOKIE("cookie"),
    LOGIN("login"),
    PASSWORD("password");

    private final String label;
}
