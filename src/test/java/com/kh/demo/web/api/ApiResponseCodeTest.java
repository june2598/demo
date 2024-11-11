package com.kh.demo.web.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ApiResponseCodeTest {

  @Test
  void of() {
    ApiResponseCode success = ApiResponseCode.of("S00");
    log.info("{}",success);

  }

  @Test
  void toResponse() {
    ApiResponse<Object> response = ApiResponseCode.SUCCESS.toResponse(null);
    log.info("response={}",response);
  }
}