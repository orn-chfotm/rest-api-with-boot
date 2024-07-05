package com.learn.restapiwithboot.meeting.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
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

    @NotNull(message = "최대 인원을 입력해주세요.")
    private Integer maxMember;

    @NotNull(message = "모임 날짜를 입력해주세요.")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime meetingDate;

    @Builder
    public MeetingRequest(String title, String content, String description, Integer dues, String meetingType, PlaceRequest place, Integer maxMember, LocalDateTime meetingDate) {
        this.title = title;
        this.content = content;
        this.description = description;
        this.dues = dues;
        this.meetingType = meetingType;
        this.place = place;
        this.maxMember = maxMember;
        this.meetingDate = meetingDate;
    }
}
