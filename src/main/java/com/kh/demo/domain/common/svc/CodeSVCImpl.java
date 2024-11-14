package com.kh.demo.domain.common.svc;

import com.kh.demo.domain.common.Code;
import com.kh.demo.domain.common.CodeId;
import com.kh.demo.domain.common.dao.CodeDAO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeSVCImpl implements CodeSVC {

  private final CodeDAO codeDAO;
  private List<Code> a02;   //PostConstruct에 의해 return a02;가 자동으로 담긴다.

  @Override
  public List<Code> getCodes(CodeId pcodeId) {
    return codeDAO.loadCodes(pcodeId);
  }

  @PostConstruct  // 생성자 호출후 실행될 메소드에 선언하면 해당 메소드가 자동호출
  private List<Code> getA02Code() {
    log.info("getA02Code() 수행됨!");
    a02 = codeDAO.loadCodes(CodeId.A02);
    return a02;
  }
  public List<Code> getA02() {
    return a02;
  }
}
