package com.kh.demo.domain.common.dao;

import com.kh.demo.domain.common.Code;
import com.kh.demo.domain.common.CodeId;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class CodeDAOImplTest {

  @Autowired
  private CodeDAO codeDAO;

  @Test
  @DisplayName("코드조회")
  void loadCodes() {
    List<Code> codes = codeDAO.loadCodes(CodeId.F01);
    codes.stream().forEach(ele->log.info("{}",ele));
  }
}