package hello.springmvc.basic.request02;
/*
    HTTP 요청 파라미터 - 쿼리 파라미터, HTML Form
    : 서블릿으로 학습했던 내용을 스프링이 얼마나 깔끔하고 효율적으로 바꾸어주는지 알아보자.

    클라이언트에서 서버로 요청 데이터를 전달할 때는 주로 다음 3가지 방법을 사용한다.
     a. GET - 쿼리 파라미터
        + /url?username=hello&age=20
        + 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달
        + 예) 검색, 필터, 페이징등에서 많이 사용하는 방식
      b. POST - HTML Form
        + content-type: application/x-www-form-urlencoded
        + 메시지 바디에 쿼리 파리미터 형식으로 전달 username=hello&age=20
        + 예) 회원 가입, 상품 주문, HTML Form 사용
      c. HTTP message body에 데이터를 직접 담아서 요청
        + HTTP API에서 주로 사용, JSON, XML, TEXT
        + 데이터 형식은 주로 JSON 사용
        + POST, PUT, PATCH
*/
/*
    GET 실행
    http://localhost:8282/request-param-v1?username=hello&age=20

    Post Form 실행 = 리소스는 /resources/static 아래에 두면 스프링 부트가 자동으로 인식
    => /resources/static/basic/hello-form.html 에 코딩후 사용할 것
    http://localhost:8282/basic/hello-form.html  => 파라미터값 로그에 출력됨

    [참고]
    Jar 를 사용하면 webapp 경로를 사용할 수 없다
*/
import hello.springmvc.basic.HelloData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController02 {
    // #1. V1
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse
            response) throws IOException {
        // 폼 파라미터 받기
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        // 폼 파라미터 로그 출력
        log.info("username={}, age={}", username, age);
        // 브라우저 출력
        response.getWriter().write("ok");
    }

