package com.kh.demo.domain.member;

import com.kh.demo.domain.entity.Member;
import com.kh.demo.domain.member.dao.MemberDAO;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Slf4j
class MemberDAOImplTest {

  @Autowired
  MemberDAO memberDAO;

  @Test
  @DisplayName("회원가입")
  void insertMember() {
    Member member = new Member();
    member.setEmail("test10@kh.com");
    member.setPasswd("1234");
    member.setTel("010-9999-9999");
    member.setNickname("별칭3");
    member.setGender("남자");
    member.setHobby("축구");
    member.setRegion("A0204");
//    member.setGubun(""); 값이 없을때의 기본값이 있음

    Member insertedMember = memberDAO.insertMember(member);
    log.info("insertedMember={}",insertedMember);
  }

  @Test
  @DisplayName("회원존재여부")
  void isExist() {
    boolean exist = memberDAO.isExist("test1@kh.com");
    Assertions.assertThat(exist).isEqualTo(true);

//    exist = memberDAO.isExist("test1@google.com");
//    Assertions.assertThat(exist).isEqualTo(true);
  }

  @Test
  @DisplayName("회원검색By회원아이디")
  void findByMemberId() {
    Optional<Member> optionalMember = memberDAO.findByMemberId(1L);
    if (optionalMember.isPresent()) {
      Member member = optionalMember.get();
      log.info("member={}", member);
    }
    optionalMember = memberDAO.findByMemberId(0L);
    if (!optionalMember.isPresent()) {
      log.info("회원없음");
    }
  }

  @Test
  @DisplayName("회원검색By이메일")
  void findByEmail() {
    Optional<Member> optionalMember = memberDAO.findByEmail("test1@kh.com");
    if (optionalMember.isPresent()) {
      Member member = optionalMember.get();
      log.info("member={}", member);
    }
    optionalMember = memberDAO.findByEmail("test1@google.com");
    if (!optionalMember.isPresent()) {
      log.info("회원없음");
    }

  }
}