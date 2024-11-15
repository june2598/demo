package com.kh.demo.web.form.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString

//세터가 없음 = 불변객체, 변경할방법이없음
public class LoginMember {
  private Long memberId;        //내부 관리용 멤버 아이디
  private String email;         //회원 로그인 아이디
  private String nickname;      //회원 별칭
  private String gubun;         //일반,vip,관리자,
}
