package com.learn.restapiwithboot.meeting.domain.embed;

import com.learn.restapiwithboot.meeting.domain.enums.PlaceType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter @NoArgsConstructor
@Embeddable
public class Place {

    private String name;

    @Enumerated(EnumType.STRING)
    private PlaceType palceType;

    @Builder
    public Place(String name, PlaceType palceType) {
        this.name = name;
        this.palceType = palceType;
    }
}
