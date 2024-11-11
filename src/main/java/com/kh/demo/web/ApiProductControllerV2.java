package com.kh.demo.web;

import com.kh.demo.domain.entity.Product;
import com.kh.demo.domain.product.svc.ProductSVC;
import com.kh.demo.web.api.ApiResponseV2;
import com.kh.demo.web.req.product.ReqDels;
import com.kh.demo.web.req.product.ReqSave;
import com.kh.demo.web.req.product.ReqUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
//@Controller
//@RestController // @Controller + @ResponseBody
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ApiProductControllerV2 {

  private final ProductSVC productSVC;

  //단건조회
  @GetMapping("/{pid}")
//  @ResponseBody
  public ApiResponseV2<Product> findById(@PathVariable("pid") Long pid){
    ApiResponseV2<Product> res = null;
    Optional<Product> optionalProduct = productSVC.findById(pid);

    if(optionalProduct.isPresent()){
      Product product = optionalProduct.get();
      res = ApiResponseV2.createApiResponse("00","success",product);
    }else{
      res = ApiResponseV2.createApiResponse("01","not found",null);
    }

    return res;
  }

  //목록
  @GetMapping
//  @ResponseBody
  public ApiResponseV2<List<Product>> all() {
    ApiResponseV2<List<Product>> res = null;
    List<Product> products = productSVC.findAll();
    if (products.size() != 0) {
      res = ApiResponseV2.createApiResponse("00", "success", products);
    }else{
      res = ApiResponseV2.createApiResponse("01", "not found", null);
    }
    return res;
  }

  //상품등록
  @PostMapping
  public ApiResponseV2<Product> add(@RequestBody ReqSave reqSave){
    log.info("reqSave={}",reqSave);
    ApiResponseV2<Product> res = null;

    Product product = new Product();
    BeanUtils.copyProperties(reqSave, product);
    Long pid = productSVC.save(product);

    Optional<Product> optionalProduct = productSVC.findById(pid);
    if(optionalProduct.isPresent()) {
      Product savedProduct = optionalProduct.get();
      res = ApiResponseV2.createApiResponse("00", "success", savedProduct);
    }else{
      res = ApiResponseV2.createApiResponse("99", "fail", null);
    }
    return res;
  }

  //상품삭제
  @DeleteMapping("/{pid}")   // delete http://localhost:9080/api/products/123
  public ApiResponseV2<String> delete(@PathVariable("pid") Long pid){
    ApiResponseV2<String> res = null;

    int rows = productSVC.deleteById(pid);
    if(rows == 1){
      res = ApiResponseV2.createApiResponse("00", "success", null);
    }else{
      res = ApiResponseV2.createApiResponse("01", "not found", null);
    }

    return res;
  }


  //상품수정
  @PatchMapping("/{pid}")
  public ApiResponseV2<Product> update(@PathVariable("pid") Long pid, @RequestBody ReqUpdate reqUpdate){
    log.info("pid={}, reqUpdate={}", pid, reqUpdate);
    ApiResponseV2<Product> res = null;

    Product product = new Product();
    BeanUtils.copyProperties(reqUpdate, product);
    int rows = productSVC.updateById(pid, product);
    if(rows == 1){
      Product updatedProduct = productSVC.findById(pid).get();
      res = ApiResponseV2.createApiResponse("00", "success", updatedProduct);
    }else{
      res = ApiResponseV2.createApiResponse("01", "not found", null);
    }

    return res;
  }

  //여러건 삭제
  @DeleteMapping
  public ApiResponseV2<String> deleteByIds(@RequestBody ReqDels reqDels){
    log.info("reqDels={}",reqDels);
    ApiResponseV2<String> res = null;

    int rows = productSVC.deleteByIds(reqDels.getProductIds());
    if(rows > 0){
      res = ApiResponseV2.createApiResponse("00", "success", "삭제건수 : " + rows);
    }else{
      res = ApiResponseV2.createApiResponse("01", "not found", null);
    }
    return res;
  }

}