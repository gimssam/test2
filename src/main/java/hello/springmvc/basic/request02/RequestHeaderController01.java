package hello.springmvc.basic.request02;
/*
# HTTP 요청이 보내는 데이터들을 스프링 MVC로 어떻게 조회 하는 방법들을 공부합시다!!

    #. HTTP 요청 - 기본, 헤더 조회
      : 애노테이션 기반의 스프링 컨트롤러는 다양한 파라미터를 지원함.

    ##. MultiValueMap
      : MAP과 유사한데, 하나의 키에 여러 값을 받을 수 있다.
        HTTP header, HTTP 쿼리 파라미터와 같이 하나의 키에 여러 값을 받을 때 사용한다.
        keyA=value1&keyA=value2
     [소스예제]
     MultiValueMap<String, String> map = new LinkedMultiValueMap();
     map.add("keyA", "value1");
     map.add("keyA", "value2");
     //[value1,value2]
     List<String> values = map.get("keyA");  // List타입 배열로 반환됨
*/
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

// log 바로 사용할수 있는 애너테이션
// 자동으로 생성해서 로그를 선언 , 개발자는 편리하게 log 라고 사용하면 됨.
@Slf4j
@RestController
public class RequestHeaderController01 {

    @RequestMapping("/headers")
    // 서블릿, 스프링 지원 많은 파라미터들 받을수 있다. = @Conroller 의 사용 가능한 파라미터/응답값 목록은 공식 메뉴얼에서 확인가능
    // (참고) https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-arguments
    // (참고) https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-return-types
    public String headers(HttpServletRequest request, HttpServletResponse response,
                          // HttpMethod => HTTP 메서드를 조회
                          HttpMethod httpMethod,
                          // Locale => Locale 정보를 조회 , 언어정보 , 가장 우선순위가 높은 ko_KR이 로그에 출력
                          Locale locale,
                          // 모든 HTTP 헤더를 MultiValueMap 형식으로 조회
                          // 상단 설명 참조
                          @RequestHeader MultiValueMap<String, String> headerMap,

                          // 특정 HTTP 헤더를 조회 | 필수 값 여부: required , 기본 값 속성: defaultValue
                          @RequestHeader("host") String host,

                          // 특정 쿠키 조회 | 필수 값 여부: required , 기본 값 속성: defaultValue
                          @CookieValue(value = "myCookie", required = false) String cookie
                          ) {
        // 로그에 출력하기
        log.info("request={}", request); // org.apache.catalina.connector.RequestFacade@5f09b6f4
        log.info("response={}", response); // org.apache.catalina.connector.ResponseFacade@55cda7c9
        log.info("httpMethod={}", httpMethod); // GET
        log.info("locale={}", locale); // ko
        log.info("headerMap={}", headerMap); // header 모든 정보 출력
        log.info("header host={}", host); // localhost:8282
        log.info("myCookie={}", cookie); // 쿠키는 현재 없으므로 null

        return "ok";
    }
}
