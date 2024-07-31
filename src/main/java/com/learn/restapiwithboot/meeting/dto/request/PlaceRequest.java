package com.learn.restapiwithboot.meeting.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PlaceRequest {

    @NotNull(message = "장소 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "장소 타입을 입력해주세요.")
    private String placeType;

    private AddressRequest address;

    @Builder
    public PlaceRequest(String name, String placeType, AddressRequest address) {
        this.name = name;
        this.placeType = placeType;
        this.address = address;
    }
}
