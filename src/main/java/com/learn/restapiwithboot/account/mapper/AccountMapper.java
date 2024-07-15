package com.learn.restapiwithboot.account.mapper;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.account.dto.request.AccountRequest;
import com.learn.restapiwithboot.account.dto.response.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account accountToAccountResponse(AccountRequest accountRequest);

    AccountResponse accountToAccountResponse(Account account);
}
