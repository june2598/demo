package com.kh.demo.web.form.product;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveFormOld {
  @NotBlank(message = "상품명은 필수 입니다.")
  @Size(min=1, max = 10,message = "상품명은 10글자 이하.")
  private String pname;

  @NotNull(message = "상품수량은 필수 입니다.")
  @Positive(message = "수량은 양수여야 합니다.")
  @Max(value=9999999999L,message = "수량은 10자리 이하")
  private Long quantity;

  @NotNull (message = "상품가격은 필수 입니다.")
  @Min(value = 1000,message = "상품가격 1000원미만 불가.")
  @Max(value=9999999999L,message = "상품가격은 10자리 이하.")
  private Long price;
}