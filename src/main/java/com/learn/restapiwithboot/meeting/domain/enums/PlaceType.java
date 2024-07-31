package com.learn.restapiwithboot.meeting.domain.enums;

import com.learn.restapiwithboot.account.domain.enums.Gender;

import java.util.Arrays;

public enum PlaceType {

    CAFE(1),
    RESTAURANT(2),
    COMPANY(3);

    private Integer value;

    PlaceType(Integer value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PlaceType getByValue(int inputValue) {
        if (inputValue == 0) {
            throw new IllegalArgumentException("inputValue is null");
        }

        return Arrays.stream(PlaceType.values())
                .filter(place -> place.getValue() == inputValue)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Unknown place value: " + inputValue));
    }
}
