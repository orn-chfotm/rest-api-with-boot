package com.learn.restapiwithboot.account.mapper;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.account.dto.request.AccountRequest;
import com.learn.restapiwithboot.account.dto.response.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "gender", expression = "java(com.learn.restapiwithboot.account.domain.enums.Gender.getName(accountRequest.getGender()))")
    @Mapping(target = "role", ignore = true)
    Account accountRequestToAccount(AccountRequest accountRequest);

    @Mapping(target = "gender", expression = "java(account.getGender().getValue())")
    AccountResponse accountToAccountResponse(Account account);
}
