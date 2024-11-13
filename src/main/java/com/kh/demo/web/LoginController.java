package com.kh.demo.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class LoginController {
  //로그인화면
  @GetMapping("/login")             //http://localhost:9080/login
  public String loginForm(){
    return "/login/loginForm";
  }

  //로그인처리

  //로그아웃처리




}
