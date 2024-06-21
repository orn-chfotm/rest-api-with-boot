package com.learn.restapiwithboot.meeting.dto.response;

import lombok.*;

@Data
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class MeetingResponseDto {

    private Long id;

    private String title;

    private String content;

    private String description;
}
