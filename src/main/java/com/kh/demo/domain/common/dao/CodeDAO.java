package com.kh.demo.domain.common.dao;

import com.kh.demo.domain.common.Code;
import com.kh.demo.domain.common.CodeId;

import java.util.List;

public interface CodeDAO {
  //
  List<Code> loadCodes(CodeId pcodeId);

}
