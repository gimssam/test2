package hello.springmvc.basic.response03;
/*
    HTTP 응답 - HTTP API, 메시지 바디에 직접 입력
     : HTTP API를 제공하는 경우에는 HTML이 아니라 데이터를 전달해야 하므로,
       HTTP 메시지 바디에 JSON 같은 형식으로 데이터를 실어 보낸다.

    [참고]
    @RestController = @Controller + @ResponseBody
    : @Controller 대신에 @RestController 애노테이션을 사용하면,
      해당 컨트롤러에 모두 @ResponseBody 가 적용되는 효과가 있음.
      따라서 뷰 템플릿을 사용하는 것이 아니라, HTTP 메시지 바디에 직접 데이터를 입력함.
      이름 그대로 Rest API(HTTP API)를 만들 때 사용하는 컨트롤러 임.

    [참고로] @ResponseBody 는 클래스 레벨에 두면 전체 메서드에 적용
    => @RestController 에노테이션 안에 @ResponseBody 가 적용되어 있음.
*/
import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//@Controller
//@ResponseBody  => 전역 영역에 코딩하면 각각 메서드에 중복 코딩을 막을수도 있음, 생략가능
@RestController  // = @Controller + @ResponseBody(생략가능)
public class ResponseBodyController02 {
/* #1. 서블릿 방식 = HttpServletResponse 객체를 통해서 HTTP 메시지 바디에 직접 ok 응답 메시지를 전달 */
    @GetMapping("/response-body-string-v1")
    public void responseBodyV1(HttpServletResponse response) throws IOException {
        response.getWriter().write("ok"); // 브라우저 문자열 ok 출력
    }

/* #2. HttpEntity, ResponseEntity(Http Status 추가) */
    @GetMapping("/response-body-string-v2")
    // ResponseEntity 엔티티 = HttpEntity 를 상속 받았는데, HttpEntity는 HTTP 메시지의 헤더, 바디 정보를 가지고 있음
    public ResponseEntity<String> responseBodyV2() {
        // ResponseEntity 는 여기에 더해서 HTTP 응답 코드를 설정
        return new ResponseEntity<>("ok", HttpStatus.OK); // HttpStatus.CREATED 로 변경하면 201 응답이 나가는 것을 확인
    }

/* #3. @ResponseBody 를 사용하면 view를 사용하지 않고, HTTP 메시지 컨버터를 통해서 HTTP 메시지를 직접 입력
       = ResponseEntity 도 동일한 방식으로 동작*/
//    @ResponseBody
    @GetMapping("/response-body-string-v3")
    public String responseBodyV3() {
        return "ok";
    }

/* JSON 처리 */
/* #1.ResponseEntity 를 반환. HTTP 메시지 컨버터를 통해서 JSON 형식으로 변환되어서 반환 */
    @GetMapping("/response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1() { //HTTP 메시지 컨버터를 통해서 JSON 형식으로 변환되어서 반환
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return new ResponseEntity<>(helloData, HttpStatus.OK); // 메세지 코드 줄수 있음 | HttpStatus.CREATED
    }

/* #1. ResponseEntity 는 HTTP 응답 코드를 설정,
       @ResponseBody 를 사용하면 이런 것을 설정하기 까다로움.
       @ResponseStatus(HttpStatus.OK) 애노테이션을 사용하면 응답 코드도 설정
       = 물론 애노테이션이기 때문에 응답 코드를 동적으로 변경할 수는 없음
       = 프로그램 조건에 따라서 동적으로 변경하려면 ResponseEntity 를 사용하면 됨.(위의 것 참조) */
    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
    @GetMapping("/response-body-json-v2")
    public HelloData responseBodyJsonV2() {
        // 자바빈 사용
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return helloData;
    }
} // end of class
