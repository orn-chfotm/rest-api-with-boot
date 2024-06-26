package com.learn.restapiwithboot.meeting.domain.embed;

import com.learn.restapiwithboot.meeting.domain.enums.PlaceType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter @NoArgsConstructor
@Embeddable
public class Place {

    @Comment("장소명")
    private String name;

    @Comment("장소 타입")
    @Enumerated(EnumType.STRING)
    private PlaceType palceType;

    @Comment("모임 장소 주소")
    @Embedded
    private Address address;

    @Builder
    public Place(String name, PlaceType palceType, Address address) {
        this.name = name;
        this.palceType = palceType;
        this.address = address;
    }
}
