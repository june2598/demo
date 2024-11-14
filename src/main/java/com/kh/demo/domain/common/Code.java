package com.kh.demo.domain.common;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor  //default 생성자 자동생성
public class Code {
  private String codeId;        //코드
  private String decode;      //디코드

}
