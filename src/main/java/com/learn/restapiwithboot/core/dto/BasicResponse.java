package com.learn.restapiwithboot.core.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public abstract class BasicResponse {

   private final Integer statusCode;
   private final String message;

}
