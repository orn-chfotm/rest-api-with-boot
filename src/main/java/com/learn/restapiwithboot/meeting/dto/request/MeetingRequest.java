package com.learn.restapiwithboot.meeting.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @NoArgsConstructor
public class MeetingRequest {

    @NotNull(message = "제목을 입력해주세요.")
    private String title;

    @NotNull(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "설명을 입력해주세요.")
    private String description;

    @NotNull(message = "회비를 입력해주세요.")
    @Min(value = 0, message = "회비는 0원 이상이어야 합니다.")
    private Integer dues;

    @NotNull(message = "모임 타입을 입력해주세요.")
    private String meetingType;

    @NotNull(message = "장소를 입력해주세요.")
    private PlaceRequest place;

    @Builder
    public MeetingRequest(String title, String content, String description, Integer dues, String meetingType, PlaceRequest place) {
        this.title = title;
        this.content = content;
        this.description = description;
        this.dues = dues;
        this.meetingType = meetingType;
        this.place = place;
    }
}
