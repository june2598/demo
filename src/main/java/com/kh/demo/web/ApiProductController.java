package com.kh.demo.web;

import com.kh.demo.domain.entity.Product;
import com.kh.demo.domain.product.svc.ProductSVC;
import com.kh.demo.web.api.ApiResponse;
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
@RestController       //@Controller + @ResponseBody
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ApiProductController {

  private final ProductSVC productSVC;


  //단건조회
  @GetMapping("/{pid}")
//  @ResponseBody
  public ApiResponse<Product> findById(@PathVariable("pid") Long pid){
    ApiResponse<Product> res = null;
    Optional<Product> optionalProduct = productSVC.findById(pid);

    if(optionalProduct.isPresent()){
      Product product = optionalProduct.get();
      res = ApiResponse.createApiResponse("00","success",product);    //객체생성 못하고(private기때문) 정적메소드로
    }else{
      res = ApiResponse.createApiResponse("01","not found",null);   //못찾았을시
    }
    return res;
  }

  //목록
  @GetMapping
//  @ResponseBody   // : json타입으로 뷰가 아니고 데이터를 반환
  public ApiResponse<List<Product>> all() {
    ApiResponse<List<Product>> res = null;
    List<Product> products = productSVC.findAll();
    if(products.size() != 0) {
      res = ApiResponse.createApiResponse("00", "success", products);
    }else{
      res = ApiResponse.createApiResponse("01", "not found", null);
    }
    return res;
  }

  //상품등록
  @PostMapping
  public ApiResponse<Product> add(@RequestBody ReqSave reqSave){
    log.info("reqSave={}",reqSave);
    ApiResponse<Product> res = null;

    Product product = new Product();
    BeanUtils.copyProperties(reqSave, product);          //자동매칭 (일일히 세터 메소드로 수동으로 안해도됨)
    Long pid = productSVC.save(product);

    Optional<Product> optionalProduct = productSVC.findById(pid);
    if(optionalProduct.isPresent()){
      Product savedProduct = optionalProduct.get();
      res = ApiResponse.createApiResponse("00","success", savedProduct);
    }else{
      res = ApiResponse.createApiResponse("01","fail",null);
    }
    return res;
  }

  //삭제

  @DeleteMapping("/{pid}")  // delete http://localhost:9080/api/products/{pid}
  public ApiResponse delete(@PathVariable("pid") Long pid){
    ApiResponse res = null;

    int rows = productSVC.deleteById(pid);
    if(rows == 1){
      res = ApiResponse.createApiResponse("00","success",null);
    }else{
      res = ApiResponse.createApiResponse("01","not found",null);
    }

    return res;
  }


  //상품수정

  @PatchMapping("/{pid}")
  public ApiResponse update(@PathVariable("pid") Long pid, @RequestBody ReqUpdate reqUpdate){
    log.info("reqUpdate={}", "reqUpdate={}", pid, reqUpdate);
    ApiResponse res = null;

    Product product = new Product();
    BeanUtils.copyProperties(reqUpdate,product);
    int rows = productSVC.updateById(pid,product);

    if(rows == 1){
      Product updatedProduct = productSVC.findById(pid).get();
      res = ApiResponse.createApiResponse("00","success",updatedProduct);
    }else{
      res = ApiResponse.createApiResponse("01","not found",null);
    }

    return res;
  }

  //여러건 삭제
  @DeleteMapping
  public ApiResponse<String> deleteByIds(@RequestBody ReqDels reqDels){
    log.info("reqDels={}",reqDels);
    ApiResponse<String> res = null;

    int rows = productSVC.deleteByIds(reqDels.getProductIds()); //삭제 건수
    if(rows > 0){
      res = ApiResponse.createApiResponse("00","success","삭제건수 : " + rows);
    }else{
      res = ApiResponse.createApiResponse("01","not found",null);
    }

    return res;
  }
}
