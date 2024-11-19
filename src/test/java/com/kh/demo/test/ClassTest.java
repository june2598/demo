package com.kh.demo.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ClassTest {
  @Test
  void t1(){
    Object obj = new Person("홍길동",20);
    Person p = (Person) obj;

    log.info("{}",obj.getClass().getName());
    log.info("{}",obj.getClass().getSimpleName());
    log.info("{}",obj.getClass());
  }

  @Getter
  @AllArgsConstructor
  @ToString
  static class Person{
    private String name;
    private int age;
  }
}
