package com.kh.demo.web;

import com.kh.demo.domain.entity.Product;
import com.kh.demo.domain.product.svc.ProductSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
//@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductControllerV2 {

  private final ProductSVC productSVC;

//  public ProductController(ProductSVC productSVC){
//    this. productSVC = productSVC;
//  }

  //등록양식
  @GetMapping("/add")
  public String addForm(){

    return "/product/add";//상품등록화면
  }
  //등록처리
  @PostMapping("/add")  // "post /products/add"
  public String add(
          @RequestParam("pname") String pname,
          @RequestParam("price") Long price,
          @RequestParam("quantity") Long quantity){

//    log.info("panme={}", pname);
//    log.info("price={}", price);
//    log.info("quantity={}", quantity);
    //사용자가 입력한정보
    log.info("panme={}, price={}, quantity={}", pname,price, quantity);
    //상품테이블에 저장
    Product product = new Product();
    product.setPname(pname);
    product.setPrice(price);
    product.setQuantity(quantity);

    productSVC.save(product);

    return "/product/detailForm"; //상품상세화면
  }

  //목록양식
  @GetMapping
  public String findAll(){
    return "/product/all"; // view이름
  }
}