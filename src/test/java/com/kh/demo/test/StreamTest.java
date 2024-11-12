package com.kh.demo.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class StreamTest {

  @Test
  void t1() {
    int[] arr = new int[]{1, 2, 3};

    //case1)
    for (int i = 0; i <arr.length; i++) {
      log.info("ele={}",arr[i]);
    }

    //case2)
    for (int ele : arr) {
      log.info("ele={}",ele);
    }

    //case3)
    Arrays.stream(arr).forEach(ele->{
      log.info("ele={}",ele);
    });

  }


  @Test
  void v2(){
    List<Person> persons = new ArrayList<>();
    persons.add(new Person("홍길동1",10));
    persons.add(new Person("홍길동2",20));
    persons.add(new Person("홍길동3",30));

    for (Person person : persons) {
      log.info("person={}",person);
    }

    persons.stream().forEach(person->log.info("person={}",person));

    persons.stream().filter(person -> person.getAge() >= 20)
        .forEach(person->log.info("person={}",person));
  }

  @Getter
  @ToString
  @AllArgsConstructor //모든 맴버필드를 매개변수로 갖는 생성자
  static class Person {
    private String name;
    private int age;
  }
}
