package com.kh.demo.domain.common.svc;

import com.kh.demo.domain.common.Code;
import com.kh.demo.domain.common.CodeId;

import java.util.List;

public interface CodeSVC {

  List<Code> getCodes(CodeId pcodeId);

  List<Code> getA02();

}
