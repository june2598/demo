package com.kh.demo.web;

import com.kh.demo.domain.common.Code;
import com.kh.demo.domain.common.svc.CodeSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice     // 모든 컨트롤러 요청에 공통으로 처리되는 메소드 위치
@RequiredArgsConstructor
public class GlobalControllerAdvice {

  private final CodeSVC codeSVC;
  //취미

  @ModelAttribute("codes")   //view에서 codes라는 참조변수로 참조, 매개값 : 뷰에서 참고할 이름
  public List<Code> getHobbies(){

    List<com.kh.demo.domain.common.Code> codes = new ArrayList<>();
    codes.add(new Code("독서","독서"));
    codes.add(new Code("음악감상","음악감상"));
    codes.add(new Code("운동","운동"));
    codes.add(new Code("게임","게임"));
    return codes;
  }
  //지역
  @ModelAttribute("regions")   // view에서 regions라는 참조변수로 참조. 매개값 : 뷰에서 참고할 이름
  public List<Code> getRegions(){
    return codeSVC.getA02();
  }
}
