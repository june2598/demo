package com.kh.demo.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class WrapperClassTest {
  @Test
  void t1(){
    int v = 1;

    Integer v2 = Integer.valueOf(1);
    Integer v3 = 1;

    log.info("v2={},v3={}",v2,v3);

  }
}
