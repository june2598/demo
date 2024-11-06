package com.kh.demo.domain.entity;

import lombok.Data;

@Data   //data anotation : getter setter + constructor 자동생성
  //All together now: A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields,
  //and @Setter on all non-final fields, and @RequiredArgsConstructor!

public class Product {
  private Long productId;  // 상품아이디 PRODUCT_ID	NUMBER(10,0)  편의상으로 spring에서 자동으로 camel표기법으로 수정해줌(같은 값이라는뜻)
  private String pname;     // 상품명 PNAME	VARCHAR2(30 BYTE)
  private Long quantity;    // 상품수량 QUANTITY	NUMBER(10,0)
  private Long price;       // 상품가격 PRICE	NUMBER(10,0)
//
//  //constructor
//  public Product(Long product_id, String pname, Long quantity, Long price) {
//    this.product_id = product_id;
//    this.pname = pname;
//    this.quantity = quantity;
//    this.price = price;
//  }
//
//  //getter setter : alt + insert
//  public Long getProduct_id() {
//    return product_id;
//  }
//
//  public void setProduct_id(Long product_id) {
//    this.product_id = product_id;
//  }
//
//  public String getPname() {
//    return pname;
//  }
//
//  public void setPname(String pname) {
//    this.pname = pname;
//  }
//
//  public Long getQuantity() {
//    return quantity;
//  }
//
//  public void setQuantity(Long quantity) {
//    this.quantity = quantity;
//  }
//
//  public Long getPrice() {
//    return price;
//  }
//
//  public void setPrice(Long price) {
//    this.price = price;
//  }
}
