package com.kh.demo.domain.product.dao;

import com.kh.demo.domain.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {
  // 등록
  Long save(Product product);

  // 목록
  List<Product> findAll();


  // 페이지목록

  /**
   * 요청페이지 레코드 가져오기
   * @param reqPage 요청 페이지
   * @param reqRec 한 페이지에 보여줄 레코드 수
   * @return
   */
  List<Product> findAll(int reqPage, int reqRec);

  //조회
  Optional<Product> findById(Long productId);

  //삭제
  int deleteById(Long productId);

  //수정
  int updateById(Long productId, Product product);

  //여러건 삭제
  int deleteByIds(List<Long> productIds);

  //총 레코드 건수
  int totalRec();





}