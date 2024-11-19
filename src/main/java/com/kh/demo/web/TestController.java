package com.kh.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {

  @GetMapping("/1")
  public String test1(Model model){
    List<String> items = new ArrayList<>();
    items.add("홍길동1");
    items.add("홍길동2");
    items.add("홍길동3");
    model.addAttribute("members", items);
    return "/test/test1"; // thymeleaf 템플림 이름
  }

  @GetMapping("/2")         //get http://localhost:9080/test/2?a=1&b=2
  public String test2(){

    return "/test/test2";
  }

  @GetMapping("/3/{a}/{b}")
  public String test3(@PathVariable("a") String a, @PathVariable("b") String b, Model model){

    model.addAttribute("a",a);
    model.addAttribute("b",b);

    return "/test/test2";
  }

  @PostMapping("/2")
  public String test4() {


    return "/test/test2";
  }
}
