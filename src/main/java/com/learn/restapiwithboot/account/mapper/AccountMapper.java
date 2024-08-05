package com.learn.restapiwithboot.account.mapper;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.account.dto.request.AccountRequest;
import com.learn.restapiwithboot.account.dto.request.AccountUpdateReqeust;
import com.learn.restapiwithboot.account.dto.response.AccountResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "gender", expression = "java(com.learn.restapiwithboot.account.domain.enums.Gender.getByValue(accountRequest.getGender()))")
    @Mapping(target = "role", ignore = true)
    Account accountRequestToAccount(AccountRequest accountRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Account accountUpdateRequestToAccount(AccountUpdateReqeust accountUpdateReqeust, @MappingTarget Account account);

    @Mapping(target = "gender", expression = "java(account.getGender().getValue())")
    AccountResponse accountToAccountResponse(Account account);
}
