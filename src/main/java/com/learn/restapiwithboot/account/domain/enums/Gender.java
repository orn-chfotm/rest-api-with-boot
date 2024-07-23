package com.learn.restapiwithboot.account.domain.enums;

import com.learn.restapiwithboot.core.enums.EnumInterface;

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

        if(inputValue == null) {
            throw new IllegalArgumentException("inputValue is null");
        }

        inputValue = inputValue.trim();

        for (Gender gender : Gender.values()) {
            if (gender.getValue().equalsIgnoreCase(inputValue)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown gender: " + inputValue);
    }

}
