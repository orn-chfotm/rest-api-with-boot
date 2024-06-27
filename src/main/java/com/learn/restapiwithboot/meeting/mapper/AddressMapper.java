package com.learn.restapiwithboot.meeting.mapper;

import com.learn.restapiwithboot.meeting.domain.embed.Address;
import com.learn.restapiwithboot.meeting.dto.request.AddressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    Address addressRequestToAddress(AddressRequest address);

    AddressRequest addressToAddressRequest(Address address);
}
