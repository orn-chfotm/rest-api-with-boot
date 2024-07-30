package com.learn.restapiwithboot.account.domain.enums;

import java.util.Arrays;

public enum Gender {
    MALE("M"), FEMALE("F");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Gender getName(String inputValue) {
        if (inputValue == null) {
            throw new IllegalArgumentException("inputValue is null");
        }

        return Arrays.stream(Gender.values())
                .filter(gender -> gender.getValue().equalsIgnoreCase(inputValue.trim()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Unknown gender: " + inputValue));
    }

}
