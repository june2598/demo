package com.kh.demo.web;

import com.kh.demo.domain.entity.Member;
import com.kh.demo.domain.member.svc.MemberSVC;
import com.kh.demo.web.form.member.JoinForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
  private final MemberSVC memberSVC;

  //가입화면
  @GetMapping("/join")
  public String joinForm(Model model){
    model.addAttribute("joinForm", new JoinForm());

    return "/member/joinForm";
  }


  //가입처리
  @PostMapping("/join")
  public String join(@ModelAttribute JoinForm joinForm){

    log.info("joinForm={}", joinForm);

    Member member = new Member();
    BeanUtils.copyProperties(joinForm,member);



    Member joinedMember = memberSVC.join(member);

    return "redirect:/login";
  }

}