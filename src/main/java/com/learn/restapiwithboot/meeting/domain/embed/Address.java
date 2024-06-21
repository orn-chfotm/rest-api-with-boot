package com.learn.restapiwithboot.meeting.domain.embed;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter @NoArgsConstructor
@Embeddable
public class Address {
    private String roadName;

    private String city;

    private String state;

    private String postalCode;

    @Builder
    public Address(String roadName, String city, String state, String postalCode) {
        this.roadName = roadName;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }
}
