package com.kh.demo.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.URLEncoder;

@Slf4j

//빈 등록: @Component가 붙은 클래스는 Spring의 ApplicationContext에 빈으로 등록됩니다. 이렇게 등록된 빈은 다른 빈에서 주입받아 사용할 수 있습니다.
//자동 스캔: Spring Boot와 같은 설정에서는 @ComponentScan이 기본적으로 활성화되어 있어, 애플리케이션의 패키지 구조 내에서 @Component가 붙은 클래스를 자동으로 검색하여 빈으로 등록합니다.
//의존성 주입: 다른 빈에서 @Autowired를 사용하여 @Component가 붙은 빈을 주입받을 수 있습니다.
@Component    // springboot 구동시 객체가 자동으로 생성되어 빈 컨테이너에서 관리


//HandlerInterceptor에는 추상메소드가 없다
//유지보수용으로 default 메소드, static 메소드  ->필요한곳에서 재정의해서 쓰도록
public class LoginCheckInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    //리다이렉트 URL
    String redirectUrl = null;

    //요청 URI ex) GET http://localhost:9080/products?a=1&b=2 상품관리
    String requestURI = request.getRequestURI();      //    /products
//    log.info("requestURI={}",request.getRequestURI());  // /products
//    log.info("queryString="+request.getQueryString());  // a=1&b=2

    //요청 url에 쿼리스트링이 없는 경우
    if(request.getQueryString() == null){
      redirectUrl = requestURI;
    }else{
      //요청 url에 쿼리스트링이 있는 경우
      String queryString = URLEncoder.encode(request.getQueryString(), "UTF-8");    // a=1&b=2   쿼리스트링이 한글일 경우 인코딩을해줘야함
      StringBuffer str = new StringBuffer();
      redirectUrl = str.append(requestURI).append("?").append(queryString).toString();    // /products?a=1&b=2
    }
    //세션조회
    HttpSession session = request.getSession(false);

    //세션이 없거나, loginOkMember 정보가 없으면 로그엔페이지로 리다이렉트
    if(session == null || session.getAttribute("loginOkMember") == null) {
//      log.info("미인증 요청");

      //requestparam으로 ?뒤에 url을 읽어올수있다
      response.sendRedirect("/login?redirectUrl=" + redirectUrl);    //302 GET http://localhost:9080/login
//      return false;
    }
    return true;
  }
}
