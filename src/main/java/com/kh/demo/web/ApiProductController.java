package com.kh.demo.web;

import com.kh.demo.domain.entity.Product;
import com.kh.demo.domain.product.svc.ProductSVC;
import com.kh.demo.web.api.ApiResponse;
import com.kh.demo.web.api.ApiResponseCode;
import com.kh.demo.web.exception.BusinessException;
import com.kh.demo.web.req.product.ReqDels;
import com.kh.demo.web.req.product.ReqSave;
import com.kh.demo.web.req.product.ReqUpdate;
import com.kh.demo.web.util.KhUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
//@Controller
@RestController // @Controller + @ResponseBody
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ApiProductController {

  private final ProductSVC productSVC;

  //단건조회
  @GetMapping("/{pid}")
//  @ResponseBody
  public ApiResponse<Product> findById(@PathVariable("pid") Long pid) {
    ApiResponse<Product> res = null;
    Optional<Product> optionalProduct = productSVC.findById(pid);

    if (optionalProduct.isPresent()) {
      Product product = optionalProduct.get();
      res = ApiResponse.of(ApiResponseCode.SUCCESS, product);
    } else {
      throw new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, null);
    }
    return res;
  }

  //목록
//  @GetMapping
//  @ResponseBody
  public ApiResponse<List<Product>> all() {
    ApiResponse<List<Product>> res = null;
    List<Product> products = productSVC.findAll();
    if (products.size() != 0) {
      res = ApiResponse.of(ApiResponseCode.SUCCESS, products);
    } else {
      throw new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, null);
    }
    return res;
  }

  @GetMapping
//  @ResponseBody
  public ApiResponse<List<Product>> reqPage(
      @RequestParam(value="reqPage",defaultValue = "1") Integer reqPage, //요청페이지
      @RequestParam(value="reqRec",defaultValue = "10") Integer reqRec  //요청페이지 레코드수(한페이지에 보여줄 레코드수)
  ) {
    ApiResponse<List<Product>> res = null;
    List<Product> products = productSVC.findAll(reqPage,reqRec);
    int totalRec = productSVC.totalRec();
    if (products.size() != 0) {
      res = ApiResponse.of(ApiResponseCode.SUCCESS, products,totalRec);
    } else {
      throw new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, null);
    }
    return res;
  }

  //상품등록
  @PostMapping
  public ApiResponse<Product> add(
      @Valid @RequestBody ReqSave reqSave,
      BindingResult bindingResult) {
    log.info("reqSave={}", reqSave);

    ApiResponse<Product> res = null;

    // 요청데이터 유효성 체크
    // 1. 어노테이션 기반의 필드 검증
    if (bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      throw new BusinessException(ApiResponseCode.VALIDATION_ERROR, KhUtil.getValidChkMap(bindingResult));
    }

    // 2. 코드기반 검증 : 필드 및 글로벌 오류(필드 2개이상)
    // 2.1 필드 오류 : 상품수량 100 초과 불가
    if (reqSave.getQuantity() > 100) {
//      bindingResult.rejectValue("quantity",null,"상품수량 100 초과 불가");
      bindingResult.rejectValue("quantity", "product", new Object[]{100}, null);  //product.saveForm.quantity,product.quantity,product
    }

    // 2.2 글로벌 오류 : 총액(상품수량 * 단가) 1000 만원 초과 불과
    if (reqSave.getPrice() * reqSave.getQuantity() > 10_000_000L) {
//      bindingResult.reject(null,"총액(상품수량 * 단가) 1000 만원 초과 불과");
      bindingResult.reject("totalPrice", new Object[]{1000}, null); //totalPrice.saveForm,totalPrice
    }

    if (bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      throw new BusinessException(ApiResponseCode.VALIDATION_ERROR, KhUtil.getValidChkMap(bindingResult));
    }

    Product product = new Product();
    BeanUtils.copyProperties(reqSave, product);
    Long pid = productSVC.save(product);

    Optional<Product> optionalProduct = productSVC.findById(pid);
    if (optionalProduct.isPresent()) {
      Product savedProduct = optionalProduct.get();
      res = ApiResponse.of(ApiResponseCode.SUCCESS, savedProduct);
    } else {
      throw new BusinessException(ApiResponseCode.INTERNAL_SERVER_ERROR, null);
    }
    return res;
  }

  //상품삭제
  @DeleteMapping("/{pid}")   // delete http://localhost:9080/api/products/123
  public ApiResponse<String> delete(@PathVariable("pid") Long pid) {
    ApiResponse<String> res = null;

    int rows = productSVC.deleteById(pid);
    if (rows == 1) {
      res = ApiResponse.of(ApiResponseCode.SUCCESS, null);
    } else {
      throw new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, null);
    }
    return res;
  }

  //상품수정
  @PatchMapping("/{pid}")
  public ApiResponse<Product> update(
      @PathVariable("pid") Long pid,
      @Valid @RequestBody ReqUpdate reqUpdate,
      BindingResult bindingResult) {
    log.info("pid={}, reqUpdate={}", pid, reqUpdate);
    ApiResponse<Product> res = null;

    // 요청데이터 유효성 체크
    // 1. 어노테이션 기반의 필드 검증
    if (bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      throw new BusinessException(ApiResponseCode.VALIDATION_ERROR, KhUtil.getValidChkMap(bindingResult));
    }

    // 2. 코드기반 검증 : 필드 및 글로벌 오류(필드 2개이상)
    // 2.1 필드 오류 : 상품수량 100 초과 불가
    if (reqUpdate.getQuantity() > 100) {
//      bindingResult.rejectValue("quantity",null,"상품수량 100 초과 불가");
      bindingResult.rejectValue("quantity", "product", new Object[]{100}, null);  //product.saveForm.quantity,product.quantity,product
    }

    // 2.2 글로벌 오류 : 총액(상품수량 * 단가) 1억 초과 불과
    if (reqUpdate.getPrice() * reqUpdate.getQuantity() > 100_000_000L) {
//      bindingResult.reject(null,"총액(상품수량 * 단가) 1억 만원 초과 불과");
      bindingResult.reject("totalPrice", new Object[]{10_000}, null); //totalPrice.saveForm,totalPrice
    }

    if (bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      throw new BusinessException(ApiResponseCode.VALIDATION_ERROR, KhUtil.getValidChkMap(bindingResult));
    }


    Product product = new Product();
    BeanUtils.copyProperties(reqUpdate, product);
    int rows = productSVC.updateById(pid, product);
    if (rows == 1) {
      Product updatedProduct = productSVC.findById(pid).get();
      res = ApiResponse.of(ApiResponseCode.SUCCESS, updatedProduct);
    } else {
      throw new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, null);
    }
    return res;
  }

  //여러건 삭제
  @DeleteMapping
  public ApiResponse<String> deleteByIds(@RequestBody ReqDels reqDels) {
    log.info("reqDels={}", reqDels);
    ApiResponse<String> res = null;

    int rows = productSVC.deleteByIds(reqDels.getProductIds());
    if (rows > 0) {
      res = ApiResponse.withDetails(ApiResponseCode.SUCCESS, Map.of("cntOfDel", String.valueOf(rows)), null);
    } else {
      throw new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, null);
    }
    return res;
  }

  // 전체레코드수 가져오기
  @GetMapping("/totalCnt")
  public ApiResponse<Integer> totalCnt(){
    ApiResponse<Integer> res = null;
    Integer totalRec =  productSVC.totalRec();

    res = ApiResponse.of(ApiResponseCode.SUCCESS, null,totalRec);

    return res;
  }
}