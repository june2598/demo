package com.kh.demo;

import com.kh.demo.web.interceptor.ExecutionTimeInterceptor;
import com.kh.demo.web.interceptor.LoggingInterceptor;
import com.kh.demo.web.interceptor.LoginCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AppConfig implements WebMvcConfigurer {

  private final LoginCheckInterceptor loginCheckInterceptor;
  private final LoggingInterceptor loggingInterceptor;
  private final ExecutionTimeInterceptor executionTimeInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    //인증체크 (다수일경우 순서 중요!)
//    registry.addInterceptor(loginCheckInterceptor)
//            .order(2) //인터셉터 실행 순서 지정
//            .addPathPatterns("/**")   // 루트부터 하위경로 모두에 대해 인증체크(인터셉터에 포함)를 하겠다.
//            .excludePathPatterns(              // 예외를 두겠다.
//                "/",                  // 초기화면
//                "/login",
//                "/logout",
//                "/members/join",
//                "/css/**",
//                "/js/**",
//                "/img/**",
//                "/api/**",
//                "/test/**",
//                "/error/**",
//                "/webjars/**",
//                "/services/**"
//            );
//    registry.addInterceptor(loggingInterceptor)
//        .addPathPatterns("/**");

    registry.addInterceptor(executionTimeInterceptor)
        .order(1)
        .addPathPatterns("/**");
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")                     //요청 URL
        .allowedOrigins("http://localhost:5501")         //요청 Client
        .allowedMethods("*")                            //모든 Method
        .maxAge(3000);                                   //캐쉬시간
  }
}
