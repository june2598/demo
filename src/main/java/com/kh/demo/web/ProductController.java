package com.kh.demo.web;

import com.kh.demo.domain.entity.Product;
import com.kh.demo.domain.product.svc.ProductSVC;
import com.kh.demo.web.form.product.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/products")  //url 서버 루트
@RequiredArgsConstructor
public class ProductController {

  private final ProductSVC productSVC;

//  public ProductController(ProductSVC productSVC){
//    this. productSVC = productSVC;
//  }

  //등록양식
  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("saveForm", new SaveForm());
    return "/product/add";//상품등록화면 - view이름, 화면의경로
  }

  //등록처리
  @PostMapping("/add")  // "post /products/add"
  public String add(
      @Valid  // form 객체에 필드별 유효성 체크를 실시
      @ModelAttribute SaveForm saveForm,  //@ModelAttribute : form 객체를 모댈겍체에 추가하여 view에서 참조
      BindingResult bindingResult, //BindingResult :  검증결과를 담는 객체
      RedirectAttributes redirectAttributes) {
    //사용자가 입력한정보
    log.info("panme={}, price={}, quantity={}", saveForm.getPname(), saveForm.getPrice(), saveForm.getQuantity());

    //요청데이터 유효성 체크
    //1. 어노테이션 기반의 필드검증
    if (bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "/product/add";
    }
    //2. 코드기반 검증 : 필드 및 글로벌 오류(필드 2개이상)
    //2.1 필드 오류 : 상품 상품수량 1000 초과 불가

    if(saveForm.getQuantity() > 1000) {
      bindingResult.rejectValue("quantity","product",new Object[]{100},null);  //product.saveForm.quantity,product.quantity, product
    }

    //2.2 글로벌 오류 : 총액(상품수량 * 단가) 1000만원 초과 불가
    if(saveForm.getPrice() * saveForm.getQuantity() > 10_000_000L) {
//      bindingResult.reject(null, "총액(상품수량 * 단가) 1000만원 초과 불가");
      bindingResult.reject("totalPrice",new Object[]{1000},null); // totalPrice.saveForm, totalPrice
    }

    if (bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "/product/add";
    }



    //상품테이블에 저장
    Product product = new Product();
    product.setPname(saveForm.getPname());
    product.setPrice(saveForm.getPrice());
    product.setQuantity(saveForm.getQuantity());

    Long pid = productSVC.save(product);
    // 리다이렉트 url경로 변수에 값을 동적으로 할당하는 용도
    redirectAttributes.addAttribute("id", pid);

    return "redirect:/products/{id}/"; //상품상세화면 302 get http://locahost:9080/products/2/detail
  }

  //목록양식
  @GetMapping  // get /products
  public String findAll(Model model) {
    List<Product> list = productSVC.findAll();
    List<AllForm> all = new ArrayList<>();

    for (Product product : list) {
      AllForm allForm = new AllForm();
      allForm.setProductId(product.getProductId());
      allForm.setPname(product.getPname());
      all.add(allForm);
    }

    model.addAttribute("all", all);

    return "/product/all"; // view이름
  }

  //상품단건조회
  @GetMapping("/{id}/")  // Get /products/2/detail
  public String findById(
      @PathVariable("id") Long productId,
      Model model) {
    log.info("productId={}", productId);

    Optional<Product> findedProduct = productSVC.findById(productId);
    Product product = findedProduct.orElseThrow();

    DetailForm detailForm = new DetailForm();
    detailForm.setProductId(product.getProductId());
    detailForm.setPname(product.getPname());
    detailForm.setPrice(product.getPrice());
    detailForm.setQuantity(product.getQuantity());

    model.addAttribute("detailForm", detailForm);

    return "/product/detailForm";
  }

  //단건삭제
  @GetMapping("/{id}/del")
  public String deleteById(@PathVariable("id") Long productId) {
    log.info("productId={}", productId);

    int rows = productSVC.deleteById(productId);

    return "redirect:/products"; // 302 get rediredUtrl: http://localhost:9080/products
  }

  //수정화면
  @GetMapping("/{id}/edit")
  public String updateForm(@PathVariable("id") Long productId, Model model) {

    Optional<Product> optionalProduct = productSVC.findById(productId);
    Product findedProduct = optionalProduct.orElseThrow();

    UpdateForm updateForm = new UpdateForm();
    updateForm.setProductId(findedProduct.getProductId());
    updateForm.setPname(findedProduct.getPname());
    updateForm.setPrice(findedProduct.getPrice());
    updateForm.setQuantity(findedProduct.getQuantity());

    model.addAttribute("updateForm", updateForm);

    return "/product/updateForm";
  }

  //단건수정처리
  @PostMapping("/{id}/edit")
  public String updatedById(
      @PathVariable("id") Long productId,
      UpdateForm updateForm,
      RedirectAttributes redirectAttributes) {
    log.info("productId={}", productId);
    log.info("updateForm={}", updateForm);

    Product product = new Product();
    product.setPname(updateForm.getPname());
    product.setPrice(updateForm.getPrice());
    product.setQuantity(updateForm.getQuantity());

    int rows = productSVC.updateById(productId, product);

    redirectAttributes.addAttribute("id", productId);
    return "redirect:/products/{id}/"; //  302 get redirectUrl->http://localhost:9080/products/2/detail
  }

  //여러건 삭제처리
  @PostMapping("/del")
  public String deleteByIds(@RequestParam("productIds") List<Long> productIds) {

    log.info("productIds={}", productIds);
    int rows = productSVC.deleteByIds(productIds);
    return "redirect:/products"; //302 get redirectUrl
  }
}