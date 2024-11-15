package com.kh.demo.web;

import com.kh.demo.domain.entity.Member;
import com.kh.demo.domain.member.dao.MemberDAO;
import com.kh.demo.web.form.login.LoginForm;
import com.kh.demo.web.form.login.LoginMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

  private final MemberDAO memberDAO;

  //로그인화면
  @GetMapping("/login")             //http://localhost:9080/login
  public String loginForm(Model model) {
    model.addAttribute("loginForm", new LoginForm()); //멤버값이 Null인 상태
    return "/login/loginForm";
  }

  //로그인처리
  @PostMapping("/login")
  public String login(@Valid LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {
    log.info("loginForm={}", loginForm);


    //1) 회원 존재유무 체크
    //표면상으로는 맞게들어왔지만 실제로는 문제가 있는케이스 : business 케이스
    if (!memberDAO.isExist(loginForm.getEmail())) {
      bindingResult.rejectValue("email", "invalidMember");
      return "/login/loginForm";
    }

    //2) 비밀번호 일치여부 체크
    Optional<Member> optionalMember = memberDAO.findByEmail(loginForm.getEmail());
    Member loginMember = optionalMember.get();
    log.info("loginMember={}", loginMember);

    if (!loginForm.getPasswd().equals(loginMember.getPasswd())) {
      bindingResult.rejectValue("passwd", "invalidMember");
      return "/login/loginForm";
    }
    //3) 로그인 세션반영

    //세션이 존재하면 해당 세션을 가져오고 없으면 신규생성
    HttpSession session = request.getSession(true);//true : 없으면 생성

    //4) 세션에 회원정보 저장
    LoginMember loginOkMember = new LoginMember(
        loginMember.getMemberId(),
        loginMember.getEmail(),
        loginMember.getNickname(),
        loginMember.getGubun());
    session.setAttribute("loginOkMember", loginOkMember);

    return "redirect:/";
  }

  //로그아웃처리
  @GetMapping("/logout")
  public String logout(HttpServletRequest request){
    //세션 정보 가져오기
    HttpSession session = request.getSession(false);
    //세션제거
    session.invalidate();
    return "redirect:/";
  }

}
