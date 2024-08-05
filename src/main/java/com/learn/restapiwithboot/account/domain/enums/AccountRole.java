package com.learn.restapiwithboot.account.domain.enums;

import com.learn.restapiwithboot.core.exceptions.enums.ExceptionType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum AccountRole {

    SUPER_ADMIN("SUPERADMIN", "ADMIN"),
    ADMIN("ADMIN"),
    USER("USER", "ADMIN");

    private final List<String> value;

    AccountRole(String... value) {
        this.value = List.of(value);
    }

    public Set<String> getValue() {
        return new HashSet<>(value);
    }
}
