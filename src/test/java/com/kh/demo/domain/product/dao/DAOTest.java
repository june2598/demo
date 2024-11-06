package com.kh.demo.domain.product.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

@Slf4j
@SpringBootTest

public class DAOTest {
  @Autowired
  ProductDAO productDAO;


  //추가: NamedParameterJdbcTemplate 변수를 @Autowired로 주입받아 사용하도록 수정하였습니다.
  //이렇게 해야 Spring이 해당 빈을 주입하고 사용할 수 있게 됩니다.
  @Autowired
  NamedParameterJdbcTemplate template;


  @Test
  @DisplayName("StringBuffer연습")
  void test1(){
    StringBuffer str = new StringBuffer();
    str.append("kh");
    str.append("인재교육원");
    log.info("str={}", str);
  }

  @Test
  @DisplayName("단일행 단일열 테스트")
  void sqlTest1(){
//    StringBuffer sql = new StringBuffer();
    String sql = "select count(*) from product ";
    String sql2 = "select pname from product where pname = '모니터' ";

    Long cnt = template.queryForObject(sql, Map.of(), Long.class);
    log.info("cnt={}", cnt);

    String pname = template.queryForObject(sql2, Map.of(), String.class);
    log.info("pname={}", pname);
  }
}
