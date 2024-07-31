package com.learn.restapiwithboot.meeting.domain.embed;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.Embeddable;

@Getter @NoArgsConstructor
@Embeddable
public class Address {

    @Comment("도시")
    private String city;

    @Comment("주")
    private String state;

    @Comment("도로명")
    private String roadName;

    @Comment("우편번호")
    private String postalCode;

    @Builder
    public Address(String roadName, String city, String state, String postalCode) {
        this.roadName = roadName;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }
}
