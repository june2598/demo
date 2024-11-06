package com.kh.demo.domain.product.svc;

import com.kh.demo.domain.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@Slf4j
@SpringBootTest
public class ProductSVCImplTest {

  @Autowired
  ProductSVC productSVC;

  @Test
  void findById(){
    Long productId = 2L;
    Optional<Product> product = productSVC.findById(productId);

    Product findedProduct = product.orElseThrow();

    Assertions.assertThat(findedProduct.getPname()).isEqualTo("모니터");
    Assertions.assertThat(findedProduct.getQuantity()).isEqualTo(5L);
    Assertions.assertThat(findedProduct.getPrice()).isEqualTo(500000L);
  }

}
