package com.learn.restapiwithboot.meeting.mapper;

import com.learn.restapiwithboot.meeting.domain.embed.Place;
import com.learn.restapiwithboot.meeting.dto.request.PlaceRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface PlaceMapper {

    Place placeRequestToPlace(PlaceRequest placeRequest);

    PlaceRequest placeToPlaceRequest(Place place);
}
