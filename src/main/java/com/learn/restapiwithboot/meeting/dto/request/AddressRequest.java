package com.learn.restapiwithboot.meeting.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AddressRequest {
    @NotNull(message = "도로명을 입력해주세요.")
    private String roadName;

    @NotNull(message = "도시를 입력해주세요.")
    private String city;

    @NotNull(message = "주를 입력해주세요.")
    private String state;

    @NotNull(message = "우편번호를 입력해주세요.")
    private String postalCode;

    @Builder
    public AddressRequest(String roadName, String city, String state, String postalCode) {
        this.roadName = roadName;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }
}
