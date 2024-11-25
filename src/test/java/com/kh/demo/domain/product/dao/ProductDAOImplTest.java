package com.kh.demo.domain.product.dao;


import com.kh.demo.domain.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest

public class ProductDAOImplTest {

  @Autowired    //해당타입의 개체를 찾아서 자동으로 주입
  ProductDAO productDAO;


  @Test
  @DisplayName("상품등록")
  void save() {
    log.info("save()호출됨!");
    Product product = new Product();
    product.setPname("의자");
    product.setPrice(1000000L);
    product.setQuantity(10L);
    Long pid = productDAO.save(product);
    log.info("상품아이디={}", pid);


  }

  @Test
  @DisplayName("상품목록")
  void findAll() {
    List<Product> list = productDAO.findAll();
    for (Product product : list) {
      log.info("product={}", product);
    }
  }

  @Test
  @DisplayName("요청 페이지 목록")
  void findReqPageAll() {
    int reqPage = 3;    //요청페이지
    int reqRec = 10;    //한페이지당 보여줄 레코드수
    List<Product> list = productDAO.findAll(reqPage, reqRec);
    for (Product product : list) {
      log.info("product={}", product);
    }
  }

  @Test
  @DisplayName("상품단건조회")
  void findById() {
    Long productId = 2L;
    Optional<Product> product = productDAO.findById(productId);
    //조회된 상품이 없으면 예외발생, 있으면 변수에 저장
    Product findedProduct = product.orElseThrow();
//    log.info("findedProduct={}",findedProduct);
    assertThat(findedProduct.getPname()).isEqualTo("모니터");
    assertThat(findedProduct.getQuantity()).isEqualTo(5L);
    assertThat(findedProduct.getPrice()).isEqualTo(500000L);


  }

  @Test
  @DisplayName("상품단건삭제")
  void deleteById() {
    Long productId = 800L;
    int rows = productDAO.deleteById(productId);

    assertThat(rows).isEqualTo(1);   //잘돌아가면 1 아니면 0

  }

  @Test
  @DisplayName("상품 수정")
  void updateById(){
    Long productId = 202L;
    Product product = new Product();
    product.setPname("볼펜4");
    product.setQuantity(40L);
    product.setPrice(4000L);

    int rows = productDAO.updateById(productId, product);
    Optional<Product> optionalProduct = productDAO.findById(productId);
    log.info("udatedProduct={}", optionalProduct);

    Product findedProduct = optionalProduct.orElseThrow();

    assertThat(findedProduct.getPname()).isEqualTo("볼펜4");
    assertThat(findedProduct.getQuantity()).isEqualTo(40L);
    assertThat(findedProduct.getPrice()).isEqualTo(4000L);
  }

  @Test
  @DisplayName("여러건 삭제")
  void deleteByIds(){

    List<Long> productIds = List.of(141L, 144L);
    int rows = productDAO.deleteByIds(productIds);

    assertThat(rows).isEqualTo(0);   //기대치와 이론값 비교
    log.info("rows={}", rows);
  }

  @Test
  @DisplayName("복수상품등록")
  void saveProducts() {
    for (int i = 0; i < 215; i++) {
      Product product = new Product();
      product.setPname("컴퓨터_"+i);
      product.setPrice(1000000L);
      product.setQuantity((long)(i+1));
      Long pid = productDAO.save(product);
      log.info("상품아이디={}", pid);
    }
  }

  @Test
  @DisplayName("총 건수")
  void totalRec(){
    int totalRec = productDAO.totalRec();
    assertThat(totalRec).isEqualTo(215);

//    log.info("totalRec={}",totalRec);
  }
}