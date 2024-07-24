package com.learn.restapiwithboot.account.domain.enums;

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

        for (Gender gender : Gender.values()) {
            if (gender.getValue().equalsIgnoreCase(inputValue.trim())) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown gender: " + inputValue);
    }

}
