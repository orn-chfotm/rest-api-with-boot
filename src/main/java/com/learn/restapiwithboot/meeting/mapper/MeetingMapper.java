package com.learn.restapiwithboot.meeting.mapper;

import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.meeting.dto.request.MeetingRequest;
import com.learn.restapiwithboot.meeting.dto.response.MeetingResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {PlaceMapper.class, AddressMapper.class})
public interface MeetingMapper {
    MeetingMapper INSTANCE = Mappers.getMapper(MeetingMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDues", ignore = true)
    Meeting meetingReqeustToMeeting(MeetingRequest meetingRequest);

    MeetingResponse meetingToMeetingResponse(Meeting meeting);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDues", ignore = true)
    void updateMeetingFromRequest(MeetingRequest meetingRequest, @MappingTarget Meeting meeting);
}
