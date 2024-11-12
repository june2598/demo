package com.kh.demo.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TryCatchFinallyTest {

  @Test
  void t1() {
    int[] arr = new int[]{1, 2, 3};

    log.info("{}", 1);
    int val = arr[3];     //ArrayIndexOutOfBoundsException : 배열 범위 초과
    log.info("{}", 2);     //실행이 안됨, 위에서 오류가 발생해서 강제종료되었기 때문
  }

  @Test
  void t2() {
    int[] arr = new int[]{1, 2, 3};

    log.info("{}", 1);
    try {
      //예외가 발생될수 있는 코드
      int val = arr[3];               //ArrayIndexOutOfBoundsException : 배열 범위 초과
    } catch (ArrayIndexOutOfBoundsException e) {   // 여기에 위치해야 됨 : catch절의 매개변수 타입은 하위타입이 위에 오도록 배치
      log.info("err={}", e.toString());
    } catch (Exception e) {             //Exception : 오류의 가장 상위 타입
      log.info("err={}", e.toString());
//    }catch(ArrayIndexOutOfBoundsException e){   //오류 : 상위 타입인 Exception에서 이미 오류가 잡힘, 따라서 디테일하게 걸러내기 위해선 상위타입이 하단에 위치해야된다.
//      log.info("err={}",e.toString());
    } finally {
      //예외 유무와 상관없이 수행되는 코드
      log.info("{}", "예외 유무와 상관없이 수행되는 코드");
    }
    log.info("{}", 2);     //실행이 안됨, 위에서 오류가 발생해서 강제종료되었기 때문
  }

  @Test
  void t3() {    //checkedException : 반드시 try catch로 오류를 잡아줘야됨 (컴파일 자체가안됨)
    try {
      Class.forName("java.lang.String");

    } catch (ClassNotFoundException e) {
      log.info("e={}", e);
    }
  }

  void t4() throws Exception {
//    try {
      t5();
//    } catch (Exception e) {

//    }
  }

  @Test
  void t5() throws ClassNotFoundException {        //메소드를 호출한쪽으로 예외처리를 위임
    Class.forName("java.lang.String");
  }
}

