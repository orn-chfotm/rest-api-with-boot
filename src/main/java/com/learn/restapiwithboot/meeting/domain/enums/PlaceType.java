package com.learn.restapiwithboot.meeting.domain.enums;

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
}
