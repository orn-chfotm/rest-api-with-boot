package com.learn.restapiwithboot.meeting.mapper;

import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.meeting.dto.request.MeetingRequest;
import com.learn.restapiwithboot.meeting.dto.response.MeetingResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PlaceMapper.class, AddressMapper.class})
public interface MeetingMapper {

    @Mapping(target = "reservedMember", ignore = true)
    Meeting meetingRequestToMeeting(MeetingRequest meetingRequest);

    @Mapping(source = "account.email", target = "regEmail")
    MeetingResponse meetingToMeetingResponse(Meeting meeting);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "account", ignore = true)
    void updateMeetingFromRequest(MeetingRequest meetingRequest, @MappingTarget Meeting meeting);
}
