package com.kh.demo.web.form.product;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveForm {
  @NotBlank
  @Size(min=1, max = 10)
  private String pname;

  @NotNull
  @Positive
  @Max (value=9999999999L)
  private Long quantity;

  @NotNull
  @Min(value = 1000)
  @Max(value=9999999999L)
  private Long price;
}