package com.telerikacademy.testframework.utils;

import lombok.Getter;

@Getter
public enum Visibility {

    PRIVATE("Private"),
    PUBLIC("Public");

    private final String stringValue;

    Visibility(String stringValue) {
        this.stringValue = stringValue;
    }
}
