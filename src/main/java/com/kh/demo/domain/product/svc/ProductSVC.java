package com.kh.demo.domain.product.svc;

import com.kh.demo.domain.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductSVC {
  // 등록
  Long save(Product product);

  // 목록
  List<Product> findAll();

  //조회
  Optional<Product> findById(Long productId);

  //삭제
  int deleteById(Long productId);

  //수정
  int updateById(Long productId, Product product);
  //다중삭제
  int deleteByIds(List<Long> productIds);

  //페이지 목록
  List<Product> findAll(int reqPage, int reqRec);

  int totalRec();
}