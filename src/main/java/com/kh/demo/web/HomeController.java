package com.kh.demo.web;


import com.kh.demo.web.form.login.LoginMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller

public class HomeController {
  //static과 templates중에 templates가 우선순위가높음
  @GetMapping("/")
  public String home(HttpServletRequest request){
    String view = null;

    HttpSession session = request.getSession(false);
    //로그인전
    if(session == null || session.getAttribute("loginMember") == null){
      view = "beforeLogin";
    }else{
      //로그인후
      LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

      //관리자 - 일반회원 구분
//      if("M01A".equals(loginMember.getGubun().substring(0,4))){
//        view = "admin";
//      } else {
//        view = "afterLogin";
//      }

      view = "M01A".equals(loginMember.getGubun().substring(0,4)) ? "admin" : "afterLogin";
    }
    return view;
  }
}
