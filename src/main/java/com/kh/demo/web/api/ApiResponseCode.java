package com.kh.demo.web.api;

import java.util.Arrays;

public enum ApiResponseCode {
  // 성공 응답
  SUCCESS("S00", "Success"),

  // 공통 예외
  VALIDATION_ERROR("E01", "Validation error occurred"),
  BUSINESS_ERROR("E02", "Business error occurred"),
  ENTITY_NOT_FOUND("E03", "Entity not found"),

  // 사용자 관련 예외
  USER_NOT_FOUND("U01", "User not found"),
  USER_ALREADY_EXISTS("U02", "User already exists"),
  INVALID_PASSWORD("U03", "Invalid password"),

  // 시스템 예외
  INTERNAL_SERVER_ERROR("999","Internal server error");

  private final String rtcd;
  private final String rtmsg;

  ApiResponseCode(String rtcd, String rtmsg) {
    this.rtcd = rtcd;
    this.rtmsg = rtmsg;
  }

  public String getRtcd() {
    return rtcd;
  }

  public String getRtmsg() {
    return rtmsg;
  }

  // 코드로 enum 조회
  public static ApiResponseCode of(String code) {
    // stream : js의 고차함수와 유사?
    return Arrays.stream(values())
        //상수객체를  하나씩 가져와서, 매개값 (code)와 같은것만 필터링
        .filter(rc -> rc.getRtcd().equals(code))
        //걸러진 배열중에 첫번째를 가져옴
        .findFirst()
        .orElse(INTERNAL_SERVER_ERROR); //없으면 내부오류 처리
  }

  //응답 생성 헬퍼 메소드
  public <T> ApiResponse<T> toResponse(T data){
    return ApiResponse.createApiResponse(this, data );
  }

  //상세

}
