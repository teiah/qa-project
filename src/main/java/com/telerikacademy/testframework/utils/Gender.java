package com.telerikacademy.testframework.utils;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE");

    private String stringValue;
    Gender(String stringValue) {
        this.stringValue = stringValue;
    }

}