/*
    #2. V2
    HTTP 요청 파라미터 - @RequestParam => 파라미터 이름으로 바인딩
    스프링이 제공하는 @RequestParam 을 사용하면 요청 파라미터를 매우 편리하게 사용
*/
    @ResponseBody // @ResponseBody => View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력
    @RequestMapping("/request-param-v2")
    // @RequestParam("username") => 폼에서 넘어온 파라미터 이름으로 바인딩
    public String requestParamV2( @RequestParam("username") String memberName,
                                 @RequestParam("age") int memberAge) {
        log.info("username={}, age={}", memberName, memberAge);
        return "ok";
    }

    /*
        #3. V3
        @RequestParam 사용
        : HTTP 파라미터 이름이 변수 이름과 같으면 @RequestParam(name="xx") 생략 가능
    */
    @ResponseBody
    @RequestMapping("/request-param-v3")
    // @RequestParam("username") => ("username")폼에서 넘어온 키명과 같은 매개변수명을 쓰면 생략가능
    //                           => HTTP 파라미터 이름이 변수 이름과 같으면 @RequestParam(name="xx") 생략 가능
    public String requestParamV3( @RequestParam String username,
                                  @RequestParam int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /*
        #4. V4
        @RequestParam 사용
        : String, int 등의 단순 타입이면 @RequestParam 도 생략 가능 | 자바빈 이런 타입은 안됨

        가독성을 위해서 그냥 사용할 예정 - 너무 오바임
    */
    @ResponseBody
    @RequestMapping("/request-param-v4")
    // String , int , Integer 등의 단순 타입이면 @RequestParam 도 생략 가능
    public String requestParamV4( String username, int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /*
        #5. @RequestParam.required
          = 파라미터 필수 여부 설정이 필요할 때 사용
          = 기본값이 파라미터 필수( requested = true ) 임.

        [요청 테스트]
        http://localhost:8282/request-param-required?username=hello&age=20 -> 정상요청 테스트
        http://localhost:8282/request-param-required -> username이 없으므로 예외 | 400에러
        http://localhost:8282/request-param-required?username=
        -> 파라미터 이름만 있고 값이 없는 경우 빈문자로 통과 => 에러는 아님

        http://localhost:8282/request-param-required
        => int age -> null을 int에 입력하는 것은 불가능(500에러발생), 따라서 래퍼클래스 Integer로 변경해야 함
        = (또는 다음에 나오는 defaultValue 사용)
    */
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            // 파라미터 필수 여부 설정 = 만약, 파라미터 이름만 있고 값이 없는 경우 빈문자로 통과
            @RequestParam(required = true) String username,
            // /request-param 요청만 있을 경우 기본형(primitive)에 null 입력 | int로 하면 안됨 = int는 null 못받기 떄문에
            @RequestParam(required = false) Integer age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /*
        #6. @RequestParam.required = defaultValue 사용
          = 파라미터에 값이 없는 경우 defaultValue 를 사용하면 기본 값을 적용할 수 있다.
          = 이미 기본 값이 있기 때문에 required 는 의미가 없다. => required 안써줘도 됨

        [요청 테스트]
        http://localhost:8282/request-param-required?username=hello&age=20 -> 정상요청 테스트
        http://localhost:8282/request-param-required?username=
        -> defaultValue는 빈 문자의 경우에도 설정한 기본 값이 적용됨. => defaultValue = "guest"가 적용
    */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            // 파라미터에 값이 없는 경우 defaultValue 를 사용하면 기본 값이 적용됨
            @RequestParam(required = true, defaultValue = "guest") String username,
            // int 사용이 가능한 이유는 문자열 -1을 기본값으로 설정해 놨기 때문임
            @RequestParam(required = false, defaultValue = "-1") int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /*
        #7. @RequestParam.required = 파라미터를 Map으로 조회
          = 모든 파라미터값 받기

        [요청 테스트]
        http://localhost:8282/request-param-map?username=hello&age=20 -> 정상요청 테스트
    */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) { // 모든 파라미터값 받기
            log.info("username={}, age={}",
            paramMap.get("username"), paramMap.get("age")); // 값 꺼내기
      return "ok";
    }

    /*
        #8. HTTP 요청 파라미터 - @ModelAttribute
        : 실제 개발을 하면 요청 파라미터를 받아서 필요한 객체를 만들고 그 객체에 값을 넣어주어야 함 => setter 개념
        [스프링] = 이 과정을 완전히 자동화해주는 @ModelAttribute 기능을 제공(매우 편리함)

        [실습]
        먼저, 요청 파라미터를 바인딩 받을 객체, 자바빈 HelloData.java 만듦
    */
    /* ##0. 기존방식 자바빈 사용법 = 아래 V1, V2 사용할 것임 */
    @ResponseBody
    @RequestMapping("/model-attribute-v0")
    public String modelAttributeV0(@RequestParam String username, @RequestParam int age) {
        HelloData helloData = new HelloData();
        helloData.setUsername(username);
        helloData.setAge(age);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        log.info("helloData={}", helloData); // HelloData(username=abc, age=30) = 덩어리로 받음
        return "ok";
    }

    /* ## V1. @ModelAttribute 사용 자바빈 사용 */
/*
    스프링MVC는 @ModelAttribute 가 있으면 다음을 실행한다.
      a. HelloData 객체를 생성한다.
      b. 요청 파라미터의 이름으로 HelloData 객체의 프로퍼티를 찾는다.
      c. 그리고 해당 프로퍼티의 setter를 호출해서 파라미터의 값을 입력(바인딩) 한다.
      d. 예) 파라미터 이름이 username 이면 setUsername() 메서드를 찾아서 호출하면서 값을 입력한다.
*/
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }

    /* ## V2. @ModelAttribute 생략가능 */
/*
    스프링은 해당 (생략시) 다음과 같은 규칙을 적용. => 됐구 그냥 다 써.
      a. String , int , Integer 같은 단순 타입 = @RequestParam 로 인식해줌
      b. 나머지 = @ModelAttribute 즉, 내가 만든 객체는 이것로 인식해줌
              = 예외 => (argument resolver(=예약어 HttpServletRequest 같은것) 로 지정해둔 타입 외)
*/
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) { // @ModelAttribute 생략가능
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }







} // end of class


